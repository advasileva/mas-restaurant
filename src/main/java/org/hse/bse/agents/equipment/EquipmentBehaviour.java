package org.hse.bse.agents.equipment;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class EquipmentBehaviour extends CyclicBehaviour {
  private LocalDateTime freeTime = LocalDateTime.now();
  private final Logger log = Logger.getLogger(this.getClass().getName());

  @Override
  public void action() {
    MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    ACLMessage msg = myAgent.receive(messageTemplate);
    if (msg != null) {
      System.out.println("received message");
      String title = msg.getContent();
      if (title.equals("using")) {
        if (LocalDateTime.now().compareTo(freeTime) > 0) {
          freeTime = LocalDateTime.now();
        }
        freeTime = freeTime.plusSeconds(5);
        long waiting = ChronoUnit.SECONDS.between(LocalDateTime.now(), freeTime);
        System.out.println("started waiting");
        while (LocalDateTime.now().compareTo(freeTime) < 0);
        System.out.println("finished waiting");
      } else {
        freeTime = LocalDateTime.now();
      }
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
    }
  }
}
