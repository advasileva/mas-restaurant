package org.hse.bse.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.logging.Logger;

public class OrderAgent extends Agent {
  public static int count = 0;
  private final Logger log = Logger.getLogger(this.getClass().getName());

  public static final String AGENT_TYPE = "order";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    //    DFAgentDescription agentDescription = new DFAgentDescription();
    //    agentDescription.setName(getAID());
    //
    //    ServiceDescription serviceDescription = new ServiceDescription();
    //    serviceDescription.setType(AGENT_TYPE);
    //    serviceDescription.setName("JADE-order");
    //
    //    agentDescription.addServices(serviceDescription);
    //
    //    try {
    //      DFService.register(this, agentDescription);
    //    } catch (FIPAException ex) {
    //      ex.printStackTrace();
    //    }
    //
    //    addBehaviour(
    //        new CyclicBehaviour() {
    //          @Override
    //          public void action() {
    //            MessageTemplate messageTemplate =
    // MessageTemplate.MatchPerformative(ACLMessage.CFP);
    //            ACLMessage msg = myAgent.receive(messageTemplate);
    //
    //            if (msg != null) {
    //              ACLMessage reply = msg.createReply();
    //              reply.setPerformative(ACLMessage.PROPOSE);
    //              reply.setContent(DataProvider.read(Data.menuDishes));
    //
    //              myAgent.send(reply);
    //            } else {
    //              block();
    //            }
    //          }
    //        });
    //    addBehaviour(
    //        new CyclicBehaviour() {
    //          @Override
    //          public void action() {
    //            MessageTemplate messageTemplate =
    //                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
    //            ACLMessage msg = myAgent.receive(messageTemplate);
    //
    //            if (msg != null) {
    //              String dishes = msg.getContent();
    //              ACLMessage reply = msg.createReply();
    //              reply.setPerformative(ACLMessage.INFORM);
    //
    //              log.info(String.format("Received order for dishes: %s", dishes));
    //
    //              myAgent.send(reply);
    //            } else {
    //              block();
    //            }
    //          }
    //        });
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
