package com.psgenerator.ui;

import com.psgenerator.core.DirectoryTreeGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

    private String rootDirectory;
    private String outputDirectoryPath;

    @FXML
    public void initialize() {
        directoryButton.setOnAction(event -> chooseDirectory());
        outputDirectory.setOnAction(event -> chooseOutputDirectory());
        generateButton.setOnAction(event -> generateTree());
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
}
