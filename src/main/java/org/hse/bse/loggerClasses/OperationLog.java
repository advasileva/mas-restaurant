package org.hse.bse.loggerClasses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OperationLog {
    public static List<OperationLogEntity> operation_log = new ArrayList<>();

    public static void dumpLog() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter("operation_log.json");
        writer.write(gson.toJson(operation_log));
        writer.close();
    }
}
