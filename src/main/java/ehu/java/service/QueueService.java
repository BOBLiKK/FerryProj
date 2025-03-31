package ehu.java.service;

import ehu.java.entity.Vehicle;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class QueueService {
    private final Queue<Vehicle> waitingQueue = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void addVehicle(Vehicle vehicle) {
        lock.lock();
        try {
            waitingQueue.add(vehicle);
        } finally {
            lock.unlock();
        }
    }

    public Vehicle pollVehicle() {
        lock.lock();
        try {
            return waitingQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    public void addVehicleBack(Vehicle vehicle) {
        lock.lock();
        try {
            ((LinkedList<Vehicle>) waitingQueue).addFirst(vehicle);
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return waitingQueue.isEmpty();
    }
}


