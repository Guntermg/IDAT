/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareInterfaces;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gunter M. Gaede
 */
public class DualdialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private Text TitleText;
    
    @FXML private Text InsertTextA;

    @FXML private Text InsertTextB;
    
    @FXML private TextField InsertFieldA;
    
    @FXML private TextField InsertFieldB;
    
    private double[] answer;
    private double answerA;
    private double answerB;

    public double getAnswerA() {
        return answerA;
    }

    public double getAnswerB() {
        return answerB;
    }

    public double[] getAnswer() {
        return answer;
    }

    public void init(String algorithm, String paramA, String paramB) {
        TitleText.setText("Enter " + algorithm + " parameters:");
        InsertTextA.setText(paramA + ":");
        InsertTextB.setText(paramB + ":");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    /*
    public void init(String technique, String paramA, String paramB) {
        this.setParameterA(paramA);
        this.setParameterB(paramB);
        this.setTitle(technique);
    }
    
    public void setTitle(String param){
        TitleText.setText(TitleText.getText() + " " + param);
    }
    
    public void setParameterA(String param){
        InsertTextA.setText(param);
    }
    
    public void setParameterB(String param){
        InsertTextB.setText(param);
    }*/
    
    @FXML public void confirmAction(ActionEvent event) {
        
        answerA = Float.parseFloat(InsertFieldA.getText());
        answerB = Float.parseFloat(InsertFieldB.getText());
        
        closeStage(event);
    }
    
    @FXML public void cancelAction(ActionEvent event) {
        answerA = -1;
        answerB = -1;
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
