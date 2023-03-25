package org.hse.bse.agents;

import static jade.util.ObjectManager.AGENT_TYPE;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class ChefAgent extends Agent {
<<<<<<< HEAD
    private int chef_type_id;
    private String chef_type_name;
    private boolean used = false;

    @Override
    protected void setup() {
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(AGENT_TYPE);
        serviceDescription.setName("bob");
=======
  private int chef_type_id;
  private String chef_type_name;
  private boolean used = true;
>>>>>>> b96d1af1cc162b5369a26dfd4831489c7c6e717e

  @Override
  protected void setup() {
    DFAgentDescription agentDescription = new DFAgentDescription();
    agentDescription.setName(getAID());
    ServiceDescription serviceDescription = new ServiceDescription();
    serviceDescription.setType(AGENT_TYPE);
    serviceDescription.setName("bob");

    agentDescription.addServices(serviceDescription);

<<<<<<< HEAD
        System.out.println("Init chef " + getAID().getName() + "");
        addBehaviour(
                new CyclicBehaviour(this) {
                    @Override
                    public void action() {
                        ACLMessage msg = myAgent.receive();
                        if (msg != null) {
                            String title = msg.getContent();
                            if (title.equals("using")) {
                                if (used) {
                                    System.out.println(getName() + " is already used!");
                                } else {
                                    used = true;
                                    System.out.println(getName() + " reserved by " + msg.getSender().getName());
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
=======
    try {
      DFService.register(this, agentDescription);
    } catch (FIPAException ex) {
      ex.printStackTrace();
    }

    System.out.println("Init chef " + getAID().getName() + "");
    addBehaviour(
        new CyclicBehaviour(this) {
          @Override
          public void action() {
            MessageTemplate messageTemplate =
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(messageTemplate);
            if (msg != null) {
              String title = msg.getContent();

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
>>>>>>> b96d1af1cc162b5369a26dfd4831489c7c6e717e

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

    System.out.println("Terminate chef: " + getAID().getName());
  }
}
