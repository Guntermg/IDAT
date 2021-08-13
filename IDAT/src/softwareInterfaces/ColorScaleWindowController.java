/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareInterfaces;

import Algoritmos.BBOBMOcolor;
import Algoritmos.BHE2PLcolor;
import Algoritmos.BLWHEcolor;
import Algoritmos.BOHEcolor;
import Algoritmos.DCMHEcolor;
import Algoritmos.FHHcolor;
import Algoritmos.ISIHEcolor;
import Algoritmos.LIPColor;
import Algoritmos.LOGcolor;
import Algoritmos.PWBHEPLcolor;
import Algoritmos.SONARcolor;
import Algoritmos.SquareRootcolor;
import Algoritmos.TRADITIONALcolor;
import Support.ColorSupport;
import Support.ProgressScreenController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Gunter M. Gaede
 */
public class ColorScaleWindowController implements Initializable {

    File inputFolder;
    File outputFolder;
    
    @FXML
    private MenuItem returnButton;
    
    @FXML
    private AnchorPane colorPane;
    
    @FXML
    private Label inputFolderPath;
    
    @FXML
    private Label outputFolderPath;
    
    @FXML
    private Label RunStatus;
    
    @FXML
    private Button inputButton;
    
    @FXML
    private Button outputButton;
    
    @FXML private CheckBox SubFolder;
    
    @FXML private CheckBox bbobmoColor;
    
    @FXML private CheckBox FHHColor;
    
    @FXML private CheckBox BOHEColor;
    
    @FXML private CheckBox BOHEColorFixed;
    
    @FXML private CheckBox PWBHEPLColor;
    
    @FXML private CheckBox PWBHEPLColorFixed;
    
    @FXML private CheckBox BLWHEColor;
    
    @FXML private CheckBox BLWHEColorFixed;
    
    @FXML private CheckBox DCMHEColor;
    
    @FXML private CheckBox ISIHEColor;
    
    @FXML private CheckBox BHE2PLColor;
    
    @FXML private CheckBox SONARColor;
    
    @FXML private CheckBox SONARColorFixed;
    
    @FXML private CheckBox TRADITIONALColor;
    
    @FXML private CheckBox SquareRootColor;
    
    @FXML private CheckBox LogarithmColor;
    
    @FXML private CheckBox FHHColorFixed;
    
    @FXML private CheckBox LIPColor;
    
