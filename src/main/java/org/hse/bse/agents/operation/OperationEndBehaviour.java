package org.hse.bse.agents.operation;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

public class OperationEndBehaviour extends CyclicBehaviour {
    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(messageTemplate);
        if (msg != null) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            reply.setContent(String.valueOf(new Random().nextInt(3))); // TODO set correct time

            myAgent.send(reply);
        } else {
            block();
        }
    }
}
