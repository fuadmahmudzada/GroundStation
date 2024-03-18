package org.example.groundstationdemo;

import java.util.Random;

public class Sensor2 extends Sensor {

    public int readValue() {
        // For now, simulate with random values for demonstration
        return new Random().nextInt(100);
    }
}
