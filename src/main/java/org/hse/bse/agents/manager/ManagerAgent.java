package org.hse.bse.agents.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.agents.store.StoreAgent;
import org.hse.bse.agents.visitor.VisitorAgent;
import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

public class ManagerAgent extends jade.core.Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, String> visitors = new HashMap<>();

  public static final String AGENT_TYPE = "manager";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    aid = getAID();
    MainController.registerService(this, AGENT_TYPE);

    initAgents();

    addBehaviour(new MenuProvider());
    addBehaviour(new OrderCreator());
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

  private void initAgents() {
    initVisitors();
    initStore();
  }

  private void initVisitors() {
    JsonArray orders =
        DataProvider.readAsJson(Data.visitorsOrders).getAsJsonArray("visitors_orders");
    for (JsonElement order : orders) {
      String visitorName = ((JsonObject) order).get("vis_name").getAsString();
      log.info(String.format("Add visitor with name %s", visitorName));
      visitors.put(
          visitorName,
          MainController.addAgent(VisitorAgent.class, visitorName, new Object[] {order}));
    }
  }

  private void initStore() {
    MainController.addAgent(
        StoreAgent.class,
        "",
        new Object[] {
          DataProvider.read(Data.productTypes), DataProvider.read(Data.products),
        });
  }
}
