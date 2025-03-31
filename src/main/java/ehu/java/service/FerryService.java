package ehu.java.service;

import ehu.java.entity.Ferry;
import ehu.java.entity.FerryState;
import ehu.java.entity.Vehicle;
import ehu.java.util.FerryConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import static ehu.java.constant.FileNameConstant.*;
import static ehu.java.constant.NumericalConstant.*;
import static ehu.java.constant.MessageConstant.*;

public class FerryService {
    private static FerryService instance;
    private static final Lock instanceLock = new ReentrantLock(); // Блокировка Singleton
    private final Ferry ferry;
    private final Lock ferryLock = new ReentrantLock(); // Блокировка для работы с паромом
    private final QueueService queueService;

    private static final Logger logger = LogManager.getLogger(FerryService.class);


    private FerryService(QueueService queueService) {
        this.queueService = queueService;
        int[] ferryParams = FerryConfigReader.readFerryConfig(FERRY_FILE);
        this.ferry = new Ferry(ferryParams[0], ferryParams[1]);
    }

    public static FerryService getInstance(QueueService queueService) {
        instanceLock.lock();
        try {
            if (instance == null) {
                instance = new FerryService(queueService);
            }
        } finally {
            instanceLock.unlock();
        }
        return instance;
    }

    public void loadAllVehicles() {
        int emptyIterations = 0;
        while (emptyIterations < MAX_EMPTY_ITERATIONS) {
            ferryLock.lock();
            try {
                if (ferry.getCurrentState() == FerryState.SAILING) {
                    logger.info(FERRY_TRAVEL_MESSAGE);
                } else {
                    ferry.setCurrentState(FerryState.LOADING);
                    Vehicle vehicle = queueService.pollVehicle();
                    if (vehicle == null) {
                        emptyIterations++;
                        logger.info(EMPTY_QUEUE_MESSAGE + (MAX_EMPTY_ITERATIONS - emptyIterations));
                        TimeUnit.MILLISECONDS.sleep(500);
                        continue;
                    }

                    emptyIterations = 0;
                    if (canBoard(vehicle)) {
                        loadVehicle(vehicle);
                    } else {
                        depart();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                ferryLock.unlock();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info(FINISH_JOB_MESSAGE);
    }


    private boolean canBoard(Vehicle vehicle) {
        return ferry.getCurrentWeight() + vehicle.getWeight() <= ferry.getMaxWeight() &&
                ferry.getCurrentSpace() + vehicle.getSpace() <= ferry.getMaxSpace();
    }

    private void loadVehicle(Vehicle vehicle) {
        ferry.increaseCurrentWeight(vehicle.getWeight());
        ferry.increaseCurrentSpace(vehicle.getSpace());
        ferry.incrementNumberOfVehiclesOnBoard();
    }

    private void depart() {
        logger.info(FERRY_LEAVES_MESSAGE + ferry.getNumberOfVehiclesOnBoard());
        ferry.setCurrentState(FerryState.SAILING);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(TRAVEL_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ferryLock.lock();
            try {
                ferry.clearFerryStorage();
                ferry.setCurrentState(FerryState.WAITING);
                logger.info(START_LOADING_MESSAGE);
            } finally {
                ferryLock.unlock();
            }
        }).start();
    }
}