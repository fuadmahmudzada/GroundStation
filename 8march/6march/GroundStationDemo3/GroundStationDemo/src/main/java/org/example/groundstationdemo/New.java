// Main.java
package org.example.groundstationdemo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class New extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());

        Image iconImage = new Image("file:C:\\Users\\agdashcom\\Desktop\\JAv\\GroundStationDemo\\src\\main\\resources\\Image\\logotip.png");
        primaryStage.getIcons().add(iconImage);

        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/LineChartStyle.css")).toExternalForm());
        Controller controller = loader.getController();

        DataGenerator dataGenerator = new DataGenerator(controller);
        new Thread(dataGenerator).start();

        // Create an instance of DataPersistenceHandler and start saving data
        DataPersistenceHandler dataPersistenceHandler = new DataPersistenceHandler(dataGenerator, "output_data.csv");
        dataPersistenceHandler.startSavingData();

        primaryStage.setOnCloseRequest(event -> {
            dataGenerator.stop();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/LineChartStyle.css")).toExternalForm());