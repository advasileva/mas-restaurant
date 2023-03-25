package org.hse.bse.agents;

import static jade.util.ObjectManager.AGENT_TYPE;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.hse.bse.configuration.JadeAgent;

@JadeAgent(number = 1)
public class ManagerAgent extends Agent {

  @Override
  protected void setup() {

    DFAgentDescription agentDescription = new DFAgentDescription();
    agentDescription.setName(getAID());

    ServiceDescription serviceDescription = new ServiceDescription();
    serviceDescription.setType(AGENT_TYPE);
    serviceDescription.setName("JADE-book-trading");

    agentDescription.addServices(serviceDescription);

    try {
      DFService.register(this, agentDescription);
    } catch (FIPAException ex) {
      ex.printStackTrace();
    }

    System.out.println("Init manager " + getAID().getName() + "");
    addBehaviour(
        new CyclicBehaviour(this) {
          @Override
          public void action() {
            MessageTemplate messageTemplate =
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(messageTemplate);
            if (msg != null) {
              String title = msg.getContent();
              ACLMessage reply = msg.createReply();

              reply.setPerformative(ACLMessage.INFORM);
              System.out.println(title + " sold to agent " + msg.getSender().getName());
            }
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

    System.out.println("Terminate manager: " + getAID().getName());
  }
}
