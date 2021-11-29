/*
 * Weightloss App
 * Group 5 
 * IST 311
 */
package Controller;

import Model.Usermodel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class UpdateAccountViewController implements Initializable {
    @FXML
    private TextField updateID;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField ageField;

    @FXML
    private Button backButton;

    @FXML
    private Button updateAccount;

    @FXML
    private Button cancel;
    
    EntityManager manager;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();

        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
        assert heightField != null : "fx:id=\"heightField\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
        assert weightField != null : "fx:id=\"weightField\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
        assert ageField != null : "fx:id=\"ageField\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
        assert updateAccount != null : "fx:id=\"createAccount\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'UpdateInfoView.fxml'.";
    }  
    
    /**
     * 
     * @param user 
     */
    public void initData(Usermodel user) {
        updateID.setText("" + user.getId());
        nameField.setText(user.getName());
        heightField.setText(user.getHeight());
        weightField.setText("" + user.getWeight());
        ageField.setText("" + user.getAge());

    }   
    
    /********************************Button Operations********************************/
    
    /**
     * Brings back profileView with all data
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void goBack(ActionEvent event) throws IOException {
        Stage updateAccount = (Stage) ((Node) event.getSource()).getScene().getWindow();
        updateAccount.close();  
        
        orginalState(event); 
    }

    /**
     * Grabs all the info from the update page
     * and reloads the profileView
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void updateAccount(ActionEvent event) throws IOException { 
        Usermodel user = new Usermodel();
        
        String id = updateID.getText();
        int userID = Integer.parseInt(id);
        user.setId(userID);
        
        user.setName(nameField.getText());
        
        user.setHeight(heightField.getText());
        
        String weight = weightField.getText();
        Double userWeight = Double.parseDouble(weight);
        user.setWeight(userWeight);
        
        String age = ageField.getText();
        int userAge = Integer.parseInt(age);
        user.setAge(userAge);
        
    //------------------Refresh and Update Profile------------------  
 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileView.fxml"));

        Parent profileView = loader.load();

        Scene newScene = new Scene(profileView);

        ProfileViewController profileControllerView = loader.getController();

        profileControllerView.initData(user);

        Stage stage = new Stage();
        stage.setScene(newScene);
               
        stage.show(); 

        Stage updateInfo = (Stage) ((Node) event.getSource()).getScene().getWindow();
        updateInfo.close();
        
        update(user);
    }
   
    /**
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void cancel(ActionEvent event) throws IOException {

        Stage updateAccount = (Stage) ((Node) event.getSource()).getScene().getWindow();
        updateAccount.close();

        orginalState(event);

    }
    
    /**********************************Helper Methods***********************************/
    
    /**
     * This is the OG info if the user wishes to cancel or go back
     * 
     * @param event
     * @throws IOException
     */
    private void orginalState(ActionEvent event) throws IOException { 

        String id = updateID.getText();
        int userID = Integer.parseInt(id);

        Query query = manager.createNamedQuery("Usermodel.findById");

        query.setParameter("id", userID);

        Usermodel user = (Usermodel) query.getSingleResult();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        ProfileViewController profileControllerView = loader.getController();

        profileControllerView.initData(user);

        Stage stage = new Stage();
        stage.setScene(tableViewScene);

        stage.show();

    }

    /**
     * 
     * @param model 
     */
    public void update(Usermodel model) {
        /*  
                                                                   this updates all the info and sends it do the db 
                                                                   since the profile page is reloaded it shows the 
                                                                   updated info. 
         */
        try {

            Usermodel existingUser = manager.find(Usermodel.class, model.getId());

            if (existingUser != null) {
                manager.getTransaction().begin();

                // update all atttributes
                existingUser.setName(model.getName());
                existingUser.setHeight(model.getHeight());
                existingUser.setWeight(model.getWeight());
                existingUser.setAge(model.getAge());

                // end transaction
                manager.getTransaction().commit();

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
