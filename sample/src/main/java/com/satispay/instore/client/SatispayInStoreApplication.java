package com.satispay.instore.client;

import com.satispay.protocore.active.SdkDeviceInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SatispayInStoreApplication extends Application {

    // ==> this object is used to pass some implementor info
    public final static SdkDeviceInfo SDK_DEVICE_INFO = new SdkDeviceInfo(
            "device",
            "My OS name",
            "1.0",
            "My company",
            "Demo app - https://github.com/satispay/in-store-api-java-sdk",
            "0.1",
            "1"
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getClassLoader().getResource("layouts/home_page.fxml");
        if (resource != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            fxmlLoader.setResources(ResourceBundle.getBundle("bundles.strings", Locale.getDefault()));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setMinWidth(640);
            stage.setMinHeight(400);
            stage.setScene(new Scene(root, 640, 400));
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}