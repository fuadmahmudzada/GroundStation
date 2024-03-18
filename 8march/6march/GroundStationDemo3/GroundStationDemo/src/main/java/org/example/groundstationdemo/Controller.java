package org.example.groundstationdemo;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    public WebView mapWebView;
    @FXML
    private LineChart<Number, Number> mySensor1;
    @FXML
    private LineChart<Number, Number> mySensor2;
    @FXML
    private LineChart<Number, Number> mySensor3;
    @FXML
    private LineChart<Number, Number> mySensor4;


    @FXML
    private TableView<SensorData> sensorTable;
    public ObservableList<SensorData> sensorDataList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<SensorData, Number> timeColumn;
    @FXML
    private TableColumn<SensorData, Number> sensor1Column;
    @FXML
    private TableColumn<SensorData, Number> sensor2Column;
    @FXML
    private TableColumn<SensorData, Number> sensor3Column;
    @FXML
    private TableColumn<SensorData, Number> sensor4Column;
    @FXML
    private TableColumn<SensorData, Number> latitudeColumn;
    @FXML
    private TableColumn<SensorData, Number> longitudeColumn;
    private Timeline mapUpdateTimeline;
    private WebEngine webEngine;
    private XYChart.Series<Number, Number> sensor1DataSeries;
    private XYChart.Series<Number, Number> sensor2DataSeries;
    private XYChart.Series<Number, Number> sensor3DataSeries;
    private XYChart.Series<Number, Number> sensor4DataSeries;

    private final RollingWindow<XYChart.Data<Number, Number>> sensor1Window;
    private final RollingWindow<XYChart.Data<Number, Number>> sensor2Window;
    private final RollingWindow<XYChart.Data<Number, Number>> sensor3Window;
    private final RollingWindow<XYChart.Data<Number, Number>> sensor4Window;

    private static final int WINDOW_SIZE = 10; // Size of the rolling window in seconds

    public Controller() {
        // ...
        sensor1Window = new RollingWindow<>(WINDOW_SIZE);
        sensor2Window = new RollingWindow<>(WINDOW_SIZE);
        sensor3Window = new RollingWindow<>(WINDOW_SIZE);
        sensor4Window = new RollingWindow<>(WINDOW_SIZE);
    }

    @FXML
    public void initialize() {
        // Initialize data series
        sensor1DataSeries = new XYChart.Series<>();
        sensor1DataSeries.setName("Sensor 1");
        mySensor1.getData().add(sensor1DataSeries);

        sensor2DataSeries = new XYChart.Series<>();
        sensor2DataSeries.setName("Sensor 2");
        mySensor2.getData().add(sensor2DataSeries);

        sensor3DataSeries = new XYChart.Series<>();
        sensor3DataSeries.setName("Sensor 3");
        mySensor3.getData().add(sensor3DataSeries);

        sensor4DataSeries = new XYChart.Series<>();
        sensor4DataSeries.setName("Sensor 4");
        mySensor4.getData().add(sensor4DataSeries);
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        sensor1Column.setCellValueFactory(cellData -> cellData.getValue().sensor1Property());
        sensor2Column.setCellValueFactory(cellData -> cellData.getValue().sensor2Property());
        sensor3Column.setCellValueFactory(cellData -> cellData.getValue().sensor3Property());
        sensor4Column.setCellValueFactory(cellData -> cellData.getValue().sensor4Property());
        latitudeColumn.setCellValueFactory(cellData -> cellData.getValue().latitudeProperty());
        longitudeColumn.setCellValueFactory(cellData -> cellData.getValue().longitudeProperty());

        sensorTable.setItems(sensorDataList);
        AnimationTimer chartUpdater = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 10_000_000_000L) { // 10 seconds in nanoseconds
                    updateChartBounds();
                    lastUpdate = now;
                }
            }
        };
        chartUpdater.start();
         webEngine = mapWebView.getEngine();

        // Load the map HTML file
        mapWebView.getEngine().load(Objects.requireNonNull(getClass().getResource("/map.html")).toExternalForm());

        // Wait for the map to load
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Assuming you have initial latitude and longitude values
                double initialLatitude = 40.7128; // Replace with your initial latitude value
                double initialLongitude = -74.0060; // Replace with your initial longitude value

                // Update the marker position with the initial coordinates
                updateMarker(initialLatitude, initialLongitude);
            }
        });
    }

    public void updateCharts(long time, double sensor1, double sensor2, double sensor3, double sensor4, double latitude, double longitude) {
        Platform.runLater(() -> {
            sensor1Window.add(new XYChart.Data<>(time, sensor1));
            sensor2Window.add(new XYChart.Data<>(time, sensor2));
            sensor3Window.add(new XYChart.Data<>(time, sensor3));
            sensor4Window.add(new XYChart.Data<>(time, sensor4));

            updateChart(mySensor1, sensor1DataSeries, sensor1Window.getWindowData());
            updateChart(mySensor2, sensor2DataSeries, sensor2Window.getWindowData());
            updateChart(mySensor3, sensor3DataSeries, sensor3Window.getWindowData());
            updateChart(mySensor4, sensor4DataSeries, sensor4Window.getWindowData());

            sensorDataList.add(new SensorData(time, sensor1, sensor2, sensor3, sensor4, latitude, longitude));
            selectLastRow();

            updateMarker(latitude, longitude);
        });
    }

    private void updateChart(LineChart<Number, Number> chart, XYChart.Series<Number, Number> series, ObservableList<XYChart.Data<Number, Number>> windowData) {
        series.getData().clear();
        series.getData().addAll(windowData);

        updateAxisBounds(chart, series);
    }
