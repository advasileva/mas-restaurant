package org.hse.bse.agents.process;

import com.google.gson.JsonElement;
import jade.core.behaviours.OneShotBehaviour;
import org.hse.bse.MainController;
import org.hse.bse.agents.manager.ManagerAgent;
import org.hse.bse.agents.operation.OperationAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateDishBehaviour extends OneShotBehaviour {
    private class LengthComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }

    @Override
    public void action() {
        List<String> keys = new ArrayList<String>(ManagerAgent.dishes.keySet());
        Collections.sort(keys, new LengthComparator());
        for (String key: keys) {
            if (getAgent().getAID().getName().contains(key)) {
                for (JsonElement je : ManagerAgent.dishes.get(key).get("operations").getAsJsonArray()) {
                    MainController.addAgent(OperationAgent.class,
                            je.getAsJsonObject().get("equip_type").toString(),
                            new Object[]{});
                }
                break;
            }
        }
    }
}
