package org.example.groundstationdemo;

public class DataPersistenceHandler {
    private final DataGenerator dataGenerator;
    private final String saveFilename;

    public DataPersistenceHandler(DataGenerator dataGenerator, String saveFilename) {
        this.dataGenerator = dataGenerator;
        this.saveFilename = saveFilename;
    }

    public void startSavingData() {
        new Thread(() -> {
            while (true) { // Replace with your desired condition
                dataGenerator.saveDataToCSV(saveFilename);

                try {
                    // Add a delay between saves (e.g., 5 seconds)
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
