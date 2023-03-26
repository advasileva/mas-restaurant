package org.hse.bse.utils;

import java.util.logging.Logger;

public class DataChecker {
    private static final Logger log = Logger.getLogger(DataChecker.class.getName());
    public static void check(Data type, String content) {
        log.info(String.format("Checking %s", type.getFilename()));
        switch (type) {
            case cookers:
                break;
            case dishCards:
                break;
            case equipment:
                break;
            case equipmentType:
                break;
            case menuDishes:
                break;
            case operationTypes:
                break;
            case productTypes:
                break;
            case products:
                break;
            case visitorsOrders:
                break;
            default:
                log.warning(String.format("Unrecognized data type %s", type.getFilename()));
        }
    }
}
