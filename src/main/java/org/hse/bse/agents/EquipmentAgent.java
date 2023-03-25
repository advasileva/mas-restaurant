package org.hse.bse.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static jade.util.ObjectManager.AGENT_TYPE;

public class EquipmentAgent extends Agent {
    private int equip_type_id;
    private String equip_type_name;
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

        System.out.println("Init equipment " + getAID().getName() + "");
        addBehaviour(
                new CyclicBehaviour(this) {
                    @Override
                    public void action() {
                        MessageTemplate messageTemplate =
                                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        ACLMessage msg = myAgent.receive(messageTemplate);
                        if (msg != null) {
                            String title = msg.getContent();

                            System.out.println("title: " + title);
                            if (title == "using") {
                                if (used) {
                                    System.out.println(getName() + " is already used!");
                                } else {
                                    used = true;
                                    System.out.println(title + " reserved by " + msg.getSender().getName());
                                }
                            } else {
                                used = false;
                            }

                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                        }
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

        System.out.println("Terminate equipment: " + getAID().getName());
    }
}
