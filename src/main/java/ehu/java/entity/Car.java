package ehu.java.entity;

import ehu.java.service.QueueService;

public class Car extends Vehicle {
    public Car(int space, int weight, QueueService queueService) {
        super(space, weight, queueService);
    }
}
