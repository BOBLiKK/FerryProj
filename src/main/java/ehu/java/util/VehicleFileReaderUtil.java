package ehu.java.util;

import ehu.java.entity.Vehicle;
import ehu.java.service.QueueService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static ehu.java.constant.ErrorMessageConstant.*;

public class VehicleFileReaderUtil {
    private static final Logger logger = LogManager.getLogger(VehicleFileReaderUtil.class);
    public static List<Vehicle> readVehiclesFromFile(String fileName, QueueService queueService) {

        List<Vehicle> vehicles = new ArrayList<>();

        InputStream inputStream = VehicleFileReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            logger.error(FILE_NOT_FOUND);
            throw new RuntimeException(fileName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length != 4) {
                    logger.error(INVALID_FILE_FORMAT);
                    throw new IllegalArgumentException(line);
                }

                String type = parts[0];
                int count = Integer.parseInt(parts[1]);
                int space = Integer.parseInt(parts[2]);
                int weight = Integer.parseInt(parts[3]);

                for (int i = 0; i < count; i++) {
                    vehicles.add(VehicleFactory.createVehicle(type, space, weight, queueService));
                }
            }
        } catch (IOException e) {
            logger.error(ERROR_READING_FILE);
        }
        return vehicles;
    }
}
