package org.hse.bse.agents.store;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.hse.bse.utils.Data;
import org.hse.bse.utils.DataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProductDistributor extends CyclicBehaviour {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final Map<String, JsonObject> products;

    private final Map<String, JsonArray> operationsByDish = new HashMap<>();

    ProductDistributor(Map<String, JsonObject> products) {
        this.products = products;
        parseOperations();
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(messageTemplate);

        if (msg != null) {
            JsonArray dishes =
                    DataProvider.parse(msg.getContent()).getAsJsonArray("vis_ord_dishes");

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);

            if (isAvailable(dishes)) {
                reply.setContent("available");
                discard(dishes);
            } else {
                reply.setContent("not enough");
            }

            myAgent.send(reply);
        } else {
            block();
        }
    }

    private void parseOperations() {
        JsonArray menuDishes =
                DataProvider.readAsJson(Data.menuDishes).get("menu_dishes").getAsJsonArray();
        JsonArray dishCards =
                DataProvider.readAsJson(Data.dishCards).get("dish_cards").getAsJsonArray();
        for (JsonElement dish : menuDishes) {
            for (JsonElement card : dishCards) {
                if (dish.getAsJsonObject().get("menu_dish_card").getAsInt()
                        == card.getAsJsonObject().get("card_id").getAsInt()) {
                    operationsByDish.put(
                            dish.getAsJsonObject().get("menu_dish_id").getAsString(),
                            card.getAsJsonObject().get("operations").getAsJsonArray());
                    break;
                }
            }
        }
    }

    private boolean isAvailable(JsonArray dishes) {
        for (JsonElement dish : dishes) {
            JsonArray operations =
                    operationsByDish.get(
                            String.valueOf(dish.getAsJsonObject().get("menu_dish").getAsInt()));
            for (JsonElement operation : operations) { // TODO There is bug for same dishes in order
                for (JsonElement product :
                        operation.getAsJsonObject().get("oper_products").getAsJsonArray()) {
                    if (product.getAsJsonObject().get("prod_quantity").getAsDouble()
                            > products.get(product.getAsJsonObject().get("prod_type").getAsString())
                                    .get("prod_item_quantity")
                                    .getAsDouble()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void discard(JsonArray dishes) {
        for (JsonElement dish : dishes) {
            JsonArray operations =
                    operationsByDish.get(
                            String.valueOf(dish.getAsJsonObject().get("menu_dish").getAsInt()));
            for (JsonElement operation : operations) {
                for (JsonElement product :
                        operation.getAsJsonObject().get("oper_products").getAsJsonArray()) {
                    JsonObject prod =
                            products.get(
                                    product.getAsJsonObject()
                                            .get("prod_type")
                                            .getAsString()); // упал
                    double curr = prod.get("prod_item_quantity").getAsDouble();
                    prod.addProperty(
                            "prod_item_quantity",
                            String.valueOf(
                                    curr
                                            - product.getAsJsonObject()
                                                    .get("prod_quantity")
                                                    .getAsDouble()));
                    log.info(
                            String.format(
                                    "Write-off product %s, current amount is %s",
                                    prod.get("prod_item_name"), prod.get("prod_item_quantity")));
                }
            }
        }
    }
}
