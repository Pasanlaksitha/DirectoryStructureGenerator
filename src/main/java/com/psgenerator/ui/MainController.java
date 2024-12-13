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
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert;


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

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Add content elements
        Label authorLabel = new Label("Author: Pasan Laksitha");
        Label versionLabel = new Label("Version: 1.0");
        Label descriptionLabel = new Label(
                "This application allows users to generate an ASCII representation \n" +
                        "of a directory structure. It is particularly useful for university students \n" +
                        "or pro ChatGPT users."
        );
        descriptionLabel.setWrapText(true);
        Label repo = new Label("Repository:");
        Hyperlink repoLink = new Hyperlink("https://github.com/Pasanlaksitha/ProjectStructureGenerator.git");
        repoLink.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/Pasanlaksitha/ProjectStructureGenerator.git"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        content.getChildren().addAll(authorLabel, versionLabel, repo, repoLink, descriptionLabel);
        alert.getDialogPane().setContent(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/psgenerator/ui/assets/DSJ.png"))));

        alert.showAndWait();
    }

}
