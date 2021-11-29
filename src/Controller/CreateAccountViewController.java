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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class CreateAccountViewController implements Initializable {

    @FXML
    private TextField idField;

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

    Scene previousScene;
    
    EntityManager manager;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();

        assert idField != null : "fx:id=\"idField\" was not injected: check your FXML file 'DetailedModelView.fxml'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'DetailedModelView.fxml'.";
        assert heightField != null : "fx:id=\"heightField\" was not injected: check your FXML file 'DetailedModelView.fxml'.";
        assert weightField != null : "fx:id=\"weightField\" was not injected: check your FXML file 'DetailedModelView.fxml'.";
        assert ageField != null : "fx:id=\"ageField\" was not injected: check your FXML file 'DetailedModelView.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'DetailedModelView.fxml'.";

        backButton.setDisable(false);
    }
    
    /*******************************Button Operations************************************/
    
    /**
     * 
     * @param event 
     */
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (previousScene != null) {
            stage.setScene(previousScene);
        }
    }
    
    /**
     * Create an account and then render the profile page
     * 
     * @param event 
     */
    @FXML
    void createAccount(ActionEvent event) {
        try {
            String id = idField.getText();
            int intUserID = Integer.parseInt(id);

            String name = nameField.getText();

            String height = heightField.getText();

            String weight = weightField.getText();
            Double userWeight = Double.parseDouble(weight);

            String age = ageField.getText();
            int userAge = Integer.parseInt(age);

            Usermodel user = new Usermodel();

            user.setId(intUserID);
            user.setName(name);
            user.setHeight(height);
            user.setWeight(userWeight);
            user.setAge(userAge);

            create(user, event);
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fields left blank");
            alert.setHeaderText("Please fill in all the fields");
            alert.showAndWait();
        }
    }
    
    /**
     * Go back to log in page
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void cancel(ActionEvent event) throws IOException {
        Stage createAccountView = (Stage) ((Node) event.getSource()).getScene().getWindow();
        createAccountView.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LogInView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        Stage stage = new Stage();
        stage.setScene(tableViewScene);

        stage.show();
    }

    /**********************************Helper Methods**************************************/
    
    /**
     * 
     * @param scene 
     */
    public void setPreviousScene(Scene scene) {
        previousScene = scene;
        backButton.setDisable(false);
    }

    /**
     * 
     * @param user
     * @param event 
     */
    public void create(Usermodel user, ActionEvent event) {
        System.out.println(user.getName());
        try {
            manager.getTransaction().begin();

            if (user.getId() != null) {

                manager.persist(user);

                manager.getTransaction().commit();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileView.fxml"));

                Parent profileView = loader.load();

                Scene newScene = new Scene(profileView);

                ProfileViewController profileControllerView = loader.getController();

                profileControllerView.initData(user);

                Stage stage = new Stage();
                stage.setScene(newScene);

                Stage createAccount = (Stage) ((Node) event.getSource()).getScene().getWindow();
                createAccount.close();

                stage.show();
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User ID already used");
            alert.setHeaderText("The Entered ID is already used");
            alert.setContentText("Please Enter a different ID"); // figure out how to show next avilable ID
            alert.showAndWait();

        }
    }
}
