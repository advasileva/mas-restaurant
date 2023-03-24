package org.hse.bse.configuration;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.annotation.Annotation;

@JadeAgent(number = 5)
public class TestAgent extends Agent implements JadeAgent {
  private static int counter = 0;
  private int currentNumber;
  private int health;

  public TestAgent() {
    currentNumber = ++counter;
    health = 100;
  }

  @Override
  protected void setup() {
    addBehaviour(new OneShotBehaviour() {
      @Override
      public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("test Message");
        msg.addReceiver(new AID("TestAgent", AID.ISLOCALNAME));
        send(msg);
      }
    });
  }

  public int getHealth() {
    return health;
  }

  public void kick(TestAgent agent) {
    agent.health -= 5;
  }

  public String value() {
    return "lol";
  }

  public int number() {
    return currentNumber;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return null;
  }
}
