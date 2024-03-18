package org.example.groundstationdemo;

import java.util.Random;

public class Sensor4 extends Sensor {

    public int readValue(){
        return new Random().nextInt(100);
    }
}
