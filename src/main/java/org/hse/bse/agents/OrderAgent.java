package org.hse.bse.agents;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.utils.DataProvider;

public class OrderAgent extends Agent {
  public static int count = 0;
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, String> processes = new HashMap<>();
  public static final String AGENT_TYPE = "order";

  public static AID aid;

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    Object[] args = getArguments();
    if (args != null && args.length > 0) {
      initProcesses();

      //      addBehaviour(new TickerBehaviour(this, 60000) {
      //        @Override
      //        protected void onTick() {
      //          DFAgentDescription template = new DFAgentDescription();
      //          ServiceDescription serviceDescription = new ServiceDescription();
      //
      //          serviceDescription.setType(BookSellerAgent.AGENT_TYPE);
      //          template.addServices(serviceDescription);
      //          try {
      //            DFAgentDescription[] result = DFService.search(myAgent, template);
      //
      //            for (DFAgentDescription agentDescription: result) {
      //              sellerAgents.add(agentDescription.getName());
      //            }
      //          } catch (FIPAException ex) {
      //            ex.printStackTrace();
      //          }
      //
      //          myAgent.addBehaviour(new RequestPerformer());
      //        }
      //      });
    }

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
    //        addBehaviour(
    //            new CyclicBehaviour() {
    //              @Override
    //              public void action() {
    //                MessageTemplate messageTemplate =
    //                    MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
    //                ACLMessage msg = myAgent.receive(messageTemplate);
    //
    //                if (msg != null) {
    //                  String dishes = msg.getContent();
    //                  ACLMessage reply = msg.createReply();
    //                  reply.setPerformative(ACLMessage.INFORM);
    //
    //                  log.info(String.format("Received order for dishes: %s", dishes));
    //
    //                  myAgent.send(reply);
    //                } else {
    //                  block();
    //                }
    //              }
    //            });
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

  private void initProcesses() {
    JsonArray dishes =
        DataProvider.parse(getArguments()[0].toString()).getAsJsonArray("vis_ord_dishes");
    for (JsonElement dish : dishes) {
      String dishId = ((JsonObject) dish).get("ord_dish_id").getAsString();
      if (!processes.containsKey(dishId)) {
        log.info(String.format("Add dish with id %s", dishId));
        processes.put(
            dishId,
            MainController.addAgent(CookingProcessAgent.class, dishId, new Object[] {dish}));
      }
    }
  }
}
