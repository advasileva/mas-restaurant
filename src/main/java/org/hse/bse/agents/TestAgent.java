package org.hse.bse.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.hse.bse.configuration.JadeAgent;

public class TestAgent extends Agent {
  private static int counter = 0;
  private int currentNumber;
  private int health;

  public TestAgent() {
    currentNumber = ++counter;
    System.out.println("init test.");
    health = 100;
  }
  @Override
  protected void setup() {

    System.out.println("init test.");
    addBehaviour(
            new OneShotBehaviour() {
              @Override
              public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("test Message");
                msg.addReceiver(new AID("TestAgentReceiver", AID.ISLOCALNAME));
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
}