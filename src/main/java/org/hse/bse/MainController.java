package org.hse.bse;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.text.MessageFormat;
import java.util.Random;
import org.hse.bse.agents.ManagerAgent;

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
    addAgents(ManagerAgent.class, 1);
  }

  public static void addAgents(Class<?> clazz, int number) {
    for (int i = 0; i < number; ++i) {
      try {
        containerController
            .createNewAgent(
                MessageFormat.format(
                    "{0}{1}", clazz.getSimpleName(), new Random().nextInt(1000)),
                clazz.getName(),
                null)
            .start(); // TODO fix ids
      } catch (StaleProxyException ex) {
        ex.printStackTrace();
      }
    }
    ;
  }
}
