package org.hse.bse.agents.equipment;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import org.hse.bse.MainController;

import java.util.logging.Logger;

public class EquipmentAgent extends jade.core.Agent {
  private int equip_type_id;
  private String equip_type_name;

  private final Logger log = Logger.getLogger(this.getClass().getName());

  public static final String AGENT_TYPE = "equipment";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));
    aid = getAID();
    MainController.registerService(this, AGENT_TYPE);

    addBehaviour(new EquipmentBehaviour());
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

    log.info("Terminate equipment: " + getAID().getName());
  }
}
