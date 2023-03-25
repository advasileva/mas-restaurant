package org.hse.bse.agents.manager;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.agents.order.Agent;
import org.hse.bse.utils.DataProvider;

public class OrderManager extends CyclicBehaviour {
  private final Logger log = Logger.getLogger(this.getClass().getName());

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

      // Добавить получение продуктов на складе, иначе не создавать заказ

      String id =
          DataProvider.parse(order).get("vis_ord_total").getAsString(); // TODO this is not id
      MainController.addAgent(Agent.class, id, new Object[] {order, msg.getSender().getName()});
    } else {
      block();
    }
  }
}
