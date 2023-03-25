package org.hse.bse.agents.store;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.hse.bse.MainController;
import org.hse.bse.utils.DataProvider;

public class Agent extends jade.core.Agent {
  private final Logger log = Logger.getLogger(this.getClass().getName());

  private final Map<String, JsonObject> productTypes = new HashMap<>();
  private final Map<String, JsonObject> products = new HashMap<>();
  public static final String AGENT_TYPE = "store";

  @Override
  protected void setup() {
    log.info(String.format("Init %s", getAID().getName()));

    MainController.registerService(this, AGENT_TYPE);

    initProductTypes();
    initProducts();

    //    addBehaviour(null); // TODO add
  }

  @Override
  protected void takeDown() {
    try {
      DFService.deregister(this);
    } catch (FIPAException exc) {
      exc.printStackTrace();
    }

    log.info(String.format("Terminate %s", getAID().getName()));
  }

  private void initProductTypes() {
    JsonArray productTypesJson =
        DataProvider.parse(getArguments()[0].toString()).getAsJsonArray("product_types");
    for (JsonElement productType : productTypesJson) {
      String productTypeId = ((JsonObject) productType).get("prod_type_id").getAsString();
      productTypes.put(productTypeId, (JsonObject) productType);
    }
    log.info(String.format("Added %d product types", productTypesJson.size()));
  }

  private void initProducts() {
    JsonArray productJson =
        DataProvider.parse(getArguments()[1].toString()).getAsJsonArray("products");
    for (JsonElement product : productJson) {
      String productId = ((JsonObject) product).get("prod_item_id").getAsString();
      products.put(productId, (JsonObject) product);
    }
    log.info(String.format("Added %d products", productJson.size()));
  }
}
