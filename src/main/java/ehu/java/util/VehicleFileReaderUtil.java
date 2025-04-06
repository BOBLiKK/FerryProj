package ehu.java.util;

import ehu.java.entity.Vehicle;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import ehu.java.service.VehicleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class VehicleFileReaderUtil {
    private static final Logger logger = LogManager.getLogger(VehicleFileReaderUtil.class);
    public static List<Vehicle> readVehiclesFromFile(String fileName) {

        List<Vehicle> vehicles = new ArrayList<>();

        InputStream inputStream = VehicleFileReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            logger.error("File not found. ");
            throw new RuntimeException(fileName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length != 4) {
                    logger.error("Invalid file format. ");
                    throw new IllegalArgumentException(line);
                }

                String type = parts[0];
                int count = Integer.parseInt(parts[1]);
                int space = Integer.parseInt(parts[2]);
                int weight = Integer.parseInt(parts[3]);

                for (int i = 0; i < count; i++) {
                    vehicles.add(new Vehicle(type, space, weight, new VehicleService()));
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file. ");
        }
        return vehicles;
    }
}
