package ehu.java.main;

import ehu.java.entity.Ferry;
import ehu.java.service.FerryService;
import ehu.java.util.FerryConfigReader;
import static ehu.java.constant.FileNameConstant.*;


public class Main {
    public static void main(String[] args) {
        int[] ferryParams = FerryConfigReader.readFerryConfig(FERRY_FILE);
        Ferry ferry = new Ferry(ferryParams[0], ferryParams[1]);
        FerryService.init(ferry);
        FerryService ferryService = FerryService.getInstance();
        ferryService.process();
        ferryService.awaitCompletion();
    }
}
