package org.hse.bse.agents;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.utils.DataProvider;

public class OrderAgent extends Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, AID> processes = new HashMap<>();
  public static final String AGENT_TYPE = "order";

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    MainController.registerService(this, AGENT_TYPE);

    initProcesses();

    addBehaviour(
        new Behaviour() {
          private MessageTemplate messageTemplate;
          private int step = 0;
          private int repliesCount = 0;

          private double maxTime = 0;

          private static final String CONVERSATION_ID = "order-time";

          @Override
          public void action() {
            switch (step) {
              case 0:
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                for (AID process : processes.values()) {
                  cfpMessage.addReceiver(process);
                }
                cfpMessage.setConversationId(CONVERSATION_ID);
                cfpMessage.setReplyWith("cfp" + System.currentTimeMillis());

                myAgent.send(cfpMessage);
                messageTemplate =
                    MessageTemplate.and(
                        MessageTemplate.MatchConversationId(CONVERSATION_ID),
                        MessageTemplate.MatchInReplyTo(cfpMessage.getReplyWith()));

                log.info("Requested time");
                step = 1;
                break;
              case 1:
                ACLMessage reply = myAgent.receive(messageTemplate);
                if (reply != null) {
                  String time = reply.getContent();
                  log.info(
                      String.format(
                          "Got time %s for process %s", time, reply.getSender().getName()));

                  ++repliesCount;
                  maxTime = Math.max(maxTime, Double.parseDouble(time));

                  if (repliesCount >= processes.size()) {
                    ACLMessage cfpTimeMessage = new ACLMessage(ACLMessage.CFP);
                    cfpTimeMessage.addReceiver(new AID(getArguments()[1].toString()));
                    cfpTimeMessage.setConversationId(CONVERSATION_ID);
                    cfpTimeMessage.setContent(String.valueOf(maxTime));
                    cfpTimeMessage.setReplyWith("cfp" + System.currentTimeMillis());

                    myAgent.send(cfpTimeMessage);

                    step = 3;
                  }
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

  private void initProcesses() {
    JsonArray dishes =
        DataProvider.parse(getArguments()[0].toString()).getAsJsonArray("vis_ord_dishes");
    for (JsonElement dish : dishes) {
      String dishId = ((JsonObject) dish).get("ord_dish_id").getAsString();
      if (!processes.containsKey(dishId)) {
        log.info(String.format("Add dish with id %s", dishId));
        processes.put(
            dishId,
            new AID(
                MainController.addAgent(CookingProcessAgent.class, dishId, new Object[] {dish})));
      }
    }
  }
}
