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
import javax.persistence.Query;

public class LogInViewController implements Initializable {
    @FXML
    private TextField userID;

    @FXML
    private Button logIn;

    @FXML
    private Button createAccount;

    @FXML
    private TextField userName;
    
    EntityManager manager;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();

        assert userID != null : "fx:id=\"userID\" was not injected: check your FXML file 'logInView.fxml'.";
        assert userName != null : "fx:id=\"userName\" was not injected: check your FXML file 'logInView.fxml'.";
        assert logIn != null : "fx:id=\"logIn\" was not injected: check your FXML file 'logInView.fxml'.";
        assert createAccount != null : "fx:id=\"createAccount\" was not injected: check your FXML file 'logInView.fxml'.";
    }
    
    /*******************************Button Operations************************************/
    
    /**
     * Opens a create account page
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void createAccount(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateAccountView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        Stage stage = new Stage();
        stage.setScene(tableViewScene);

        stage.show();

        //--------------------Go back----------------------------
        CreateAccountViewController detailedControlled = loader.getController();

        Scene currentScene = ((Node) event.getSource()).getScene();
        Stage login = (Stage) ((Node) event.getSource()).getScene().getWindow();
        detailedControlled.setPreviousScene(currentScene);

        stage.setScene(tableViewScene);
        stage.show();
        login.close();
    }

    /**
     * Logs in and then shows the profile page
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void logIn(ActionEvent event) throws IOException {
        String userid = userID.getText();
        String username = userName.getText();

        if (userid.isEmpty() || username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Valid Credentials");
            alert.setHeaderText("You did not fill in either the ID or Username");
            alert.setContentText("Please fill in the proper fields");
            alert.showAndWait();
        } else {
            int intUserID = Integer.parseInt(userid);
            readByIDAndName(intUserID, username, event);
        }
    }
    
    /**********************************Helper Methods**************************************/

    /**
     * Pulls all the info from the DB
     * 
     * @param id
     * @param name
     * @param event 
     */
    public void readByIDAndName(int id, String name, ActionEvent event) { 
        Query query = manager.createNamedQuery("Usermodel.readByNameAndId");

        try {
            query.setParameter("id", id);
            query.setParameter("name", name);

            Usermodel user = (Usermodel) query.getSingleResult();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileView.fxml"));

            Parent detailedModelView = loader.load();

            Scene tableViewScene = new Scene(detailedModelView);

            ProfileViewController profileControllerView = loader.getController();

            profileControllerView.initData(user);

            Stage logInView = (Stage) ((Node) event.getSource()).getScene().getWindow();

            logInView.close();

            Stage stage = new Stage();
            stage.setScene(tableViewScene);

            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Valid Credentials");
            alert.setHeaderText("There is no account with that ID and/or Username");
            alert.setContentText("Please correct information or create an account.");
            alert.showAndWait();
        }
    }
}
