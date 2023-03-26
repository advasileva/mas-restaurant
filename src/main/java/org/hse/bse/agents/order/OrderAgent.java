package org.hse.bse.agents.order;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;

import org.hse.bse.MainController;
import org.hse.bse.agents.process.ProcessAgent;
import org.hse.bse.utils.DataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OrderAgent extends jade.core.Agent {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final Map<String, AID> processes = new HashMap<>();

    public static final String AGENT_TYPE = "order";

    @Override
    protected void setup() {
        log.info(String.format("Init %s", getAID().getName()));

        MainController.registerService(this, AGENT_TYPE);

        initProcesses();

        addBehaviour(new OrderPerformer(getArguments()));
        addBehaviour(new OrderInformer(getArguments(), processes, this));
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException exc) {
            exc.printStackTrace();
        }

        log.info(String.format("Terminate %s", getAID().getName()));
    }

    private void initProcesses() {
        JsonArray dishes =
                DataProvider.parse(getArguments()[0].toString()).getAsJsonArray("vis_ord_dishes");
        for (JsonElement dish : dishes) {
            String dishId = ((JsonObject) dish).get("ord_dish_id").getAsString();
            log.info(String.format("Add dish with id %s", dishId));
            processes.put(
                    dishId,
                    new AID(
                            MainController.addAgent(
                                    ProcessAgent.class, dishId, new Object[] {dish})));
        }
    }
}
