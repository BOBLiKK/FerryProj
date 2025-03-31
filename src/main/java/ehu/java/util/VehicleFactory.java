package ehu.java.util;

import ehu.java.entity.Vehicle;
import ehu.java.entity.Car;
import ehu.java.entity.Truck;
import ehu.java.service.QueueService;
import static ehu.java.constant.ErrorMessageConstant.*;
import static ehu.java.constant.ParameterNameConstant.*;

public class VehicleFactory {
    public static Vehicle createVehicle(String type, int space, int weight, QueueService queueService) {
        switch (type.toUpperCase()) {
            case CAR:
                return new Car(space, weight, queueService);
            case TRUCK:
                return new Truck(space, weight, queueService);
            default:
                throw new IllegalArgumentException(UNKNOWN_TYPE + type);
        }
    }
}
