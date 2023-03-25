package org.hse.bse;

import jade.core.*;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.text.MessageFormat;
import org.hse.bse.agents.manager.ManagerAgent;

public class MainController {

  private static ContainerController containerController;

  public MainController() {
    final Runtime rt = Runtime.instance();
    final Profile p = new ProfileImpl();

    p.setParameter(Profile.MAIN_HOST, "localhost");
    p.setParameter(Profile.MAIN_PORT, "8081");
    p.setParameter(Profile.GUI, "true");

    containerController = rt.createMainContainer(p);
  }

  void start() {
    addAgent(ManagerAgent.class, "", null);
  }

  public static String addAgent(Class<?> clazz, String suffix, Object[] args) {
    try {
      AgentController agent =
          containerController.createNewAgent(
              MessageFormat.format("{0}{1}", clazz.getSimpleName(), suffix), clazz.getName(), args);
      agent.start();
      return agent.getName();
    } catch (StaleProxyException ex) {
      ex.printStackTrace(); // I prefer ff
    }
    return "";
  }

  public static void registerService(jade.core.Agent agent, String type) {
    DFAgentDescription agentDescription = new DFAgentDescription();
    agentDescription.setName(agent.getAID());

    ServiceDescription serviceDescription = new ServiceDescription();
    serviceDescription.setType(type);
    serviceDescription.setName(agent.getName());

    agentDescription.addServices(serviceDescription);

    try {
      DFService.register(agent, agentDescription);
    } catch (FIPAException ex) {
      ex.printStackTrace();
    }
  }
}
