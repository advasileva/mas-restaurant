package org.hse.bse.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.hse.bse.configuration.JadeAgent;

@JadeAgent(value="visitor", number = 5)
public class VisitorAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("init visitor.");
        addBehaviour(
            new OneShotBehaviour() {
                @Override
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent("Order 1");
                    msg.addReceiver(new AID("ManagerAgent", AID.ISLOCALNAME));
                    send(msg);
                }
        });
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Buyer-agent terminating.");
    }
}
