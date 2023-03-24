package org.hse.bse.behaviour;

import com.fasterxml.jackson.core.JsonProcessingException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.hse.bse.model.Person;
import org.hse.bse.util.JsonMessage;

public class SendMessageBehaviour extends Behaviour {

    private final Person message;
    private final AID[] recipients;

    public SendMessageBehaviour(AID[] recipients, Person message) {
        this.recipients = recipients;
        this.message = message;
    }

    @Override
    public void action() {
        JsonMessage cfp = new JsonMessage(ACLMessage.CFP);
        for (AID recipient : recipients) {
            cfp.addReceiver(recipient);
        }
        try {
            cfp.setContent(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(cfp);
    }

    @Override
    public boolean done() {
        return true;
    }
}