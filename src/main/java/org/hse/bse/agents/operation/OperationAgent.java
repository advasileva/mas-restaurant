package org.hse.bse.agents.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import org.hse.bse.MainController;
import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class OperationAgent extends jade.core.Agent {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    public static final String AGENT_TYPE = "operation";

    private static JsonArray operations = new JsonArray();

    private JsonObject operation = new JsonObject();

    public static AID aid;

    @Override
    protected void setup() {
        log.info(String.format("Init %s", getAID().getName()));

        aid = getAID();
        MainController.registerService(this, AGENT_TYPE);

        operation.addProperty("oper_id", getArguments()[1].toString());
        operation.addProperty("oper_proc", getArguments()[2].toString());
        operation.addProperty("oper_started", LocalDateTime.now().toString());
        operation.addProperty("proc_acrive", "false");
    }

    @Override
    protected void takeDown() {
        operation.addProperty("oper_ended", LocalDateTime.now().toString());
        operations.add(operation);
        JsonObject logs = new JsonObject();
        logs.add("operation_log", operations);

        DataProvider.writeJson(Data.operationLog, logs);

        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        log.info("Terminate operation: " + getAID().getName());
    }
}
