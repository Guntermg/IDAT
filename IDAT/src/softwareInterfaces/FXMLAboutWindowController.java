/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareInterfaces;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gunter M. Gaede
 */
public class FXMLAboutWindowController implements Initializable {

    @FXML private BorderPane aboutPane;
        
    
    @FXML void gotoColorPage(ActionEvent event) throws IOException {
        Parent colorPageParent = FXMLLoader.load(getClass().getResource("ColorScaleWindow.fxml"));
        Scene scene = new Scene(colorPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) aboutPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML void gotoGrayPage(ActionEvent event) throws IOException {
        Parent grayPageParent = FXMLLoader.load(getClass().getResource("GrayScaleWindow.fxml"));
        Scene scene = new Scene(grayPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) aboutPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML private void returnToMainPage(ActionEvent event) throws IOException {
        Parent mainPageParent = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        Scene scene = new Scene(mainPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) aboutPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
}
