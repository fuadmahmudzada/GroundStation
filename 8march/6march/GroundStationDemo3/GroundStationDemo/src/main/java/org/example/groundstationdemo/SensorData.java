// SensorData.java
package org.example.groundstationdemo;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

public class SensorData {
    private final LongProperty time;
    private final DoubleProperty sensor1;
    private final DoubleProperty sensor2;
    private final DoubleProperty sensor3;
    private final DoubleProperty sensor4;
    private final DoubleProperty latitude;
    private final DoubleProperty longitude;

    public SensorData(long time, double sensor1, double sensor2, double sensor3, double sensor4, double latitude, double longitude) {
        this.time = new SimpleLongProperty(time);
        this.sensor1 = new SimpleDoubleProperty(sensor1);
        this.sensor2 = new SimpleDoubleProperty(sensor2);
        this.sensor3 = new SimpleDoubleProperty(sensor3);
        this.sensor4 = new SimpleDoubleProperty(sensor4);
        this.latitude = new SimpleDoubleProperty(latitude);
        this.longitude = new SimpleDoubleProperty(longitude);
    }

    public long getTime() {
        return time.get();
    }

    public LongProperty timeProperty() {
        return time;
    }

    public double getSensor1() {
        return sensor1.get();
    }

    public DoubleProperty sensor1Property() {
        return sensor1;
    }

    public double getSensor2() {
        return sensor2.get();
    }

    public DoubleProperty sensor2Property() {
        return sensor2;
    }

    public double getSensor3() {
        return sensor3.get();
    }

    public DoubleProperty sensor3Property() {
        return sensor3;
    }

    public double getSensor4() {
        return sensor4.get();
    }

    public DoubleProperty sensor4Property() {
        return sensor4;
    }

    public double getLatitude() {
        return latitude.get();
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public double getLongitude() {
        return longitude.get();
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }
}