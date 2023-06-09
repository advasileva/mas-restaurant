package org.hse.bse.agents.visitor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.hse.bse.agents.manager.ManagerAgent;
import org.hse.bse.utils.DataProvider;

import java.util.logging.Logger;

public class OrderMaker extends Behaviour {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private MessageTemplate messageTemplate;

    private int step = 0;

    private static final String CONVERSATION_ID = "order";

    private final Object[] args;

    public OrderMaker(Object[] args) {
        this.args = args;
    }

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
                    JsonObject menu = DataProvider.parse(reply.getContent());
                    JsonArray dishes = menu.get("menu_dishes").getAsJsonArray();

                    log.info(String.format("Got menu with %d dishes", dishes.size()));
                    String orderJson = args[0].toString();

                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

                    order.addReceiver(ManagerAgent.aid);
                    order.setContent(orderJson);
                    order.setConversationId(CONVERSATION_ID);
                    order.setReplyWith("order" + System.currentTimeMillis());

                    log.info(String.format("Requested order %s", orderJson));

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

            case 2:
                MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                ACLMessage msg = myAgent.receive(messageTemplate);

                if (msg != null) {
                    log.info(
                            String.format(
                                    "Got time %s for order %s",
                                    msg.getContent(), msg.getSender().getName()));
                    if (Double.parseDouble(msg.getContent()) == 0) {
                        log.info("Got order and exit");
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
        return step == 3;
    }
}
