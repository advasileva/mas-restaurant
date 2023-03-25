package org.hse.bse.agents;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.*;
import java.util.logging.Logger;
import org.hse.bse.utils.DataProvider;

public class VisitorAgent extends Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    DFAgentDescription template = new DFAgentDescription();
    ServiceDescription serviceDescription = new ServiceDescription();

    serviceDescription.setType(ManagerAgent.AGENT_TYPE);
    template.addServices(serviceDescription);

    addBehaviour(
        new Behaviour() {
          private MessageTemplate messageTemplate;
          private int step = 0;

          private static final String CONVERSATION_ID = "order";

          @Override
          public void action() {
            switch (step) {
              case 0:
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                cfpMessage.addReceiver(ManagerAgent.aid);
                cfpMessage.setConversationId(CONVERSATION_ID);
                cfpMessage.setReplyWith("cfp" + System.currentTimeMillis());

                myAgent.send(cfpMessage);
                messageTemplate =
                    MessageTemplate.and(
                        MessageTemplate.MatchConversationId(CONVERSATION_ID),
                        MessageTemplate.MatchInReplyTo(cfpMessage.getReplyWith()));

                log.info("Requested menu");
                step = 1;
                break;
              case 1:
                ACLMessage reply = myAgent.receive(messageTemplate);
                if (reply != null) {
                  log.info("Got menu");

                  JsonObject menu = DataProvider.parse(reply.getContent());
                  JsonArray dishes = menu.get("menu_dishes").getAsJsonArray();
                  String selectedId =
                      dishes
                          .get(new Random().nextInt(dishes.size()))
                          .getAsJsonObject()
                          .get("menu_dish_id")
                          .toString();

                  ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

                  order.addReceiver(ManagerAgent.aid);
                  order.setContent(selectedId);
                  order.setConversationId(CONVERSATION_ID);
                  order.setReplyWith("order" + System.currentTimeMillis());

                  log.info(String.format("Requested order with dish id %s", selectedId));

                  myAgent.send(order);
                  messageTemplate =
                      MessageTemplate.and(
                          MessageTemplate.MatchConversationId(CONVERSATION_ID),
                          MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                  step = 2;
                } else {
                  block();
                }
                break;
            }
          }

          @Override
          public boolean done() {
            return step == 2;
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
}
