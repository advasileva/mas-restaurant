package org.hse.bse.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import static jade.util.ObjectManager.AGENT_TYPE;

public class CookingProcessAgent extends Agent {
    private boolean used = true;

    @Override
    protected void setup() {
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(AGENT_TYPE);
        serviceDescription.setName("knife");

        agentDescription.addServices(serviceDescription);

        try {
            DFService.register(this, agentDescription);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

        System.out.println("Init cooking process " + getAID().getName() + "");
        addBehaviour(
                new OneShotBehaviour(this) {
                    @Override
                    public void action() {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContent("using");
                        msg.addReceiver(new AID("bob", AID.ISLOCALNAME));
                        send(msg);

                        ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                        msg2.setContent("using");
                        msg2.addReceiver(new AID("knife", AID.ISLOCALNAME));
                        send(msg2);
                    }
                });
    }

    public void setUp() {
        setup();
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        System.out.println("Terminate cooking process: " + getAID().getName());
    }
}
