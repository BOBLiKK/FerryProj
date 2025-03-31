package ehu.java.entity;

import ehu.java.service.QueueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static ehu.java.constant.MessageConstant.*;

public abstract class Vehicle implements Callable<Boolean> {
    protected final int space;
    protected final int weight;
    private final QueueService queueService;

    private static final Logger logger = LogManager.getLogger(Vehicle.class);

    public Vehicle(int space, int weight, QueueService queueService) {
        this.space = space;
        this.weight = weight;
        this.queueService = queueService;
    }

    @Override
    public Boolean call() {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
            logger.info(this + VEHICLE_ARRIVED_MESSAGE);
            queueService.addVehicle(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }

    public int getSpace() {
        return space;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (Weight: " + weight + " kg, Space: " + space + " sqm )";
    }
}
