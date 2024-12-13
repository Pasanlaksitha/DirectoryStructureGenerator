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

    @FXML
    public void initialize() {
        directoryButton.setOnAction(event -> chooseDirectory());
        generateButton.setOnAction(event -> generateTree());
    }

    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selected = directoryChooser.showDialog(new Stage());
        if (selected != null) {
            rootDirectory = selected.getAbsolutePath();
            selectedDirectory.setText(rootDirectory);
        }
    }

    private void generateTree() {
        if (rootDirectory == null || rootDirectory.isEmpty()) {
            previewArea.setText("Please select a directory!");
            return;
        }
        String outputFile = outputFileField.getText().trim();
        if (outputFile.isEmpty()) outputFile = "output.txt";

        String[] exclusions = excludeField.getText().split(",");
        String tree = DirectoryTreeGenerator.generateTree(rootDirectory, exclusions);
        previewArea.setText(tree);

        try {
            DirectoryTreeGenerator.saveTreeToFile(tree, outputFile);
            previewArea.appendText("\n\nTree saved to: " + outputFile);
        } catch (IOException e) {
            previewArea.appendText("\n\nError saving file: " + e.getMessage());
        }
    }
}
