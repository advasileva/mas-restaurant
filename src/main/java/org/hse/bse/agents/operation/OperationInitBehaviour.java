package org.hse.bse.agents.operation;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.hse.bse.agents.manager.ManagerAgent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OperationInitBehaviour extends OneShotBehaviour {
  private class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
      return s1.length() - s2.length();
    }
  }

  @Override
  public void action() {
    LocalDateTime begin = LocalDateTime.now();
    List<String> keys = new ArrayList<String>(ManagerAgent.usedEquipment.keySet());
    Collections.sort(keys, new OperationInitBehaviour.LengthComparator());
    for (String key: keys) {
      if (getAgent().getAID().getName().contains(key + "_")) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(ManagerAgent.waitingTime.get(key).toString());
        msg.addReceiver(new AID(ManagerAgent.equipments.get(ManagerAgent.usedEquipment.get(key))));
        getAgent().send(msg);
        break;
      }
    }

    /*OperationLog.operation_log.add(new OperationLogEntity(
            1, 1, begin, Integer.getInteger(usedEquipmentId),
    )*/

    /*try {
      Thread.sleep(50);
    } catch (InterruptedException ignored) { }*/

    /*for (String key: keys) {
      if (getAgent().getAID().getName().contains(key.toString() + "_")) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("not using");
        msg.addReceiver(new AID(ManagerAgent.equipments.get(key), AID.ISLOCALNAME));
        getAgent().send(msg);
        break;
      }
    }*/

    /*ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
    msg2.setContent("using");
    msg2.addReceiver(new AID("knife", AID.ISLOCALNAME));
    getAgent().send(msg2);*/
  }
}