    @FXML private CheckBox LIPColorFixed;
    
    
    @FXML
    private void returnToMainPage(ActionEvent event) throws IOException {
        Parent mainPageParent = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        Scene scene = new Scene(mainPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) colorPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML
    void gotoGrayPage(ActionEvent event) throws IOException {
        Parent grayPageParent = FXMLLoader.load(getClass().getResource("GrayScaleWindow.fxml"));
        Scene scene = new Scene(grayPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) colorPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML void gotoAboutPage(ActionEvent event) throws IOException {
        Parent aboutPageParent = FXMLLoader.load(getClass().getResource("FXMLAboutWindow.fxml"));
        Scene aboutScene = new Scene(aboutPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) colorPane.getScene().getWindow();
        
        window.setScene(aboutScene);
        window.show();
    }
    
    @FXML
    private void selectFolder(ActionEvent event) {
        
        DirectoryChooser directory = new DirectoryChooser();
        directory.setTitle("Open File");
        File path = directory.showDialog(new Stage());
        
        System.out.println(event.getSource());
        if(event.getSource() == inputButton){
            inputFolderPath.setText(path.toString());
            inputFolder = path;
        } else if(event.getSource() == outputButton) {
            outputFolderPath.setText(path.toString());
            outputFolder = path;
        }   
    }
    

    
    
    @FXML
    void runProcess(ActionEvent event) throws IOException {
        
        // Define parameter variables
        float blwheV, blwheR, boheFilter, fhhBeta, sonarAlpha, lipAlfa, lipBeta;
        double pwbheplAlpha, pwbheplBeta;

        // Init parameter variables
        blwheV = 0;
        blwheR = 0;
        boheFilter = 0;
        fhhBeta = 0;
        pwbheplAlpha = 0;
        pwbheplBeta = 0;
        sonarAlpha = 0;
        lipAlfa = 0;
        lipBeta = 0;
        
        // Cria a lista de imagens da pasta de entrada que será percorrida;
        File standardInput = new File(inputFolder.getPath());
        inputFolderPath.setText(standardInput.getPath());

        File[] pathOriginal = standardInput.listFiles();
        String outputFolderName = outputFolder.toString();
        System.out.println("Output Folder: " + outputFolderName);

        // Set RunStatus label as running;
        RunStatus.setText("Run Status: Running");
        
        // Ask for parametric algorithms's parameters
        if (BLWHEColor.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dualdialog.fxml"));
            Parent parent = fxmlLoader.load();
            DualdialogController dialogController = fxmlLoader.getController();
            dialogController.init("BLWHE", "V[0, 1]", "R[0, 1]");

            Scene scene = new Scene(parent, 300, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            blwheV = (float)dialogController.getAnswerA();
            blwheR = (float)dialogController.getAnswerB();
            
            if(blwheV == -1) BLWHEColor.selectedProperty().set(false);

        } if(BOHEColor.isSelected()) {
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("For BOHE function:");
            dialog.setHeaderText("Enter a value for filter size[0, 250]: ");
            dialog.setContentText("Value: ");
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                boheFilter = Float.parseFloat(result.get());
            }
        } if (FHHColor.isSelected()) {
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("For FHH function:");
            dialog.setHeaderText("Enter a value for beta[0.5, 2.0]: ");
            dialog.setContentText("Value: ");
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                fhhBeta = Float.parseFloat(result.get());
            }
        } if (PWBHEPLColor.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dualdialog.fxml"));
            Parent parent = fxmlLoader.load();
            DualdialogController dialogController = fxmlLoader.getController();
            dialogController.init("PWBHEPL", "Alpha(alpha > 1)", "Beta(beta > 1)");

            Scene scene = new Scene(parent, 300, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            pwbheplAlpha = (float)dialogController.getAnswerA();
            pwbheplBeta = (float)dialogController.getAnswerB();
            
            if(pwbheplAlpha == -1) PWBHEPLColor.selectedProperty().set(false);

        } if (SONARColor.isSelected()) {
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("For SONAR function:");
            dialog.setHeaderText("Enter a value for alfa[0, 1]: ");
            dialog.setContentText("Value: ");
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                sonarAlpha = Float.parseFloat(result.get());
            }
        } if(LIPColor.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dualdialog.fxml"));
            Parent parent = fxmlLoader.load();
            DualdialogController dialogController = fxmlLoader.getController();
            //dialogController.init("LIP", "R(ratio > x)", "V(V > y)");
            dialogController.init("LIP", "Alfa (0.2 < alfa < 2)", "Beta (4 < beta < 10)");
            //dialogController.init("LIP", "R", "V");

            Scene scene = new Scene(parent, 300, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            lipAlfa = (float)dialogController.getAnswerA();
            lipBeta = (float)dialogController.getAnswerB();
            
            if(lipAlfa == -1) LIPColor.selectedProperty().set(false);
        }
        
        
        if(BLWHEColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BLWHEcolor algoritmo = new BLWHEcolor();
                finalImg = algoritmo.BLWHEColorRun(atualPrep, blwheR, blwheV);
                
                ColorSupport convertParam = new ColorSupport();
                String RParam = convertParam.convertFloatToString(blwheR);
                String VParam = convertParam.convertFloatToString(blwheV);

                String algoritmoNome = "_BLWHER" + RParam + "V" + VParam;
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // BLWHEColor - END

        if(BLWHEColorFixed.isSelected()){
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();
            float V = 1;
            float R = 1;

            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                // For each fixed param
                
                //for(V = (float) 0.1; V < 1; V=(float) (V+0.4)){
                //for(R = (float) 0.1; R < 1; R = (float) (R + 0.4)) {
                BLWHEcolor algoritmo = new BLWHEcolor();
                for(V = (float) 0.9; V < 1; V=(float) (V+0.4)){
                    for(R = (float) 0.5; R < 0.6; R = (float) (R + 0.4)) {
                        finalImg = algoritmo.BLWHEColorRun(atualPrep, R, V);
                        
                        ColorSupport convertParam = new ColorSupport();
                        String RParam = convertParam.convertFloatToString(R);
                        String VParam = convertParam.convertFloatToString(V);
                        
                        //String algoritmoNome = "_BLWHE_Ratio_" + R + "_V_" + V + "_";
                        String algoritmoNome = "_BLWHER" + RParam + "V" + VParam;
                        //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                        // Generate sub-folder for processed images if necessary;
                        String folderName = "";
                        if(SubFolder.isSelected()) {
                            folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                            File dir = new File(folderName);
                            dir.mkdirs();
                            folderName = "\\" + algoritmoNome.substring(1);
                        }
                        String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                        System.out.println("Nome da Imagem: " + outputName);
                        File outputFile = new File(outputName);
                        ImageIO.write(finalImg, "png", outputFile);
                    }
                }
            }
        } // BLWHECinzaFixed - END
        
        if(BOHEColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BOHEcolor algoritmo = new BOHEcolor();
                finalImg = algoritmo.BOHEColorRun(atualPrep, (int)boheFilter);
                
                ColorSupport convertParam = new ColorSupport();
                String parameter = convertParam.convertFloatToString(boheFilter);

                //String algoritmoNome = "_BOHE_FilterSize_" + boheFilter  + "_";
                String algoritmoNome = "_BOHEF" + parameter;
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // BOHEColor - END

        if(BOHEColorFixed.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BOHEcolor algoritmo = new BOHEcolor();

                //for(int window = 25; window < 101; window += 25) {
                for(int window = 100; window < 101; window += 25) {
                    finalImg = algoritmo.BOHEColorRun(atualPrep, (int)window);
                    
                    ColorSupport convertParam = new ColorSupport();
                    String parameter = convertParam.convertFloatToString(window);

                    //String algoritmoNome = "_BOHE_FilterSize_" + window + "_";
                    String algoritmoNome = "_BOHEF" + parameter;
                    //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                    
                    // Generate sub-folder for processed images if necessary;
                    String folderName = "";
                    if(SubFolder.isSelected()) {
                        folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                        File dir = new File(folderName);
                        dir.mkdirs();
                        folderName = "\\" + algoritmoNome.substring(1);
                    }
                    String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                    System.out.println("Nome da Imagem: " + outputName);
                    File outputFile = new File(outputName);
                    ImageIO.write(finalImg, "png", outputFile);
                }
            }
        } // BOHEColorFixed - END
        
        if(FHHColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                FHHcolor algoritmo = new FHHcolor();
                finalImg = algoritmo.FHHColorRun(atualPrep, fhhBeta);
                
                // TESTE DE FUNÇÃO DE CONVERSÃO FLOAT-STRING
                ColorSupport convertParam = new ColorSupport();
                String parameter = convertParam.convertFloatToString(fhhBeta);

                //String algoritmoNome = "_FHH_Beta_" + fhhBeta + "_";
                String algoritmoNome = "_FHH" + parameter;
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // FHHColor - END
        
        if(FHHColorFixed.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());
                float beta = (float)0.7;
                
                FHHcolor algoritmo = new FHHcolor();
                finalImg = algoritmo.FHHColorRun(atualPrep, beta);
                
                // TESTE DE FUNÇÃO DE CONVERSÃO FLOAT-STRING
                ColorSupport convertParam = new ColorSupport();
                String parameter = convertParam.convertFloatToString(beta);

                //String algoritmoNome = "_FHH_Beta_" + fhhBeta + "_";
                String algoritmoNome = "_FHH" + parameter;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // FHHColorFixed - END
        
        if(PWBHEPLColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                PWBHEPLcolor algoritmo = new PWBHEPLcolor();
                finalImg = algoritmo.PWBHEPLrun(atualPrep, pwbheplAlpha, pwbheplBeta);
                
                ColorSupport convertParam = new ColorSupport();
                String alfaParam = convertParam.convertFloatToString((float)pwbheplAlpha);
                String betaParam = convertParam.convertFloatToString((float)pwbheplBeta);

                //String algoritmoNome = "_PWBHEPL_alfa_" + pwbheplAlpha + "_beta_" + pwbheplBeta + "_";
                String algoritmoNome = "_PWBHEPLa" + alfaParam + "beta" + betaParam;
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // PWBHEPLColor - END

        if(PWBHEPLColorFixed.isSelected()){
            double alpha = 1;
            double beta;

            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                PWBHEPLcolor algoritmo = new PWBHEPLcolor();

                //for(beta = 1; beta < 6; beta++) {
                for(beta = 5; beta < 6; beta++) {
                    finalImg = algoritmo.PWBHEPLrun(atualPrep, alpha, beta);
                    
                    ColorSupport convertParam = new ColorSupport();
                    String alfaParam = convertParam.convertFloatToString((float)alpha);
                    String betaParam = convertParam.convertFloatToString((float)beta);

                    //String algoritmoNome = "_PWBHEPL_alfa_" + alpha + "_beta_" + beta + "_";
                    String algoritmoNome = "_PWBHEPLa" + alfaParam + "beta" + betaParam;
                    //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                    // Generate sub-folder for processed images if necessary;
                    String folderName = "";
                    if(SubFolder.isSelected()) {
                        folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                        File dir = new File(folderName);
                        dir.mkdirs();
                        folderName = "\\" + algoritmoNome.substring(1);
                    }
                    String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                    System.out.println("Nome da Imagem: " + outputName);
                    File outputFile = new File(outputName);
                    ImageIO.write(finalImg, "png", outputFile);
                } 
            }
        } // PWBHEPLColorFixed - END
            
        if(SONARColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                SONARcolor algoritmo = new SONARcolor();
                finalImg = algoritmo.SONARrun(atualPrep, sonarAlpha);
                
                ColorSupport convertParam = new ColorSupport();
                String parameter = convertParam.convertFloatToString(sonarAlpha);

                //String algoritmoNome = "_SONAR_alfa_" + sonarAlpha + "_";
                String algoritmoNome = "_SONARa" + parameter;
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // SONARColor - END

        if(SONARColorFixed.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                SONARcolor algoritmo = new SONARcolor();

                //for(double i = 0.2; i < 0.6; i += 0.1) {
                for(double i = 0.2; i < 0.3; i += 0.1) {
                    finalImg = algoritmo.SONARrun(atualPrep, i);
                    
                    ColorSupport convertParam = new ColorSupport();
                    String parameter = convertParam.convertFloatToString((float)i);

                    //String algoritmoNome = "_SONAR_alfa_" + i + "_";
                    String algoritmoNome = "_SONARa" + parameter;
                    //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                    // Generate sub-folder for processed images if necessary;
                    String folderName = "";
                    if(SubFolder.isSelected()) {
                        folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                        File dir = new File(folderName);
                        dir.mkdirs();
                        folderName = "\\" + algoritmoNome.substring(1);
                    }
                    String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                    System.out.println("Nome da Imagem: " + outputName);
                    File outputFile = new File(outputName);
                    ImageIO.write(finalImg, "png", outputFile);
                }
            }
        } // SONARColorFixed - END
        
        if(bbobmoColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BBOBMOcolor algoritmo = new BBOBMOcolor();
                finalImg = algoritmo.bbobmoColorRun(atualPrep);

                String algoritmoNome = "_BBOBMO";
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\BBOBMO";
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\BBOBMO";
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // bbobmoColor - END
        
        if(BHE2PLColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BHE2PLcolor algoritmo = new BHE2PLcolor();
                finalImg = algoritmo.BHE2PLColorRun(atualPrep);

                String algoritmoNome = "_BHE2PL";
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // BHE2PLColor - END
        
        if(DCMHEColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                DCMHEcolor algoritmo = new DCMHEcolor();
                finalImg = algoritmo.DCMHEColorRun(atualPrep);

                String algoritmoNome = "_DCMHE";
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // DCMHEColor - END
        
        if(ISIHEColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                ISIHEcolor algoritmo = new ISIHEcolor();
                finalImg = algoritmo.ISIHEColorRun(atualPrep);

                String algoritmoNome = "_ISIHE";
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // ISIHEColor - END
        
        if(TRADITIONALColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                TRADITIONALcolor algoritmo = new TRADITIONALcolor();
                finalImg = algoritmo.TraditionalColorRun(atualPrep);

                String algoritmoNome = "_Traditional";
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // TRADITIONALColor - END
        
        if(SquareRootColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                SquareRootcolor algoritmo = new SquareRootcolor();
                //for (int c = 12; c < 16; c++) {
                for (int c = 15; c < 16; c++) {    // Roda somente parametro c = 15;
                    // Parâmetro C = [12, 15]
                    finalImg = algoritmo.SquareRootRun(atualPrep, c);
                    
                    ColorSupport convertParam = new ColorSupport();
                    String parameter = convertParam.convertFloatToString(c);

                    String algoritmoNome = "_SquareRootNormaC" + c;
                    //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                    // Generate sub-folder for processed images if necessary;
                    String folderName = "";
                    if(SubFolder.isSelected()) {
                        folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                        File dir = new File(folderName);
                        dir.mkdirs();
                        folderName = "\\" + algoritmoNome.substring(1);
                    }
                    String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                    System.out.println("Nome da Imagem: " + outputName);
                    File outputFile = new File(outputName);
                    ImageIO.write(finalImg, "png", outputFile);
                }
            }
        } // SquareRootColor - END
        
        if(LogarithmColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                LOGcolor algoritmo = new LOGcolor();
                //for (int c = 10; c < 20; c += 2) {
                for (int c = 16; c < 17; c += 2) {
                    // Parâmetro C = [35, 40]
                    finalImg = algoritmo.LogarithmRun(atualPrep, c);
                    
                    ColorSupport convertParam = new ColorSupport();
                    String parameter = convertParam.convertFloatToString(c);

                    String algoritmoNome = "_LogNormaC" + parameter;
                    //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                    // Generate sub-folder for processed images if necessary;
                    String folderName = "";
                    if(SubFolder.isSelected()) {
                        folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                        File dir = new File(folderName);
                        dir.mkdirs();
                        folderName = "\\" + algoritmoNome.substring(1);
                    }
                    String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                    System.out.println("Nome da Imagem: " + outputName);
                    File outputFile = new File(outputName);
                    ImageIO.write(finalImg, "png", outputFile);
                }

            }
        } // LogarithmCinza - END
        
        if(LIPColor.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                LIPColor algoritmo = new LIPColor();
                finalImg = algoritmo.LIPrun(atualPrep, lipAlfa, lipBeta);
                
                ColorSupport convertParam = new ColorSupport();
                String RParam = convertParam.convertFloatToString(lipAlfa);
                String VParam = convertParam.convertFloatToString(lipBeta);

                //String algoritmoNome = "_BLWHE_Ratio_" + blwheR + "_V_" + blwheV + "_";
                String algoritmoNome = "_LIPa" + RParam + "b" + VParam;
                //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                // Generate sub-folder for processed images if necessary;
                String folderName = "";
                if(SubFolder.isSelected()) {
                    folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                    File dir = new File(folderName);
                    dir.mkdirs();
                    folderName = "\\" + algoritmoNome.substring(1);
                }
                String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";
                
                System.out.println("Nome da Imagem: " + outputName);
                File outputFile = new File(outputName);
                ImageIO.write(finalImg, "png", outputFile);
            }
        } // LIPColor - END
        
        if(LIPColorFixed.isSelected()){
            float lipA, lipB;
            lipA=(float)0.0;
            lipB = (float)0.0;
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                LIPColor algoritmo = new LIPColor();
                
                for(lipA = 1; lipA < 2; lipA = (float)(lipA + 0.5)) {
                    for(lipB = 4; lipB < 8; lipB = (float) (lipB + 2)) {
                        finalImg = algoritmo.LIPrun(atualPrep, lipA, lipB);
                
                        ColorSupport convertParam = new ColorSupport();
                        //String RParam = convertParam.convertFloatToString(lipAlfa);
                        //String VParam = convertParam.convertFloatToString(lipBeta);

                        //String algoritmoNome = "_BLWHE_Ratio_" + blwheR + "_V_" + blwheV + "_";
                        String algoritmoNome = "_LIPa" + lipA + "b" + lipB;
                        //String outputName = outputFolderName + "\\" + atualNome + algoritmoNome + ".png";
                        // Generate sub-folder for processed images if necessary;
                        String folderName = "";
                        if(SubFolder.isSelected()) {
                            folderName = outputFolderName + "\\" + algoritmoNome.substring(1);
                            File dir = new File(folderName);
                            dir.mkdirs();
                            folderName = "\\" + algoritmoNome.substring(1);
                        }
                        String outputName = outputFolderName + folderName + "\\" + atualNome + algoritmoNome + ".png";

                        System.out.println("Nome da Imagem: " + outputName);
                        File outputFile = new File(outputName);
                        ImageIO.write(finalImg, "png", outputFile);
                    }
                    
                }
            }
        } // LIPColor - END
        
        // Set RunStatus label as done;
        RunStatus.setText("Run Status: Waiting");
        System.out.println("Running Process Ended;");
        
        
    } // Fim de runProcess();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
