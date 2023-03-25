package org.hse.bse.configuration;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.hse.bse.configuration.JadeAgent;

import javax.swing.*;

@JadeAgent(number = 5)
public class TestAgentReceiver extends Agent {
  private static int counter = 0;
  private int currentNumber;
  private int health;

  public TestAgentReceiver() {
    currentNumber = ++counter;
    health = 100;
  }

  @Override
  protected void setup() {
    addBehaviour(
        new CyclicBehaviour() {
          @Override
          public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
              JOptionPane.showMessageDialog(null, "Content: " + msg.getContent());
            } else {
              block();
            }
          }
        });
  }

  public int getHealth() {
    return health;
  }

  public String value() {
    return "lol";
  }

  public int number() {
    return currentNumber;
  }
}
