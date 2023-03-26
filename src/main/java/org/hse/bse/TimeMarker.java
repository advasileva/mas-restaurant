package org.hse.bse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TimeMarker {
    public static Map<String, LocalDateTime> dishTime;
    public static Map<String, LocalDateTime> operationTime;
    public static Map<String, LocalDateTime> equipmentTime;

    public static void init() {
        dishTime = new HashMap<>();
        operationTime = new HashMap<>();
        equipmentTime = new HashMap<>();
    }
}
