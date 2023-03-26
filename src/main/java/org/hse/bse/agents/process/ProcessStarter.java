package org.hse.bse.agents.process;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import org.hse.bse.agents.manager.ManagerAgent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProcessStarter extends OneShotBehaviour {
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
        Collections.sort(keys, new ProcessStarter.LengthComparator());
        for (String key : keys) {
            if (getAgent().getAID().getName().contains(key + "_")) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent(ManagerAgent.waitingTime.get(key).toString());
                msg.addReceiver(
                        new AID(ManagerAgent.equipments.get(ManagerAgent.usedEquipment.get(key))));
                getAgent().send(msg);
                break;
            }
        }
    }
}
