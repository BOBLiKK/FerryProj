package ehu.java.state.impl;

import ehu.java.entity.Ferry;
import ehu.java.service.FerryService;
import ehu.java.state.FerryState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import static ehu.java.constant.MessageConstant.*;
import static ehu.java.constant.NumericalConstant.*;

public class SailingState implements FerryState {
    private static final Logger logger = LogManager.getLogger(SailingState.class);

    @Override
    public void handle(Ferry ferry, FerryService ferryService) {

    }
}
