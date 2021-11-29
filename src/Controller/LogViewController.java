/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Logmodel;
import Model.Usermodel;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class LogViewController implements Initializable {
    @FXML
    private TextField logDateField;
    
    @FXML
    private TextArea logContentField;
    
    @FXML
    private TextField nameTextBox;
    
    @FXML
    private TextField idTextBox;
        
    EntityManager manager;
   
    private Integer logId;
    
    Scene previousScene;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();
    }

    /**
     * 
     * @param log 
     */
    public void initData(Logmodel log) {
        Query query = manager.createNamedQuery("Usermodel.findById");
        query.setParameter("id", log.getUserId());
        Usermodel user = (Usermodel) query.getSingleResult();

        idTextBox.setText("" + user.getId());
        nameTextBox.setText(user.getName());
        logDateField.setText(log.getDate());
        logContentField.setText(log.getContent());
        
        logId = log.getId();
    }
    
    /********************************Button Operations********************************/

    /**
     * 
     * @param event 
     */
    @FXML
    private void deleteLog(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete this log?");
        alert.setContentText("Are you sure you want to delete this log entry?");

        Optional<ButtonType> result = alert.showAndWait();
       
        if (result.get() == ButtonType.OK) {
            Logmodel log = logQuery();
            Usermodel user = userQuery();

            delete(log);
           
            try {
                refreshProfileView(event, user);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * 
     * @param event 
     */
    @FXML
    private void cancelLog(ActionEvent event) {
        Usermodel user = userQuery();

        try {
            refreshProfileView(event, user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void editLog(ActionEvent event) throws IOException {
        Usermodel user = userQuery();
        Logmodel log = logQuery();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateLogView.fxml"));
            Parent createLogView = loader.load();

            Scene createLogScene = new Scene(createLogView);

            CreateLogViewController controller = loader.getController();
            controller.initData(user, log);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(createLogScene);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**********************************Helper Methods***********************************/
    
    /**
     * 
     * @param log 
     */
    private void delete(Logmodel log) {
        try {
            manager.getTransaction().begin();
            manager.remove(log);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    private Logmodel logQuery() {
        Query query = manager.createNamedQuery("Logmodel.findById");
        query.setParameter("id", logId);
        Logmodel log = (Logmodel) query.getSingleResult();

        return log;
    }

    /**
     * 
     * @return 
     */
    private Usermodel userQuery(){
        Logmodel log = logQuery();
        
        Query query = manager.createNamedQuery("Usermodel.findById");
        query.setParameter("id", log.getUserId());
        Usermodel user = (Usermodel) query.getSingleResult();
        
        return user;
    }
    
    /**
     * 
     * @param event
     * @param user
     * @throws IOException 
     */
    private void refreshProfileView(ActionEvent event, Usermodel user) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileView.fxml"));

        Parent profileView = loader.load();

        Scene newScene = new Scene(profileView);
        ProfileViewController profileControllerView = loader.getController();
        profileControllerView.initData(user);

        Stage stage = new Stage();
        stage.setScene(newScene);
               
        stage.show(); 

        Stage logView = (Stage) ((Node) event.getSource()).getScene().getWindow();
        logView.close();
    }
    
    /**
     * 
     * @param scene 
     */
    public void setPreviousScene(Scene scene) {
        previousScene = scene;
    }
}
