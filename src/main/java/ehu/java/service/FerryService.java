package ehu.java.service;

import ehu.java.entity.Ferry;
import ehu.java.entity.FerryState;
import ehu.java.entity.Vehicle;
import ehu.java.util.VehicleFileReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static ehu.java.constant.FileNameConstant.VEHICLES_FILE;
import static ehu.java.constant.NumericalConstant.*;


public class FerryService {
    private static final Logger logger = LogManager.getLogger(FerryService.class);
    private static FerryService ferryServiceInstance;
    private static final Lock instanceLock = new ReentrantLock();
    private final Ferry ferry;
    private final ExecutorService ferryExecutor;
    private final Lock ferryLock = new ReentrantLock();

    private FerryService(Ferry ferry) {
        this.ferry = ferry;
        this.ferryExecutor = Executors.newSingleThreadExecutor();
    }

    public static void init(Ferry ferry) {
        instanceLock.lock();
        try {
            if (ferryServiceInstance == null) {
                ferryServiceInstance = new FerryService(ferry);
            } else {
                logger.warn("FerryService has already been initialized");
            }
        } finally {
            instanceLock.unlock();
        }
    }

    public static FerryService getInstance() {
        if (ferryServiceInstance == null) {
            logger.error("FerryService has not been initialized");
        }
        return ferryServiceInstance;
    }

    public void process() {
        ferryExecutor.execute(this::loadAllVehicles);
    }

    private void loadAllVehicles() {
        List<Vehicle> vehicles = VehicleFileReaderUtil.readVehiclesFromFile(VEHICLES_FILE);
        ferry.setCurrentState(FerryState.LOADING);
        for (Vehicle vehicle : vehicles) {
            ferryLock.lock();
            try {
                boolean isOnboard = vehicle.call();
                if (!isOnboard) {
                    logger.info("No place on board, waiting for next arrival.");
                    depart();
                    TimeUnit.SECONDS.sleep(TRAVEL_TIME);
                    vehicle.call();
                }
            } catch (InterruptedException e) {
                logger.error("Loading interrupted.");
            } finally {
                ferryLock.unlock();
            }
        }
        if (!ferry.isEmpty()) {
            ferryLock.lock();
            try{
                depart();
            }finally {
                ferryLock.unlock();
            }
        }
        logger.info("All vehicles processed");
    }

    public boolean canBoard(Vehicle vehicle) {
        ferryLock.lock();
        try {
            return ferry.getCurrentState() == FerryState.LOADING &&
                    ferry.getCurrentWeight() + vehicle.getWeight() <= ferry.getMaxWeight() &&
                    ferry.getCurrentSpace() + vehicle.getSpace() <= ferry.getMaxSpace();
        } finally {
            ferryLock.unlock();
        }
    }

    public boolean tryLoadVehicle(Vehicle vehicle) {
        ferryLock.lock();
        try {
            if (canBoard(vehicle)) {
                loadVehicle(vehicle);
                return true;
            }
            return false;
        } finally {
            ferryLock.unlock();
        }
    }

    public void loadVehicle(Vehicle vehicle) {
        ferry.increaseCurrentWeight(vehicle.getWeight());
        ferry.increaseCurrentSpace(vehicle.getSpace());
        ferry.incrementNumberOfVehiclesOnBoard();
        logger.info(vehicle + " on board");
    }

    private void depart() {
        logger.info("🚢 Ferry leaves. Number of vehicles on board:  " + ferry.getNumberOfVehiclesOnBoard());
        ferry.setCurrentState(FerryState.SAILING);
        try{
            TimeUnit.SECONDS.sleep(TRAVEL_TIME);
        }catch (InterruptedException e){
            logger.error("Departure error");
        }
        ferry.clearFerryStorage();
        ferry.setCurrentState(FerryState.LOADING);
        logger.info("⚓ Ferry arrived empty. Start loading...");
    }

    public void awaitCompletion() {
        try {
            ferryExecutor.shutdown();
            if (!ferryExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                ferryExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ferryExecutor.shutdownNow();
        }
    }
}
