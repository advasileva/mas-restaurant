package org.hse.bse.agents.manager;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

public class MenuProvider extends CyclicBehaviour {
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
}
