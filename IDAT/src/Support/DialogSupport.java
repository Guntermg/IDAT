/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

/**
 *
 * @author Gunter M. Gaede
 */
public class DialogSupport {
    public void showDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/diff/diff-view.fxml"));
        
        // Gotta make this thing work   
        
        /*
        try {
            loader.setController(diffController);
            DialogPane root = loader.load();
            root.getStylesheets().add(getClass().getResource("/gui/diff/diff-view.css").toExternalForm());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Diff Viewer");
            alert.setResizable(true);
            alert.setDialogPane(root);
            alert.initModality(Modality.WINDOW_MODAL);

            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(event -> window.hide());

            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    */
    }
}
