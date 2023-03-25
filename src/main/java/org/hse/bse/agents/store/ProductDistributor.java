package org.hse.bse.agents.store;

import com.google.gson.JsonObject;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.utils.DataProvider;

public class ProductDistributor extends CyclicBehaviour {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, JsonObject> products;

  ProductDistributor(Map<String, JsonObject> products) {
    this.products = products;
  }

  @Override
  public void action() {
    MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
    ACLMessage msg = myAgent.receive(messageTemplate);

    if (msg != null) {
      JsonObject order = DataProvider.parse(msg.getContent());

      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.PROPOSE);

      if (isAvailable()) {
        reply.setContent("available");
        discard();
      } else {
        reply.setContent("not enought");
      }

      myAgent.send(reply);
    } else {
      block();
    }
  }

  private boolean isAvailable() {
    return true;
  }

  private void discard() {
    // Синкнуться на продукты!!!
  }
}
