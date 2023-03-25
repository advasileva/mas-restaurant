package org.hse.bse.agents;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.configuration.JadeAgent;
import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

@JadeAgent(number = 1)
public class ManagerAgent extends Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, String> visitors = new HashMap<>();

  public static final String AGENT_TYPE = "manager";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    aid = getAID();

    initAgents();

    DFAgentDescription agentDescription = new DFAgentDescription();
    agentDescription.setName(getAID());

    ServiceDescription serviceDescription = new ServiceDescription();
    serviceDescription.setType(AGENT_TYPE);
    serviceDescription.setName("JADE-order");

    agentDescription.addServices(serviceDescription);

    try {
      DFService.register(this, agentDescription);
    } catch (FIPAException ex) {
      ex.printStackTrace();
    }

    addBehaviour(
        new CyclicBehaviour() {
          @Override
          public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(messageTemplate);

            if (msg != null) {
              ACLMessage reply = msg.createReply();
              reply.setPerformative(ACLMessage.PROPOSE);
              reply.setContent(DataProvider.read(Data.menuDishes));

              myAgent.send(reply);
            } else {
              block();
            }
          }
        });

    addBehaviour(
        new CyclicBehaviour() {
          @Override
          public void action() {
            MessageTemplate messageTemplate =
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(messageTemplate);

            if (msg != null) {
              String order = msg.getContent();
              ACLMessage reply = msg.createReply();
              reply.setPerformative(ACLMessage.INFORM);

              log.info(String.format("Received order: %s", order));

              myAgent.send(reply);

              String id =
                  DataProvider.parse(order)
                      .get("vis_ord_total")
                      .getAsString(); // TODO this is not id
              MainController.addAgent(
                  OrderAgent.class, id, new Object[] {order, msg.getSender().getName()});
              // TODO cutomize OrderAgent
            } else {
              block();
            }
          }
        });
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

  private void initAgents() {
    initVisitors();
  }

  private void initVisitors() {
    JsonArray orders =
        DataProvider.readAsJson(Data.visitorsOrders).getAsJsonArray("visitors_orders");
    for (JsonElement order : orders) {
      String visitorName = ((JsonObject) order).get("vis_name").getAsString();
      if (!visitors.containsKey(visitorName)) {
        log.info(String.format("Add visitor with name %s", visitorName));
        visitors.put(
            visitorName,
            MainController.addAgent(VisitorAgent.class, visitorName, new Object[] {order}));
      }
    }
  }
}
