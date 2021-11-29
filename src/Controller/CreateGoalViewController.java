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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author claudia
 */
public class CreateGoalViewController implements Initializable{

    /**
     *
     */


    @FXML
    private TextField goalDateField;

    @FXML
    private TextArea goalContentField;

    @FXML
    private TextField nameTextBox;

    @FXML
    private TextField idTextBox;
    

    @FXML
    private Button cancelGoalButton;

    @FXML
    private Button saveGoalButton;
    
    private boolean editExistingLog;
    
    private Integer originalGoalId;

    EntityManager manager;
    
    Scene previousScene;
        
    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = (EntityManager) Persistence.createEntityManagerFactory("WeightLossAppFXMLPU").createEntityManager();
        
        assert nameTextBox != null : "fx:id=\"nameTextBox\" was not injected: check your FXML file 'CreateGoalView.fxml'.";
        assert idTextBox != null : "fx:id=\"idTextBox\" was not injected: check your FXML file 'CreateGoalView.fxml'.";
        assert goalDateField != null : "fx:id=\"goalDateField\" was not injected: check your FXML file 'CreateGoalView.fxml'.";
        assert goalContentField != null : "fx:id=\"logContentField\" was not injected: check your FXML file 'CreateGoalView.fxml'.";
        assert cancelGoalButton != null : "fx:id=\"cancelGoalButton\" was not injected: check your FXML file 'CreateGoalView.fxml'.";
        assert saveGoalButton != null : "fx:id=\"saveGoalButton\" was not injected: check your FXML file 'CreateGoalView.fxml'.";
        
    }

    /**
     * 
     * @param user 
     */
    public void initData(Usermodel user) {
        nameTextBox.setText("" + user.getName());
        idTextBox.setText("" + user.getId());
        
        editExistingLog = false;
    }
    
    /**
     * 
     * @param user
     * @param goal 
     */
    public void initData(Usermodel user, Goalmodel goal){
        initData(user);
        
        goalDateField.setText(goal.getDate());
        goalContentField.setText(goal.getContent());
        
        editExistingLog = true;
        originalGoalId = goal.getId();
    }

    /*******************************Button Operations************************************/
    
    /**
     * Load the profileView and close this view
     * 
     * @param event
     */
    @FXML
    void cancelGoal(ActionEvent event) {
        try {
            refreshProfileView(event);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This is a CREATE & UPDATE function
     *
     * @param event
     */
    @FXML
    void saveGoal(ActionEvent event) throws IOException {
        Goalmodel goal = new Goalmodel();
        
        //If you're creating a brand new log
        if (editExistingLog == false){
            Integer goalId = createGoalId();
            Integer userId = Integer.parseInt(idTextBox.getText());
            String goalDate = goalDateField.getText();
            String goalContent = goalContentField.getText();


            goal.setId(goalId);
            goal.setUserId(userId);
            goal.setDate(goalDate);
           goal.setContent(goalContent);

            save(goal);
        } 
        
        //If you're editing an existing log
        else {
            Integer userId = Integer.parseInt(idTextBox.getText());
            String goalDate = goalDateField.getText();
            String goalContent = goalContentField.getText();
            
            goal.setId(originalGoalId);
            goal.setUserId(userId);
            goal.setDate(goalDate);
            goal.setContent(goalContent);
            
            update(goal);
        }

        try {
            refreshProfileView(event);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }       
    }

    /**********************************Helper Methods**************************************/
    
    /**
     *
     * @param goal
     */
    private void save(Goalmodel goal) {        
        try {
            manager.getTransaction().begin();

            if (goal.getId() != null) {

                manager.persist(goal);

                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * 
     * @param goal 
     */
    private void update(Goalmodel goal){
        try {
            Goalmodel existingGoal = manager.find(Goalmodel.class, goal.getId());

            if (existingGoal != null) {
                manager.getTransaction().begin();

                existingGoal.setDate(goal.getDate());
                existingGoal.setContent(goal.getContent());

                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * 
     * @return 
     */
    private Usermodel userQuery(){        
        Query query = manager.createNamedQuery("Usermodel.findById");
        query.setParameter("id", Integer.parseInt(idTextBox.getText()));
        Usermodel user = (Usermodel) query.getSingleResult();
        
        return user;
    }

    /**
     * Returns unique logId value based on how many log entries are already in
     * DB. Designed to handle creation of log id's if user deletes log entries
     *
     * Example: If there are 6 entries in the DB and the last entry has an id of
     * 6, this will retrieve log with id=logId (or 6), see that the entry exists
     * and increment logId to 7. It will then try to retrieve log with id=7
     * which doesn't exist so it returns logId=7 as a unique log id
     * 
     * @return 
     */
    private Integer createGoalId() {
        //query to get how many logs are in the DB
        Query countEntriesQuery = manager.createNamedQuery("Goalmodel.countEntries");
        //sets logId to the number of entries in the DB
        Integer goalId = ((Long) (countEntriesQuery.getSingleResult())).intValue();

        //query to check if a log with id=logId already exists
        Query findByIdQuery = manager.createNamedQuery("Goalmodel.findById");
        findByIdQuery.setParameter("id", goalId);
        Goalmodel doesExist = (Goalmodel) findByIdQuery.getSingleResult();

        //if a log with id=logId exists already increment logId and check again
        try {
            while (doesExist != null) {
                goalId++;

                findByIdQuery.setParameter("id", goalId);
                doesExist = (Goalmodel) findByIdQuery.getSingleResult();
            }
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
        }

        //at the end return a unique logId
        return goalId;
    }
    
    /**
     * 
     * @param scene 
     */
    public void setPreviousScene(Scene scene) {
        previousScene = scene;
    }
    
    /**
     * 
     * @param event
     * @throws IOException 
     */
    private void refreshProfileView(ActionEvent event) throws IOException{      
            Usermodel user = userQuery();
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileView.fxml"));
            Parent profileView = loader.load();

            Scene profileScene = new Scene(profileView);

            ProfileViewController controller = loader.getController();
            controller.initData(user);
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreviousScene(currentScene);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(profileScene);
            stage.show();
    }

    
    }



