/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * FXML Controller class
 *
 * @author Gunter M. Gaede
 */
public class ProgressScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private ProgressBar progressBar;
    
    @FXML private Label percentLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setProgress(double percent) {
        progressBar.setProgress(percent);
        percentLabel.setText(Double.toString(percent));
    }
    
    
    
}
