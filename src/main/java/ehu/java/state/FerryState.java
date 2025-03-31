package ehu.java.state;

import ehu.java.entity.Ferry;
import ehu.java.service.FerryService;

public interface FerryState {
    void handle(Ferry ferry, FerryService ferryService);
}
