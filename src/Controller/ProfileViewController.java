/*
 * Weightloss App
 * Group 5 
 * IST 311
 */
package Controller;

import Model.Goalmodel;
import Model.Logmodel;
import Model.Usermodel;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class ProfileViewController implements Initializable {
    @FXML
    private Button viewLogButton;

    @FXML
    private Button createLogButton;

    @FXML
    private TextField idBox;

    @FXML
    private TextField nameBox;

    @FXML
    private TextField heightBox;

    @FXML
    private TextField weightBox;

    @FXML
    private Text goalsBox;

    @FXML
    private TextField ageBox;

    @FXML
    private TextField bmiBox;

    @FXML
    private TextField bmiStatus;

    @FXML
    private TableView<Logmodel> logsTable;

    @FXML
    private TableColumn<Logmodel, String> logDate;

    @FXML
    private TableColumn<Logmodel, String> logPreview;

    @FXML
    private Button updateInfo;

    @FXML
    private Button logOut;

    @FXML
    private Button quit;
        
    private ObservableList<Logmodel> logData;
    
    private ObservableList<Goalmodel> goalData;
    
    @FXML
    private Button viewGoalButton;

    @FXML
    private Button createGoalButton;

    @FXML
    private TableView<Goalmodel> goalsTable;

    @FXML
    private TableColumn<Goalmodel, String> goalDate;

    @FXML
    private TableColumn<Goalmodel, String> goalDescription;
    
    EntityManager manager;

    Scene previousScene;

   /**
    * 
    * @param url
    * @param rb 
    */
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();

        //Set the cell values for the 'Collection of Logs' Table
        logDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        logPreview.setCellValueFactory(new PropertyValueFactory<>("Content"));
        logsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);   
        
        //Set the cell values for the 'Collection of Goals' Table
        goalDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        goalDescription.setCellValueFactory(new PropertyValueFactory<>("Content"));
        goalsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                
        assert nameBox != null : "fx:id=\"nameBox\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert heightBox != null : "fx:id=\"heightBox\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert weightBox != null : "fx:id=\"weightBox\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert ageBox != null : "fx:id=\"ageBox\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert bmiBox != null : "fx:id=\"bmiBox\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert bmiStatus != null : "fx:id=\"bmiStatus\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert logsTable != null : "fx:id=\"logsTable\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert logDate != null : "fx:id=\"logDate\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert logPreview != null : "fx:id=\"logPreview\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert viewLogButton != null : "fx:id=\"viewLogButton\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert createLogButton != null : "fx:id=\"createLogButton\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert updateInfo != null : "fx:id=\"updateInfo\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert idBox != null : "fx:id=\"idBox\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert logOut != null : "fx:id=\"logOut\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert quit != null : "fx:id=\"quit\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert viewGoalButton != null : "fx:id=\"viewGoalButton\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert createGoalButton != null : "fx:id=\"createGoalButton\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert goalsTable != null : "fx:id=\"goalsTable\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert goalDate != null : "fx:id=\"goalDate\" was not injected: check your FXML file 'ProfileView.fxml'.";
        assert goalDescription != null : "fx:id=\"goalDescription\" was not injected: check your FXML file 'ProfileView.fxml'.";

    }
    
    /**
     * 
     * @param user 
     */ 
    public void initData(Usermodel user) { 
        idBox.setText("" + user.getId());
        nameBox.setText(user.getName());
        heightBox.setText(user.getHeight());
        weightBox.setText("" + user.getWeight());
        ageBox.setText("" + user.getAge());

        calculateBMI(user.getHeight(), user.getWeight());
        
        refreshTable();
    }

    /********************************Button Operations********************************/
    
    /**
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void updateInfo(ActionEvent event) throws IOException { 
        Usermodel user = new Usermodel();

        String id = idBox.getText();
        int intUserID = Integer.parseInt(id);

        String name = nameBox.getText();

        String height = heightBox.getText();

        String weight = weightBox.getText();
        Double userWeight = Double.parseDouble(weight);

        String age = ageBox.getText();
        int userAge = Integer.parseInt(age);

        user.setId(intUserID);
        user.setName(name);
        user.setHeight(height);
        user.setWeight(userWeight);
        user.setAge(userAge);

        //------------------Close the profile page and update account------------------  
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateAccountView.fxml"));

        Parent profileView = loader.load();

        Scene newScene = new Scene(profileView);

        UpdateAccountViewController updateAccount = loader.getController();

        updateAccount.initData(user);

        Stage stage = new Stage();
        stage.setScene(newScene);

        Stage closeAccountInfo = (Stage) ((Node) event.getSource()).getScene().getWindow();
        closeAccountInfo.close();

        stage.show();
    }

    /**
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LogInView.fxml"));

        Parent logIn = loader.load();

        Scene logInScene = new Scene(logIn);

        Stage profileView = (Stage) ((Node) event.getSource()).getScene().getWindow();
        profileView.close();

        Stage stage = new Stage();
        stage.setScene(logInScene);
        stage.show();
    }

    /**
     * 
     * @param event 
     */
    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Opens CreateLogView
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void createLog(ActionEvent event) throws IOException {
        Usermodel user = userQuery();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateLogView.fxml"));
            Parent createLogView = loader.load();

            Scene createLogScene = new Scene(createLogView);

            CreateLogViewController controller = loader.getController();
            controller.initData(user);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(createLogScene);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Opens LogView
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void viewLog(ActionEvent event) throws IOException {
        Logmodel selectedLog = logsTable.getSelectionModel().getSelectedItem();

        if (selectedLog != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LogView.fxml"));
            Parent detailedLogView = loader.load();

            Scene logViewScene = new Scene(detailedLogView);

            LogViewController controller = loader.getController();
            controller.initData(selectedLog);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(logViewScene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog Box");
            alert.setHeaderText("No log selected.");
            alert.setContentText("Please select a log you would like to view.");
            alert.showAndWait();
        }       
    }
    
    @FXML
    void createGoal(ActionEvent event) {
            Usermodel user = userQuery();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateGoalView.fxml"));
            Parent createGoalView = loader.load();

            Scene createGoalScene = new Scene(createGoalView);

            CreateGoalViewController controller = loader.getController();
            controller.initData(user);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(createGoalScene);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @FXML
    void viewGoal(ActionEvent event) throws IOException {
        Goalmodel selectedGoal = goalsTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedGoal);

        if (selectedGoal != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/GoalView.fxml"));
            Parent goalView = loader.load();

            Scene goalViewScene = new Scene(goalView);

            GoalViewController1 controller = loader.getController();
            controller.initData(selectedGoal);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(goalViewScene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog Box");
            alert.setHeaderText("No Goal selected.");
            alert.setContentText("Please select a goal you would like to view.");
            alert.showAndWait();
        }  
    }
    
    

    /**********************************Helper Methods***********************************/

    /**
     * 
     * @param height
     * @param weight 
     */
     public void calculateBMI(String height, Double weight) {
        int userFeet;
        int userInches;

        if (heightBox.getText().length() == 4) {

            String feet = heightBox.getText().substring(0, 1);
            String inches = heightBox.getText().substring(2, 3);

            userFeet = Integer.parseInt(feet);
            userInches = Integer.parseInt(inches);

            int heightInInches = (userFeet * 12) + userInches;
            Double bmi = (weight * 703.0) / (heightInInches * heightInInches);
            String stringBmi = String.format("%.2f", bmi);

            bmiBox.setText(stringBmi);
            getBMICategory(bmi);

        } else if (heightBox.getText().length() == 5) {

            String feet = heightBox.getText().substring(0, 1);
            String inches = heightBox.getText().substring(2, 4);

            userFeet = Integer.parseInt(feet);
            userInches = Integer.parseInt(inches);

            int heightInInches = (userFeet * 12) + userInches;
            Double bmi = (weight * 703) / (heightInInches * heightInInches);
            String stringBmi = String.format("%.2f", bmi);

            bmiBox.setText(stringBmi);
            getBMICategory(bmi);
        }
    }

     /**
      * 
      * @param bmi 
      */
    public void getBMICategory(Double bmi) {

        if (bmi <= 18.5) {
            bmiStatus.setText("Underweight");
        } else if (bmi < 25) {
            bmiStatus.setText("Normal Weight");
        } else if (bmi < 30) {
            bmiStatus.setText("Overweight");
        } else {
            bmiStatus.setText("Obese");
        }
    }    

    /**
     * 
     */
    public void refreshTable() {
        Integer userId = Integer.parseInt(idBox.getText());
        List<Logmodel> logs = readLogByUserId(userId);
        setTableData(logs);
        
        Integer userGoalId = Integer.parseInt(idBox.getText());
        List<Goalmodel> goals = readGoalByUserId(userGoalId);
        setGoalTableData(goals);
    }
    
    /**
     * 
     * @param logList 
     */
    private void setTableData(List<Logmodel> logList) {
        logData = FXCollections.observableArrayList();

        logList.forEach(s -> {
            logData.add(s);
        });

        logsTable.setItems(logData);
        logsTable.refresh();
    }
    private void setGoalTableData(List<Goalmodel> goalList) {
        goalData = FXCollections.observableArrayList();

        goalList.forEach(s -> {
            goalData.add(s);
        });

        goalsTable.setItems(goalData);
        goalsTable.refresh();
    }

    /**
     * 
     * @param id
     * @return 
     */
    private List<Logmodel> readLogByUserId(Integer id){
        Query query = manager.createNamedQuery("Goals.findByUserid");
        query.setParameter("userid", id);
        List<Logmodel> logs = query.getResultList();

        return logs;
    }
    
    private List<Goalmodel> readGoalByUserId(Integer id){
        Query query = manager.createNamedQuery("Goalmodel.findByUserid");
        query.setParameter("userID", id);
        List<Goalmodel> goals = query.getResultList();

        return goals;
    }
    
    /**
     *
     * @return
     */
    private Usermodel userQuery() {
        Query query = manager.createNamedQuery("Usermodel.findById");
        query.setParameter("id", Integer.parseInt(idBox.getText()));
        Usermodel user = (Usermodel) query.getSingleResult();

        return user;
    }

    /**
     * 
     * @param scene 
     */
    public void setPreviousScene(Scene scene) {
        previousScene = scene;
    }
    
}
