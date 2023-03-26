package org.hse.bse.agents.equipment;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class EquipmentBehaviour extends CyclicBehaviour {
  private LocalDateTime freeTime = LocalDateTime.now();
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private Integer getSleepingTime(String title) {
    return (int)(Double.parseDouble(title) * 60);
  }

  @Override
  public void action() {
    MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    ACLMessage msg = myAgent.receive(messageTemplate);
    if (msg != null) {
      String title = msg.getContent();
      if (LocalDateTime.now().compareTo(freeTime) > 0) {
        freeTime = LocalDateTime.now();
      }
      freeTime = freeTime.plusSeconds(getSleepingTime(title));
      log.info(getAgent().getAID().getName() + " reserved");
      while (LocalDateTime.now().compareTo(freeTime) < 0);
      log.info(getAgent().getAID().getName() + " now is not reserved");
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
    }
  }
}
