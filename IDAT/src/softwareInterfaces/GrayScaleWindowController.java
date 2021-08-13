/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareInterfaces;

import Algoritmos.BBOBMOcinza;
import Algoritmos.BHE2PLcinza;
import Algoritmos.BLWHEcinza;
import Algoritmos.BOHEcinza;
import Algoritmos.DCMHEcinza;
import Algoritmos.FHHcinza;
import Algoritmos.ISIHEcinza;
import Algoritmos.LOGcinza;
import Algoritmos.PWBHEPLcinza;
import Algoritmos.SONARcinza;
import Algoritmos.SquareRootcinza;
import Algoritmos.TRADITIONALcinza;
import Algoritmos.LIPcinza;
import Support.ColorSupport;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class GrayScaleWindowController {
    
    File inputFolder;
    File outputFolder;
    
    @FXML private MenuItem returnButton;
    
    @FXML private AnchorPane grayPane;
    
    @FXML private Label inputFolderPath;
    
    @FXML private Label outputFolderPath;
    
    @FXML private DualdialogController dualDialog;
    
    @FXML private Label RunStatus;
    
    @FXML private Button inputButton;
    
    @FXML private Button outputButton;
    
    // Algorithm Buttons
    @FXML private CheckBox SubFolder;
    
    @FXML private CheckBox bbobmo;
    
    @FXML private CheckBox FHHCinza;
    
    @FXML private CheckBox BOHECinza;
    
    @FXML private CheckBox BOHECinzaFixed;
    
    @FXML private CheckBox PWBHEPLCinza;
    
    @FXML private CheckBox PWBHEPLCinzaFixed;
    
    @FXML private CheckBox BLWHECinza;
    
    @FXML private CheckBox BLWHECinzaFixed;
    
    @FXML private CheckBox DCMHECinza;
    
    @FXML private CheckBox ISIHECinza;
    
    @FXML private CheckBox BHE2PLCinza;
    
    @FXML private CheckBox SONARCinza;
    
    @FXML private CheckBox SONARCinzaFixed;
    
    @FXML private CheckBox TRADITIONALCinza;
    
    @FXML private CheckBox SquareRootCinza;
    
    @FXML private CheckBox LogarithmCinza;
    
    @FXML private CheckBox LIPCinza;
    
    @FXML private CheckBox LIPCinzaFixed;
    
    
    @FXML
    private void returnToMainPage(ActionEvent event) throws IOException {
        Parent mainPageParent = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        Scene scene = new Scene(mainPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) grayPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML
    void gotoColorPage(ActionEvent event) throws IOException {
        Parent colorPageParent = FXMLLoader.load(getClass().getResource("ColorScaleWindow.fxml"));
        Scene scene = new Scene(colorPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) grayPane.getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    @FXML void gotoAboutPage(ActionEvent event) throws IOException {
        Parent aboutPageParent = FXMLLoader.load(getClass().getResource("FXMLAboutWindow.fxml"));
        Scene aboutScene = new Scene(aboutPageParent);
        
        //This line gets the Stage information
        Stage window = (Stage) grayPane.getScene().getWindow();
        
        window.setScene(aboutScene);
        window.show();
    }
    
    @FXML
    private void selectFolder(ActionEvent event) {
        
        DirectoryChooser directory = new DirectoryChooser();
        directory.setTitle("Open File");
        File path = directory.showDialog(new Stage());
        
        
        if(event.getSource() == inputButton){
            inputFolderPath.setText(path.toString());
            inputFolder = path;
        } else if(event.getSource() == outputButton) {
            outputFolderPath.setText(path.toString());
            outputFolder = path;
        }   
        System.out.println(path.toString());
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
        //File[] pathOriginal = inputFolder.listFiles();
        String outputFolderName = outputFolder.toString();
        System.out.println("Output Folder: " + outputFolderName);

        // Set RunStatus label as running;
        RunStatus.setText("Run Status: Running");

        // Ask for parametric algorithms's parameters
        if (BLWHECinza.isSelected()) {
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
            
            if(blwheV == -1) BLWHECinza.selectedProperty().set(false);

        } if(BOHECinza.isSelected()) {
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("For BOHE function:");
            dialog.setHeaderText("Enter a value for filter size[0, 250]: ");
            dialog.setContentText("Value: ");
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                boheFilter = Float.parseFloat(result.get());
            }
        } if (FHHCinza.isSelected()) {
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("For FHH function:");
            dialog.setHeaderText("Enter a value for beta[0.5, 2.0]: ");
            dialog.setContentText("Value: ");
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                fhhBeta = Float.parseFloat(result.get());
            }
        } if (PWBHEPLCinza.isSelected()) {
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
            
            if(pwbheplAlpha == -1) PWBHEPLCinza.selectedProperty().set(false);

        } if (SONARCinza.isSelected()) {
            // get parameter values:
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("For SONAR function:");
            dialog.setHeaderText("Enter a value for alfa[0, 1]: ");
            dialog.setContentText("Value: ");
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                sonarAlpha = Float.parseFloat(result.get());
            }
        } if(LIPCinza.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dualdialog.fxml"));
            Parent parent = fxmlLoader.load();
            DualdialogController dialogController = fxmlLoader.getController();
            //dialogController.init("LIP", "R(ratio > x)", "V(V > y)");
            dialogController.init("LIP", "R(0.2 < R < 2)", "V(4 < V < 10)");
            //dialogController.init("LIP", "R", "V");

            Scene scene = new Scene(parent, 300, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            lipAlfa = (float)dialogController.getAnswerA();
            lipBeta = (float)dialogController.getAnswerB();
            
            if(lipAlfa == -1) LIPCinza.selectedProperty().set(false);
        }

        // Run Progress Bar Thread
        //new Thread(progressUpdate).start();



        if(bbobmo.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BBOBMOcinza algoritmo = new BBOBMOcinza();
                finalImg = algoritmo.bbobmoRun(atualPrep);

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
        } // bbobmo - END

        if(FHHCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                FHHcinza algoritmo = new FHHcinza();
                finalImg = algoritmo.FHH(atualPrep, fhhBeta);
                
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
        } // FHHCinza - END

        if(BOHECinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BOHEcinza algoritmo = new BOHEcinza();
                finalImg = algoritmo.BOHErun(atualPrep, (int)boheFilter);
                
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
        } // BOHECinza - END

        if(BOHECinzaFixed.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BOHEcinza algoritmo = new BOHEcinza();

                //for(int window = 25; window < 101; window += 25) {
                for(int window = 100; window < 101; window += 25) {
                    finalImg = algoritmo.BOHErun(atualPrep, (int)window);
                    
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
        } // BOHECinzaFixed - END

        if(PWBHEPLCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                PWBHEPLcinza algoritmo = new PWBHEPLcinza();
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
        } // PWBHEPLCinza - END

        if(PWBHEPLCinzaFixed.isSelected()){
            double alpha = 1;
            double beta;

            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                PWBHEPLcinza algoritmo = new PWBHEPLcinza();

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
        } // PWBHEPLCinzaFixed - END

        if(BLWHECinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BLWHEcinza algoritmo = new BLWHEcinza();
                finalImg = algoritmo.BLWHErun(atualPrep, blwheR, blwheV);
                
                ColorSupport convertParam = new ColorSupport();
                String RParam = convertParam.convertFloatToString(blwheR);
                String VParam = convertParam.convertFloatToString(blwheV);

                //String algoritmoNome = "_BLWHE_Ratio_" + blwheR + "_V_" + blwheV + "_";
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
        } // BLWHECinza - END

        if(BLWHECinzaFixed.isSelected()){
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
                BLWHEcinza algoritmo = new BLWHEcinza();
                for(V = (float) 0.9; V < 1; V=(float) (V+0.4)){
                    for(R = (float) 0.5; R < 0.6; R = (float) (R + 0.4)) {
                        finalImg = algoritmo.BLWHErun(atualPrep, R, V);
                        
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

        if(DCMHECinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                DCMHEcinza algoritmo = new DCMHEcinza();
                finalImg = algoritmo.DCMHErun(atualPrep);

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
        } // DCMHECinza - END

        if(ISIHECinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                ISIHEcinza algoritmo = new ISIHEcinza();
                finalImg = algoritmo.ISIHErun(atualPrep);

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
        } // ISIHECinza - END

        if(BHE2PLCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                BHE2PLcinza algoritmo = new BHE2PLcinza();
                finalImg = algoritmo.BHE2PLrun(atualPrep);

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
        } // BHE2PLCinza - END

        if(SONARCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                SONARcinza algoritmo = new SONARcinza();
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
        } // SONARCinza - END

        if(SONARCinzaFixed.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                SONARcinza algoritmo = new SONARcinza();

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
        } // SONARCinzaFixed - END

        if(TRADITIONALCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                TRADITIONALcinza algoritmo = new TRADITIONALcinza();
                finalImg = algoritmo.TraditionalRun(atualPrep);

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
        } // TRADITIONALCinza - END

        if(SquareRootCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                SquareRootcinza algoritmo = new SquareRootcinza();
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
        } // SquareRootCinza - END

        if(LogarithmCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                LOGcinza algoritmo = new LOGcinza();
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
        
        if(LIPCinza.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                LIPcinza algoritmo = new LIPcinza();
                finalImg = algoritmo.LIPrun(atualPrep, lipAlfa, lipBeta);
                
                ColorSupport convertParam = new ColorSupport();
                String RParam = convertParam.convertFloatToString(lipAlfa);
                String VParam = convertParam.convertFloatToString(lipBeta);

                //String algoritmoNome = "_BLWHE_Ratio_" + blwheR + "_V_" + blwheV + "_";
                String algoritmoNome = "_LIPr" + RParam + "v" + VParam;
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
        } // LIPCinza - END
        
        if(LIPCinzaFixed.isSelected()){
            for(File imgAtual : pathOriginal) {
                String atualNome = imgAtual.getName();
                atualNome = atualNome.substring(0, (atualNome.length() - 4));
                BufferedImage atualPrep = ImageIO.read(imgAtual);
                BufferedImage finalImg = new BufferedImage(atualPrep.getWidth(), atualPrep.getHeight(), atualPrep.getType());

                LIPcinza algoritmo = new LIPcinza();
                float Lipratio = 5;
                float LipV = 1;
                finalImg = algoritmo.LIPrun(atualPrep, LipV, Lipratio);
                
                ColorSupport convertParam = new ColorSupport();
                String RParam = convertParam.convertFloatToString(Lipratio);
                String VParam = convertParam.convertFloatToString(LipV);

                //String algoritmoNome = "_BLWHE_Ratio_" + blwheR + "_V_" + blwheV + "_";
                String algoritmoNome = "_LIPr" + RParam + "v" + VParam;
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
        } // LIPCinzaFixed - END


        // Set RunStatus label as done;
        RunStatus.setText("Run Status: Waiting");
        System.out.println("Running Process Ended;");
    }
    
    
}
