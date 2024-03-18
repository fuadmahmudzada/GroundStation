package org.example.groundstationdemo;

import org.apache.commons.csv.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CSVMerger {

    public void mergeFile() {
        try {
            // Create a CSVFormat object with the desired header names and options
            CSVFormat format = CSVFormat.DEFAULT.withHeader("Time", "Sensor1", "Sensor2", "Sensor3", "Sensor4");

            // Create a CSVPrinter object with a Writer and the CSVFormat object
            FileWriter outputWriter = new FileWriter("Output_data.csv");
            CSVPrinter printer = new CSVPrinter(outputWriter, format);
// Create a CSVFormat object for the first input file with the correct header name
            CSVFormat format1 = CSVFormat.DEFAULT.withHeader("Time", "Sensor1").withSkipHeaderRecord();

// Create a CSVParser object for the first input file with a Reader and the CSVFormat object
            FileReader inputReader1 = new FileReader("Sensor1_data.csv");
            CSVParser parser1 = new CSVParser(inputReader1, format1);

// Create a CSVFormat object for the second input file with the correct header name
            CSVFormat format2 = CSVFormat.DEFAULT.withHeader("Time", "Sensor2").withSkipHeaderRecord();

// Create a CSVParser object for the second input file with a Reader and the CSVFormat object
            FileReader inputReader2 = new FileReader("Sensor2_data.csv");
            CSVParser parser2 = new CSVParser(inputReader2, format2);

// Create a CSVFormat object for the third input file with the correct header name
            CSVFormat format3 = CSVFormat.DEFAULT.withHeader("Time", "Sensor3").withSkipHeaderRecord();

// Create a CSVParser object for the third input file with a Reader and the CSVFormat object
            FileReader inputReader3 = new FileReader("Sensor3_data.csv");
            CSVParser parser3 = new CSVParser(inputReader3, format3);

// Create a CSVFormat object for the fourth input file with the correct header name
            CSVFormat format4 = CSVFormat.DEFAULT.withHeader("Time", "Sensor4").withSkipHeaderRecord();

// Create a CSVParser object for the fourth input file with a Reader and the CSVFormat object
            FileReader inputReader4 = new FileReader("Sensor4_data.csv");
            CSVParser parser4 = new CSVParser(inputReader4, format4);


            Iterator<CSVRecord> iterator1 = parser1.iterator();
            Iterator<CSVRecord> iterator2 = parser2.iterator();
            Iterator<CSVRecord> iterator3 = parser3.iterator();
            Iterator<CSVRecord> iterator4 = parser4.iterator();
            // Loop until both of the iterators run out of records
            while (iterator1.hasNext() && iterator2.hasNext()) {
// Get the next record from the first iterator
                CSVRecord record1 = iterator1.next();
                // Get the next record from the second iterator
                CSVRecord record2 = iterator2.next();
                CSVRecord record3 = iterator3.next();
                CSVRecord record4 = iterator4.next();
                // Check if the time values of the two records are equal
                if (record1.get("Time").equals(record2.get("Time")) && record3.get("Time").equals(record4.get("Time"))) {
                    // Create a new record by merging the two records
                    CSVRecord mergedRecord = mergeRecords(record1, record2, record3, record4);
                    printer.printRecord(mergedRecord);
                }
                // Otherwise, skip the mismatched records
            }
// Close the parsers and the readers for the input files
            parser1.close();
            inputReader1.close();
            parser2.close();
            inputReader2.close();
            parser3.close();
            inputReader3.close();
            parser4.close();
            inputReader4.close();
            printer.close();
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CSVRecord mergeRecords(CSVRecord record1, CSVRecord record2, CSVRecord record3, CSVRecord record4) throws IOException {
        List<String> values = new ArrayList<>();
        for (String value : record1) {
            values.add(value);
        }

        for (int i = 1; i < record2.size(); i++) {
            values.add(record2.get(i));
        }

        for (int i = 1; i < record3.size(); i++) {
            values.add(record3.get(i));
        }

        for (int i = 1; i < record4.size(); i++) {
            values.add(record4.get(i));
        }


        // Convert the list of strings to an array of strings
        String[] array = values.toArray(new String[0]);
        // Use the convertToCSV method to format the array as a CSV string
        String csvString = convertToCSV(array);
        // Create a CSVFormat object with the desired header names and options
        CSVFormat format = CSVFormat.DEFAULT.withHeader("Time", "Sensor1", "Sensor2", "Sensor3", "Sensor4");
        // Parse the CSV string to create a CSVRecord object
        CSVRecord record = format.parse(new StringReader(csvString)).iterator().next();
        // Return the record
        return record;
    }
    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public String convertToCSV(String[] data) {
        return Arrays.stream(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

}
//
//    // A helper method that takes two CSV parsers and returns an iterable of pairs of CSV records
//    public Iterable<CSVRecord[]> zip(CSVParser parser1, CSVParser parser2) {
//        // Create a new list to store the pairs of records
//        List<CSVRecord[]> pairs = new ArrayList<>();
//        // Create iterators for the two parsers
//        Iterator<CSVRecord> iterator1 = parser1.iterator();
//        Iterator<CSVRecord> iterator2 = parser2.iterator();
//        Iterator<CSVRecord> iterator3 = parser2.iterator();
//        Iterator<CSVRecord> iterator4 = parser2.iterator();
//        // Loop until one of the iterators runs out of records
//        while (iterator1.hasNext() && iterator2.hasNext() && iterator3.hasNext() && iterator4.hasNext()) {
//            // Get the next record from each iterator
//            CSVRecord record1 = iterator1.next();
//            CSVRecord record2 = iterator2.next();
//            CSVRecord record3 = iterator3.next();
//            CSVRecord record4 = iterator4.next();
//            // Create a new array to store the pair of records
//            CSVRecord[] pair = new CSVRecord[2];
//            // Assign the records to the array
//            pair[0] = record1;
//            pair[1] = record2;
//            pair[2] = record3;
//            pair[3] = record4;
//            // Add the array to the list of pairs
//            pairs.add(pair);
//        }
//        // Return the list of pairs as an iterable
//        return pairs;
//    }
//}

// Create a CSVParser object for the first input file with a Reader and the CSVFormat object
//            FileReader inputReader1 = new FileReader("Sensor1_data.csv");
//            CSVParser parser1 = new CSVParser(inputReader1, format);
//
//            // Create a CSVParser object for the second input file with a Reader and the CSVFormat object
//            FileReader inputReader2 = new FileReader("Sensor2_data.csv");
//            CSVParser parser2 = new CSVParser(inputReader2, format);
//
//            FileReader inputReader3 = new FileReader("Sensor3_data.csv");
//            CSVParser parser3 = new CSVParser(inputReader3, format);
//
//            FileReader inputReader4 = new FileReader("Sensor4_data.csv");
//            CSVParser parser4 = new CSVParser(inputReader4, format);
// Create iterators for the two parsers
