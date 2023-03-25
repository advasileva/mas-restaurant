package org.hse.bse.agents;

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
import org.hse.bse.MainController;
import org.hse.bse.configuration.JadeAgent;
import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

@JadeAgent(number = 1)
public class ManagerAgent extends Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  public static final String AGENT_TYPE = "manager";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    this.aid = getAID();

    initAgents();

    DFAgentDescription agentDescription = new DFAgentDescription();
    agentDescription.setName(getAID());

    ServiceDescription serviceDescription = new ServiceDescription();
    serviceDescription.setType(AGENT_TYPE);
    serviceDescription.setName("JADE-order");

    agentDescription.addServices(serviceDescription);

    try {
      DFService.register(this, agentDescription);
    } catch (FIPAException ex) {
      ex.printStackTrace();
    }

    addBehaviour(
        new CyclicBehaviour() {
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
        });
    addBehaviour(
        new CyclicBehaviour() {
          @Override
          public void action() {
            MessageTemplate messageTemplate =
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(messageTemplate);

            if (msg != null) {
              String dishes = msg.getContent();
              ACLMessage reply = msg.createReply();
              reply.setPerformative(ACLMessage.INFORM);

              log.info(String.format("Received order for dishes: %s", dishes));

              myAgent.send(reply);

              MainController.addAgents(OrderAgent.class, 1);
            } else {
              block();
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

  private void initAgents() {
    MainController.addAgents(VisitorAgent.class, 5);
  }
}
