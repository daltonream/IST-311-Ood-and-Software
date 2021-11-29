/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Goalmodel;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author claudia
 */
    public class GoalViewController1 implements Initializable {
    @FXML
    private URL location;
    @FXML
    private TextField goalDateField;

    @FXML
    private TextField nameTextBox;

    @FXML
    private TextField idTextBox;

    @FXML
    private Button cancelGoalButton;

    @FXML
    private Button editGoalButton;

    @FXML
    private Button deleteGoalButton;
    
    @FXML
    private TextArea goalContentField;
        
    EntityManager manager;
   
    private Integer goalID;
    
    Scene previousScene;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();
        
        assert goalDateField != null : "fx:id=\"goalDateField\" was not injected: check your FXML file 'GoalView.fxml'.";
        assert nameTextBox != null : "fx:id=\"nameTextBox\" was not injected: check your FXML file 'GoalView.fxml'.";
        assert idTextBox != null : "fx:id=\"idTextBox\" was not injected: check your FXML file 'GoalView.fxml'.";
        assert cancelGoalButton != null : "fx:id=\"cancelGoalButton\" was not injected: check your FXML file 'GoalView.fxml'.";
        assert editGoalButton != null : "fx:id=\"editGoalButton\" was not injected: check your FXML file 'GoalView.fxml'.";
        assert deleteGoalButton != null : "fx:id=\"deleteGoalButton\" was not injected: check your FXML file 'GoalView.fxml'.";
        assert goalContentField != null : "fx:id=\"goalContentField\" was not injected check your FXML file 'GoalView.fxml'.";
    }

    /**
     * 
     * @param Goal 
     */
    public void initData(Goalmodel goal) {
        Query query = manager.createNamedQuery("Usermodel.findById");
        query.setParameter("id", goal.getUserId());
        Usermodel user = (Usermodel) query.getSingleResult();

        idTextBox.setText("" + user.getId());
        nameTextBox.setText(user.getName());
        goalDateField.setText(goal.getDate());
        goalContentField.setText(goal.getContent());
        
        goalID = goal.getId();
    }
    
    /********************************Button Operations********************************/

    /**
     * 
     * @param event 
     */
    @FXML
    private void deleteGoal(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete this goal?");
        alert.setContentText("Are you sure you want to delete this goal entry?");

        Optional<ButtonType> result = alert.showAndWait();
       
        if (result.get() == ButtonType.OK) {
            Goalmodel goal = goalQuery();
            Usermodel user = userQuery();

            delete(goal);
           
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
    private void cancelGoal(ActionEvent event) {
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
    private void editGoal(ActionEvent event) throws IOException {
        Usermodel user = userQuery();
        Goalmodel goal = goalQuery();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateGoalView.fxml"));
            Parent createGoalView = loader.load();

            Scene createGoalScene = new Scene(createGoalView);

            CreateGoalViewController controller = loader.getController();
            controller.initData(user, goal);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(createGoalScene);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**********************************Helper Methods***********************************/
    
    /**
     * 
     * @param Goal 
     */
    private void delete(Goalmodel goal) {
        try {
            manager.getTransaction().begin();
            manager.remove(goal);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    private Goalmodel goalQuery() {
        Query query = manager.createNamedQuery("Goalmodel.findById");
        query.setParameter("id", goalID);
        Goalmodel goal = (Goalmodel) query.getSingleResult();

        return goal;
    }

    /**
     * 
     * @return 
     */
    private Usermodel userQuery(){
        Goalmodel goal = goalQuery();
        
        Query query = manager.createNamedQuery("Usermodel.findById");
        query.setParameter("id", goal.getUserId());
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

        Stage goalView = (Stage) ((Node) event.getSource()).getScene().getWindow();
        goalView.close();
    }
    
    /**
     * 
     * @param scene 
     */
    public void setPreviousScene(Scene scene) {
        previousScene = scene;
    }
}


