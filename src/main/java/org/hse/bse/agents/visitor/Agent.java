package org.hse.bse.agents.visitor;

import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.logging.Logger;
import org.hse.bse.MainController;

public class Agent extends jade.core.Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    MainController.registerService(this, org.hse.bse.agents.manager.Agent.AGENT_TYPE);

    addBehaviour(new OrderMaker(getArguments()));
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
}