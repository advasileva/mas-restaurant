package org.hse.bse.agents.process;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ProcessInitBehaviour extends OneShotBehaviour {
  @Override
  public void action() {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setContent("using");
    msg.addReceiver(new AID("bob", AID.ISLOCALNAME));
    getAgent().send(msg);

    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
    msg2.setContent("using");
    msg2.addReceiver(new AID("knife", AID.ISLOCALNAME));
    getAgent().send(msg2);
  }
}
