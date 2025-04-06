package ehu.java.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FerryConfigReader {
    private static final Logger logger = LogManager.getLogger(FerryConfigReader.class);

    public static int[] readFerryConfig(String fileName) {
        InputStream inputStream = FerryConfigReader.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            logger.error("File not found. ");
            throw new RuntimeException(fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            if (line == null) {
                logger.error("File is empty");
                throw new RuntimeException(fileName);
            }

            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                logger.error("Invalid file format.");
                throw new IllegalArgumentException(fileName);
            }

            int maxWeight = Integer.parseInt(parts[0]);
            int maxSpace = Integer.parseInt(parts[1]);

            return new int[]{maxWeight, maxSpace};
        } catch (IOException e) {
            logger.error("Error reading file.");
            throw new RuntimeException(fileName, e);
        }
    }
}
