package org.hse.bse.agents.manager;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.agents.order.OrderAgent;
import org.hse.bse.utils.DataProvider;

public class OrderCreator extends CyclicBehaviour {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private static final String CONVERSATION_ID = "order-manager";

  @Override
  public void action() {
    MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
    ACLMessage msg = myAgent.receive(messageTemplate);

    if (msg != null) {
      String order = msg.getContent();
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);

      log.info(String.format("Received order: %s", order));

      myAgent.send(reply);

      String id = DataProvider.parse(order).get("vis_name").getAsString(); // TODO this is not id
      MainController.addAgent(
          OrderAgent.class, id, new Object[] {order, msg.getSender().getName()});

    } else {
      block();
    }
  }
}
