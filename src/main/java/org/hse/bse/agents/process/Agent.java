package org.hse.bse.agents.process;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;
import java.util.logging.Logger;

public class Agent extends jade.core.Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());
  private boolean used = true;

  public static final String AGENT_TYPE = "process";

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

    addBehaviour(
        new CyclicBehaviour() {
          @Override
          public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(messageTemplate);

            if (msg != null) {
              ACLMessage reply = msg.createReply();
              reply.setPerformative(ACLMessage.PROPOSE);
              reply.setContent(String.valueOf(new Random().nextInt(10))); // TODO set correct time

              myAgent.send(reply);
            } else {
              block();
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

    System.out.println("Terminate cooking process: " + getAID().getName());
  }
}
