package com.psgenerator.ui;

import com.psgenerator.core.DirectoryTreeGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private Button directoryButton;

    @FXML
    private Button outputDirectory;

    @FXML
    private Label selectedDirectory;

    @FXML
    private TextField outputFileField;

    @FXML
    private TextField excludeField;

    @FXML
    private TextArea previewArea;

    @FXML
    private Button generateButton;

    @FXML
    private MenuItem aboutMenuItem;

    private String rootDirectory;
    private String outputDirectoryPath;

    @FXML
    public void initialize() {
        directoryButton.setOnAction(event -> chooseDirectory());
        outputDirectory.setOnAction(event -> chooseOutputDirectory());
        generateButton.setOnAction(event -> generateTree());
        aboutMenuItem.setOnAction(event -> showAboutDialog());
    }

    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Root Directory");
        File selected = directoryChooser.showDialog(new Stage());
        if (selected != null) {
            rootDirectory = selected.getAbsolutePath();
            selectedDirectory.setText("Root: " + rootDirectory);
        }
    }

    private void chooseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Directory");
        File selected = directoryChooser.showDialog(new Stage());
        if (selected != null) {
            outputDirectoryPath = selected.getAbsolutePath();
            outputFileField.setPromptText(outputDirectoryPath + "/output.txt");
        }
    }

    private void generateTree() {
        if (rootDirectory == null || rootDirectory.isEmpty()) {
            previewArea.setText("Please select a root directory!");
            return;
        }

        if (outputDirectoryPath == null || outputDirectoryPath.isEmpty()) {
            previewArea.setText("Please select an output directory!");
            return;
        }

        String outputFileName = outputFileField.getText().trim();
        if (outputFileName.isEmpty()) outputFileName = "output.txt";
        String outputFilePath = outputDirectoryPath + File.separator + outputFileName;

        String[] exclusions = excludeField.getText().split(",");
        String tree = DirectoryTreeGenerator.generateTree(rootDirectory, exclusions);
        previewArea.setText(tree);

        try {
            DirectoryTreeGenerator.saveTreeToFile(tree, outputFilePath);
            previewArea.appendText("\n\nTree saved to: " + outputFilePath);
        } catch (IOException e) {
            previewArea.appendText("\n\nError saving file: " + e.getMessage());
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Directory Structure Generator");
        alert.setContentText(
                "Author: Pasan Laksitha\n\n" +
                "Version: 1.0\n\n" +
                "Repository: https://github.com/Pasanlaksitha/ProjectStructureGenerator.git\n\n" +
                "This application allows users to generate an ASCII representation " +
                "of a directory structure. you will need this if you  are a University student or pro ChatGPT user"
        );
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/psgenerator/ui/assets/DSJ.png"))));
        alert.showAndWait();
    }
}
