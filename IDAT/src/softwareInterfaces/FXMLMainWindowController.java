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
public class FXMLMainWindowController implements Initializable {
    
    @FXML private BorderPane mainPane;

    @FXML void gotoColorPage(ActionEvent event) throws IOException {
        Parent colorPageParent = FXMLLoader.load(getClass().getResource("ColorScaleWindow.fxml"));
        Scene scene = new Scene(colorPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML void gotoGrayPage(ActionEvent event) throws IOException {
        Parent grayPageParent = FXMLLoader.load(getClass().getResource("GrayScaleWindow.fxml"));
        Scene scene = new Scene(grayPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML void gotoAboutPage(ActionEvent event) throws IOException {
        //BorderPane aboutPane = FXMLLoader.load(getClass().getResource("FXMLAboutWindow.fxml"));
        //mainPane.getChildren().setAll(aboutPane);
        Parent aboutPageParent = FXMLLoader.load(getClass().getResource("FXMLAboutWindow.fxml"));
        Scene aboutScene = new Scene(aboutPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(aboutScene);
        window.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
