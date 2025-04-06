package ehu.java.service;

import ehu.java.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VehicleService{
    private static final Logger logger = LogManager.getLogger(VehicleService.class);
    private final FerryService ferryService;

    public VehicleService() {
        this.ferryService = FerryService.getInstance();
    }

    public boolean getOnBoard(Vehicle vehicle) {
        return ferryService.tryLoadVehicle(vehicle);
    }
}



