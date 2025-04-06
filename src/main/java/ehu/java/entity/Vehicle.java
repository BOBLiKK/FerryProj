package ehu.java.entity;

import ehu.java.service.VehicleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class Vehicle implements Callable<Boolean> {
    private static final Logger logger = LogManager.getLogger(Vehicle.class);
    private final VehicleType vehicleType;
    private final int space;
    private final int weight;
    private final VehicleService vehicleService;

    public Vehicle(String type, int space, int weight, VehicleService vehicleService) {
        this.vehicleType = VehicleType.valueOf(type);
        this.space = space;
        this.weight = weight;
        this.vehicleService = vehicleService;
    }

    public int getSpace() {
        return space;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return  vehicleType + " weight: " + weight + " kg " + " space: " + space + " sqm)";
    }

    @Override
    public Boolean call()  {
        try {
            Random random = new Random();
            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            logger.info(vehicleType + " arrived for boarding.");
            return vehicleService.getOnBoard(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Vehicle  boarding interrupted!");
            return false;
        }
    }
}
