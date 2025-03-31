package ehu.java.main;

import ehu.java.entity.Vehicle;
import ehu.java.service.FerryService;
import ehu.java.service.QueueService;
import ehu.java.util.VehicleFileReaderUtil;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static ehu.java.constant.FileNameConstant.*;
import static ehu.java.constant.NumericalConstant.*;

public class Main {
    public static void main(String[] args) {
        QueueService queueService = new QueueService();
        FerryService ferryService = FerryService.getInstance(queueService);
        ExecutorService vehicleExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        List<Vehicle> vehicles = VehicleFileReaderUtil.readVehiclesFromFile(VEHICLES_FILE, queueService);

        for (Vehicle vehicle : vehicles) {
            vehicleExecutor.submit(vehicle);
        }
        new Thread(ferryService::loadAllVehicles).start();
        vehicleExecutor.shutdown();
    }
}
