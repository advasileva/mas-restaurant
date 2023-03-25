package org.hse.bse.agents;

import static jade.util.ObjectManager.AGENT_TYPE;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Logger;
import org.hse.bse.configuration.JadeAgent;

@JadeAgent(number = 1)
public class ManagerAgent extends Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  public static final String AGENT_TYPE = "manager";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    this.aid = getAID();

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

    addBehaviour(
        new CyclicBehaviour(this) {
          @Override
          public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(messageTemplate);
//            log.info("Got msg " + msg);

            if (msg != null) {
              String title = msg.getContent();
              log.info("Got request " + title);
              ACLMessage reply = msg.createReply();

              Integer price = 1;
              if (price != null) {
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent("not-available");
              }

              myAgent.send(reply);
            } else {
//              block();
            }
          }
        });
  }

  @Override
  protected void takeDown() {
    try {
      DFService.deregister(this);
    } catch (FIPAException exc) {
      exc.printStackTrace();
    }

    log.info(String.format("Terminate %s", getAID().getName()));
  }
}
