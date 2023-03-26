package org.hse.bse.agents.process;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.hse.bse.TimeMarker;

import java.time.Duration;
import java.time.LocalDateTime;

public class ProcessTimer extends CyclicBehaviour {
    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(messageTemplate);
        if (msg != null) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            if (TimeMarker.operationTime.containsKey(getAgent().getAID().getName())) {
                LocalDateTime dend = TimeMarker.operationTime.get(getAgent().getAID().getName());
                Long ost = Duration.between(LocalDateTime.now(), dend).getSeconds();
                reply.setContent(ost.toString());
                myAgent.send(reply);
            } else {
                reply.setContent("0");
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