//    private void updateChartBounds() {
//        updateAxisBounds(mySensor1, sensor1DataSeries);
//        updateAxisBounds(mySensor2, sensor2DataSeries);
//        updateAxisBounds(mySensor3, sensor3DataSeries);
//        updateAxisBounds(mySensor4, sensor4DataSeries);
//    }

    private void updateChartBounds() {
      //  long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
        //long tenSecondsAgo = currentTime - 10000; // Calculate the time for 10 seconds ago

        // Update the bounds for each sensor chart
        updateAxisBounds(mySensor1, sensor1DataSeries);
        updateAxisBounds(mySensor2, sensor2DataSeries);
        updateAxisBounds(mySensor3, sensor3DataSeries);
        updateAxisBounds(mySensor4, sensor4DataSeries);
    }
    private void updateAxisBounds(LineChart<Number, Number> chart, XYChart.Series<Number, Number> series) {
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();

        // Assuming 'time' is the timestamp of the latest data point in seconds
        double maxTime = series.getData().stream()
                .mapToDouble(data -> data.getXValue().doubleValue())
                .max().orElse(0.0);

        // Set the lower bound to 10 seconds before the latest timestamp
        double minTime = maxTime - 9;

        // Update the X axis to show only the latest 10 seconds
        xAxis.setAutoRanging(false);
        if(minTime>0) {
            xAxis.setLowerBound(minTime);
        }
        xAxis.setUpperBound(maxTime);

        // Set the tick unit to display more numbers on the x-axis
        xAxis.setTickUnit((maxTime - minTime+1) / 10); // Adjust this value as needed
    }

//    private void initMapUpdateTimeline() {
//        mapUpdateTimeline = new Timeline(
//                new KeyFrame(Duration.ZERO, e -> updateMapWithRandomLocation()),
//                new KeyFrame(Duration.seconds(1), e -> updateMapWithRandomLocation())
//        );
//        mapUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
//        mapUpdateTimeline.play();
//    }

//    private void updateMapWithRandomLocation() {
//        double[] coordinates = generateRandomCoordinates();
//        updateMarker(coordinates[0], coordinates[1]);
//    }

    public void updateMarker(double latitude, double longitude) {
        Platform.runLater(() -> {
            String script = String.format("updateMarkerPosition(%f, %f);", latitude, longitude);
            mapWebView.getEngine().executeScript(script);
        });
    }




    public void selectLastRow() {
        // Code to select and scroll
        TableView.TableViewSelectionModel<SensorData> selectionModel = sensorTable.getSelectionModel();
        ObservableList<SensorData> tableData = sensorTable.getItems();
        if (!tableData.isEmpty()) {
            int lastIndex = (int) (tableData.size() - 0.1);
            selectionModel.select(lastIndex);
            sensorTable.scrollTo(selectionModel.getSelectedIndex());
        }
    }


}

//    private void updateAxisBounds(LineChart<Number, Number> chart, XYChart.Series<Number, Number> series) {
//        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
//        //NumberAxis yAxis = (NumberAxis) chart.getYAxis();
//
//        // Assuming 'time' is the timestamp of the latest data point in seconds
//        double maxTime = series.getData().stream()
//                .mapToDouble(data -> data.getXValue().doubleValue())
//                .max().orElse(0.0);
//
//        // Set the lower bound to 10 seconds before the latest timestamp
//        double minTime = maxTime - 9;
//
//        // Update the X axis to show only the latest 10 seconds
//        xAxis.setAutoRanging(false);
//        if (minTime > 0) {
//            xAxis.setLowerBound(minTime);
//        }
//        xAxis.setUpperBound(maxTime);
//
//        // Update Y axis bounds if necessary
////        double minValue = series.getData().stream()
////                .mapToDouble(data -> data.getYValue().doubleValue())
////                .min().orElse(0.0);
////        double maxValue = series.getData().stream()
////                .mapToDouble(data -> data.getYValue().doubleValue())
////                .max().orElse(0.0);
//
////        yAxis.setAutoRanging(false);
////        yAxis.setLowerBound(Math.max(0, minValue - 10)); // Adjust the lower bound for better visibility
////        yAxis.setUpperBound(maxValue + 10); // Adjust the upper bound for better visibility
//    }




//        // Update y-axis bounds using moving averages
//        double movingAverage = series.getData().stream()
//                .mapToDouble(data -> data.getYValue().doubleValue())
//                .average().orElse(0.0);
//        double movingAverageRange = 1.5; // Adjust this value as needed
//        double minValue = movingAverage - movingAverageRange;
//        double maxValue = movingAverage + movingAverageRange;
//
//        yAxis.setAutoRanging(false);
//        yAxis.setLowerBound(minValue);
//        yAxis.setUpperBound(maxValue);
//  }