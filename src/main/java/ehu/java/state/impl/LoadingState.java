package ehu.java.state.impl;

import ehu.java.entity.Ferry;
import ehu.java.entity.Vehicle;
import ehu.java.service.FerryService;
import ehu.java.state.FerryState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadingState implements FerryState {
    private static final Logger logger = LogManager.getLogger(LoadingState.class);

    @Override
    public void handle(Ferry ferry, FerryService ferryService) {

    }
}
