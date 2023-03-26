package org.hse.bse.agents.proces;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;

import org.hse.bse.MainController;
import org.hse.bse.agents.operation.OperationAgent;
import org.hse.bse.agents.store.ProductDistributor;
import org.hse.bse.utils.DataProvider;

import java.util.logging.Logger;

public class ProcessAgent extends jade.core.Agent {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public static final String AGENT_TYPE = "process";

    public static AID aid;

    private boolean used = true;

    @Override
    protected void setup() {
        log.info(String.format("Init %s", getAID().getName()));

        aid = getAID();
        MainController.registerService(this, AGENT_TYPE);

        initOperations();

        addBehaviour(new ProcessStarter());
        addBehaviour(new ProcessTimer());
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        log.info("Terminate cooking process: " + getAID().getName());
    }

    private void initOperations() {
        JsonArray operations =
                ProductDistributor.operationsByDish.get(
                        DataProvider.parse(getArguments()[0].toString())
                                .get("menu_dish")
                                .getAsString());
        for (JsonElement operation : operations) {
            String opId = operation.getAsJsonObject().get("oper_type").getAsString();
            log.info(String.format("Add operation with id %s", opId));
            MainController.addAgent(OperationAgent.class, opId, new Object[] {operation});
        }
    }
}
