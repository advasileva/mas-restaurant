package org.hse.bse.agents.equipment;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Logger;

public class EquipmentBehaviour extends CyclicBehaviour {
  private boolean used = false;

  private final Logger log = Logger.getLogger(this.getClass().getName());

  @Override
  public void action() {
    ACLMessage msg = myAgent.receive();
    if (msg != null) {
      String title = msg.getContent();
      if (title.equals("using")) {
        if (used) {
          log.info(getAgent().getAID().getName() + " is already used!");
        } else {
          used = true;
          log.info(getAgent().getAID().getName() + " reserved by " + msg.getSender().getName());
        }
      } else {
        used = false;
      }
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
    }
  }
}
