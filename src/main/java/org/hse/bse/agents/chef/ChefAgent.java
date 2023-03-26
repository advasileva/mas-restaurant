package org.hse.bse.agents.chef;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import org.hse.bse.MainController;

import java.util.logging.Logger;

public class ChefAgent extends jade.core.Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  public static final String AGENT_TYPE = "chef";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    aid = getAID();
    MainController.registerService(this, AGENT_TYPE);

    addBehaviour(new ChefBehaviour());
  }

  public void setUp() {
    setup();
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
