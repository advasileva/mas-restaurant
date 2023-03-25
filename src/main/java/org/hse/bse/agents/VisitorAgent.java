package org.hse.bse.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.*;
import java.util.logging.Logger;

import jade.lang.acl.MessageTemplate;
import org.hse.bse.configuration.JadeAgent;

@JadeAgent(number = 5)
public class VisitorAgent extends Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

    private List<AID> sellerAgents = new ArrayList<>();

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    DFAgentDescription template = new DFAgentDescription();
    ServiceDescription serviceDescription = new ServiceDescription();

    serviceDescription.setType(ManagerAgent.AGENT_TYPE);
    template.addServices(serviceDescription);

      try {
          DFAgentDescription[] result = DFService.search(this, template);

          for (DFAgentDescription agentDescription: result) {
              sellerAgents.add(agentDescription.getName());
          }
      } catch (FIPAException ex) {
          ex.printStackTrace();
      }

    addBehaviour(
        new Behaviour() {
          private AID bestSeller;
          private int bestPrice;
          private int repliesCount = 0;
          private MessageTemplate messageTemplate;
          private int step = 0;

          private static final String CONVERSATION_ID = "book-trade";
          @Override
          public void action() {
            log.info("Start action");
            switch (step) {
              case 0:
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                  for (AID agent : sellerAgents) {
                      cfpMessage.addReceiver(agent);
                  }

                cfpMessage.setContent("test");
                cfpMessage.setConversationId(CONVERSATION_ID);
                cfpMessage.setReplyWith("cfp" + System.currentTimeMillis());

                messageTemplate = MessageTemplate.and(
                        MessageTemplate.MatchConversationId(CONVERSATION_ID),
                        MessageTemplate.MatchInReplyTo(cfpMessage.getReplyWith()));

                step = 1;
                break;
              case 1:
                ACLMessage reply = myAgent.receive(messageTemplate);
                if (reply != null) {
                  if (reply.getPerformative() == ACLMessage.PROPOSE) {
                    int price = Integer.parseInt(reply.getContent());
                    if (bestSeller == null || price < bestPrice) {
                      bestPrice = price;
                      bestSeller = reply.getSender();
                    }
                  }

                  ++repliesCount;

                  step = 2;
                } else {
                  block();
                }
                break;
            }
          }

          @Override
          public boolean done() {
            return step == 2;
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
