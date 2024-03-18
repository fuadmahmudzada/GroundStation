package org.example.groundstationdemo;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.groundstationdemo.Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
public class DataGenerator implements Runnable, SerialPortDataListener {
    private final Controller controller;
    private SerialPort serialPort;
    private volatile boolean running = true;

    public DataGenerator(Controller controller) {
        this.controller = controller;
        connectToSerialPort();
    }

    private void connectToSerialPort() {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for(SerialPort port : serialPorts) {
            System.out.println(port);
        }

            try {
                SerialPort port = SerialPort.getCommPort("COM1");
                port.openPort();
                System.out.println(port);
                port.openPort();
                port.setBaudRate(9600); // Set the appropriate baud rate
                port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

                port.addDataListener(this);
                serialPort = port;
                 // Exit the loop if a port is successfully opened
            } catch (Exception e) {
                // Handle the case where the port cannot be opened
            }

    }

    private void parseSensorData(String data) {
        // Remove the surrounding curly braces from the data string
        String cleanedData = data.replaceAll("[{}]", "");

        // Split the cleaned data string by commas to get the array elements
        String[] values = cleanedData.split(",");

        if (values.length == 7) {
            long time = Long.parseLong(values[0]);
            double sensor1 = Double.parseDouble(values[1]);
            double sensor2 = Double.parseDouble(values[2]);
            double sensor3 = Double.parseDouble(values[3]);
            double sensor4 = Double.parseDouble(values[4]);
            double latitude = Double.parseDouble(values[5]);
            double longitude = Double.parseDouble(values[6]);

            controller.updateCharts(time, sensor1, sensor2, sensor3, sensor4, latitude, longitude);
        } else {
            System.err.println("Invalid sensor data format: " + data);
        }
    }

    public ConcurrentHashMap<Integer, double[]> getSensorDataList() {
        ConcurrentHashMap<Integer, double[]> sensorValues = new ConcurrentHashMap<>();
        for (SensorData data : controller.sensorDataList) {
            int time = (int) data.getTime();
            double[] values = {
                    data.getSensor1(),
                    data.getSensor2(),
                    data.getSensor3(),
                    data.getSensor4(),
                    data.getLatitude(),
                    data.getLongitude()
            };
            sensorValues.put(time, values);
        }
        return sensorValues;
    }
    public void saveDataToCSV(String filename) {
        ConcurrentHashMap<Integer, double[]> sensorValues = getSensorDataList();
        // Populate the sensorValues map with the sensor data

        List<String[]> records = new ArrayList<>();
        for (int time : sensorValues.keySet()) {
            double[] values = sensorValues.get(time);
            if (values != null && values.length == 6) {
                String[] record = {
                        String.valueOf(time),
                        String.valueOf(values[0]), // sensor1
                        String.valueOf(values[1]), // sensor2
                        String.valueOf(values[2]), // sensor3
                        String.valueOf(values[3]), // sensor4
                        String.valueOf(values[4]), // latitude
                        String.valueOf(values[5])  // longitude
                };
                records.add(record);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Time", "Sensor1", "Sensor2", "Sensor3", "Sensor4", "Latitude", "Longitude"))) {
            for (String[] record : records) {
                csvPrinter.printRecord(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (running) {
            Thread.onSpinWait();
            // Wait for incoming data
        }
    }

    public void stop() {
        running = false;
        if (serialPort != null) {
            serialPort.removeDataListener();
            serialPort.closePort();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            byte[] newData = event.getReceivedData();
            String data = new String(newData);
            parseSensorData(data);
        }
    }
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }
}