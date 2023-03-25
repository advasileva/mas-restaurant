package org.hse.bse.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.hse.bse.configuration.JadeAgent;

@JadeAgent(value="visitor", number = 5)
public class VisitorAgent extends Agent {
    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("test-squad");
        sd.setName("JADE-test");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
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
