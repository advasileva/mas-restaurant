package org.hse.bse.agents.order;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.hse.bse.agents.store.StoreAgent;

import java.util.Map;
import java.util.logging.Logger;

public class OrderInformer extends Behaviour { // Тикер, запрашивающий время и процесса и сообщающий пользователю
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private MessageTemplate messageTemplate;

    private int step = 0;

    private int repliesCount = 0;

    private double maxTime = 0;

    private static final String CONVERSATION_ID = "order-time";

    private final Object[] args;

    private final Map<String, AID> processes;

    public OrderInformer(Object[] args, Map<String, AID> processes) {
        this.args = args;
        this.processes = processes;
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
            case 2:
                cfpMessage = new ACLMessage(ACLMessage.CFP);
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
                step = 3;
                break;
            case 3:
                reply = myAgent.receive(messageTemplate);
                if (reply != null) {
                    String time = reply.getContent();
                    log.info(
                            String.format(
                                    "Got time %s for process %s",
                                    time, reply.getSender().getName()));

                    ++repliesCount;
                    maxTime = Math.max(maxTime, Double.parseDouble(time));

                    if (repliesCount >= processes.size()) {
                        ACLMessage cfpTimeMessage = new ACLMessage(ACLMessage.CFP);
                        cfpTimeMessage.addReceiver(new AID(args[1].toString()));
                        cfpTimeMessage.setConversationId(CONVERSATION_ID);
                        cfpTimeMessage.setContent(String.valueOf(maxTime));
                        cfpTimeMessage.setReplyWith("cfp" + System.currentTimeMillis());

                        myAgent.send(cfpTimeMessage);

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
        return step == 4;
    }
}
