package ehu.java.entity;

import ehu.java.service.QueueService;

public class Truck extends Vehicle {
    public Truck(int space, int weight, QueueService queueService) {
        super(space, weight, queueService);
    }
}
