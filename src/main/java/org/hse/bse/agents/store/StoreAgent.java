package org.hse.bse.agents.store;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;

import org.hse.bse.MainController;
import org.hse.bse.utils.DataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class StoreAgent extends jade.core.Agent {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final Map<String, JsonObject> productTypes = new HashMap<>();

    private final Map<String, JsonObject> products = new HashMap<>();

    public static final String AGENT_TYPE = "store";

    public static AID aid;

    @Override
    protected void setup() {
        log.info(String.format("Init %s", getAID().getName()));

        aid = getAID();
        MainController.registerService(this, AGENT_TYPE);

        initProductTypes();
        initProducts();

        addBehaviour(new ProductDistributor(products));
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
            String productTypeId = productType.getAsJsonObject().get("prod_type_id").getAsString();
            productTypes.put(productTypeId, productType.getAsJsonObject());
        }
        log.info(String.format("Added %d product types", productTypesJson.size()));
    }

    private void initProducts() {
        JsonArray productJson =
                DataProvider.parse(getArguments()[1].toString()).getAsJsonArray("products");
        for (JsonElement product : productJson) {
            String productId = product.getAsJsonObject().get("prod_item_type").getAsString();
            products.put(productId, product.getAsJsonObject());
        }
        log.info(String.format("Added %d products", productJson.size()));
    }
}
