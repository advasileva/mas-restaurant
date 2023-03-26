package org.hse.bse.agents.proces;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;

import org.hse.bse.MainController;
import org.hse.bse.agents.operation.OperationAgent;
import org.hse.bse.agents.store.ProductDistributor;
import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

public class ProcessAgent extends jade.core.Agent {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public static final String AGENT_TYPE = "process";

    public static AID aid;

    private static JsonArray processes = new JsonArray();

    private JsonObject process = new JsonObject();

    private String uuid;

    private JsonArray operations = new JsonArray();

    private boolean used = true;

    @Override
    protected void setup() {
        log.info(String.format("Init %s", getAID().getName()));

        aid = getAID();
        MainController.registerService(this, AGENT_TYPE);

        initOperations();

        addBehaviour(new ProcessStarter());
        addBehaviour(new ProcessTimer());

        uuid = UUID.randomUUID().toString();
        process.addProperty("proc_id", uuid);
        process.addProperty(
                "ord_dish",
                DataProvider.parse(getArguments()[0].toString()).get("menu_dish").getAsString());
        process.addProperty("proc_started", LocalDateTime.now().toString());
        process.addProperty("proc_acrive", "false");
    }

    @Override
    protected void takeDown() {
        process.addProperty("proc_ended", LocalDateTime.now().toString());
        process.add("proc_operations", operations);
        processes.add(process);
        JsonObject logs = new JsonObject();
        logs.add("process_log", processes);

        DataProvider.writeJson(Data.processLog, logs);

        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        log.info("Terminate cooking process: " + getAID().getName());
    }

    private void initOperations() {
        JsonArray ops =
                ProductDistributor.operationsByDish.get(
                        DataProvider.parse(getArguments()[0].toString())
                                .get("menu_dish")
                                .getAsString());
        for (JsonElement operation : ops) {
            String opId = operation.getAsJsonObject().get("oper_type").getAsString();
            log.info(String.format("Add operation with id %s", opId));
            String opUuid = UUID.randomUUID().toString();
            MainController.addAgent(OperationAgent.class, opId, new Object[] {operation, opUuid});
            JsonObject op = new JsonObject();
            op.addProperty("proc_oper", opUuid);
            operations.add(op);
        }
    }
}
