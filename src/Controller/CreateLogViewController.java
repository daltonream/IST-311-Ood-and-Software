/*
 * Weightloss App
 * Group 5 
 * IST 311
 */
package Controller;

import Model.Logmodel;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class CreateLogViewController implements Initializable {

    @FXML
    private TextField logDateField;

    @FXML
    private TextArea logContentField;

    @FXML
    private TextField nameTextBox;

    @FXML
    private TextField idTextBox;
    
    private boolean editExistingLog;
    
    private Integer originalLogId;

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
     * @param log 
     */
    public void initData(Usermodel user, Logmodel log){
        initData(user);
        
        logDateField.setText(log.getDate());
        logContentField.setText(log.getContent());
        
        editExistingLog = true;
        originalLogId = log.getId();
    }

    /*******************************Button Operations************************************/
    
    /**
     * Load the profileView and close this view
     * 
     * @param event
     */
    @FXML
    void cancelLog(ActionEvent event) {
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
    void saveLog(ActionEvent event) throws IOException {
        Logmodel log = new Logmodel();
        
        //If you're creating a brand new log
        if (editExistingLog == false){
            Integer logId = createLogId();
            Integer userId = Integer.parseInt(idTextBox.getText());
            String logDate = logDateField.getText();
            String logContent = logContentField.getText();


            log.setId(logId);
            log.setUserId(userId);
            log.setDate(logDate);
            log.setContent(logContent);

            save(log);
        } 
        
        //If you're editing an existing log
        else {
            Integer userId = Integer.parseInt(idTextBox.getText());
            String logDate = logDateField.getText();
            String logContent = logContentField.getText();
            
            log.setId(originalLogId);
            log.setUserId(userId);
            log.setDate(logDate);
            log.setContent(logContent);
            
            update(log);
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
     * @param log
     */
    private void save(Logmodel log) {        
        try {
            manager.getTransaction().begin();

            if (log.getId() != null) {

                manager.persist(log);

                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * 
     * @param log 
     */
    private void update(Logmodel log){
        try {
            Logmodel existingLog = manager.find(Logmodel.class, log.getId());

            if (existingLog != null) {
                manager.getTransaction().begin();

                existingLog.setDate(log.getDate());
                existingLog.setContent(log.getContent());

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
    private Integer createLogId() {
        //query to get how many logs are in the DB
        Query countEntriesQuery = manager.createNamedQuery("Logmodel.countEntries");
        //sets logId to the number of entries in the DB
        Integer logId = ((Long) (countEntriesQuery.getSingleResult())).intValue();

        //query to check if a log with id=logId already exists
        Query findByIdQuery = manager.createNamedQuery("Logmodel.findById");
        findByIdQuery.setParameter("id", logId);
        Logmodel doesExist = (Logmodel) findByIdQuery.getSingleResult();

        //if a log with id=logId exists already increment logId and check again
        try {
            while (doesExist != null) {
                logId++;

                findByIdQuery.setParameter("id", logId);
                doesExist = (Logmodel) findByIdQuery.getSingleResult();
            }
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
        }

        //at the end return a unique logId
        return logId;
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
