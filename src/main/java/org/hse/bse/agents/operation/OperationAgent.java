package org.hse.bse.agents.operation;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.MainController;

public class OperationAgent extends jade.core.Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, String> visitors = new HashMap<>();

  public static final String AGENT_TYPE = "operation";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    aid = getAID();
    MainController.registerService(this, AGENT_TYPE);

    // addBehaviour();
  }

  @Override
  protected void takeDown() {
    try {
      DFService.deregister(this);
    } catch (FIPAException fe) {
      fe.printStackTrace();
    }

    log.info("Terminate chef: " + getAID().getName());
  }
}
