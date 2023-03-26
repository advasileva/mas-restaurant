package org.hse.bse.agents.order;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.hse.bse.agents.store.StoreAgent;

import java.util.logging.Logger;

public class OrderPerformer extends Behaviour {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private MessageTemplate messageTemplate;

    private int step = 0;

    private static final String CONVERSATION_ID = "order-time";

    private final Object[] args;

    public OrderPerformer(Object[] args) {
        this.args = args;
    }

    @Override
    public void action() {
        switch (step) {
            case 0:
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                cfpMessage.addReceiver(StoreAgent.aid);
                cfpMessage.setConversationId(CONVERSATION_ID);
                cfpMessage.setContent(args[0].toString());
                cfpMessage.setReplyWith("cfp" + System.currentTimeMillis());

                myAgent.send(cfpMessage);
                messageTemplate =
                        MessageTemplate.and(
                                MessageTemplate.MatchConversationId(CONVERSATION_ID),
                                MessageTemplate.MatchInReplyTo(cfpMessage.getReplyWith()));

                log.info("Requested products");
                step = 1;
                break;
            case 1:
                ACLMessage reply = myAgent.receive(messageTemplate);
                if (reply != null) {
                    if (reply.getContent().contentEquals("available")) {
                        log.info("Products are available in store");
                        step = 2;
                    } else {
                        log.info("There are not enough products. Order cancelled");
                        step = 4;
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
}
