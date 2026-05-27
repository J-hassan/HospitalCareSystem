package com.hospital.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import java.net.URL;
import javafx.util.Duration;

public class SceneManager {

    private static Stage primaryStage;
    private static FXMLLoader lastLoader;

    public static void setStage(Stage primStage) {
        primaryStage = primStage;
    }

    public static void switchScene(String fxmlName, String title) {
        try {
            URL url = SceneManager.class.getResource("/com/hospital/views/" + fxmlName + ".fxml");
            lastLoader = new FXMLLoader(url);
            Parent root = lastLoader.load();

            applyFadeIn(root);

            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle(title);
        } catch (Exception e) {
            System.out.println("Error switching scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void applyFadeIn(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

}
