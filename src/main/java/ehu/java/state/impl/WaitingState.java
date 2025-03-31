package ehu.java.state.impl;


import ehu.java.entity.Ferry;
import ehu.java.service.FerryService;
import ehu.java.state.FerryState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static ehu.java.constant.MessageConstant.*;

public class WaitingState implements FerryState {
    private static final Logger logger = LogManager.getLogger(WaitingState.class);

    @Override
    public void handle(Ferry ferry, FerryService ferryService) {

    }
}