package org.hse.bse.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.logging.Logger;

public class DataChecker {
    private static final Logger log = Logger.getLogger(DataChecker.class.getName());

    private static final JsonArray errors = new JsonArray();

    public static void check(Data type, String content) {
        log.info(String.format("Checking %s", type.getFilename()));
        JsonObject parsed = DataProvider.parse(content);
        switch (type) {
            case cookers:
                wrapper(checkCookers(parsed), type);
                break;
            case dishCards:
                wrapper(checkDishCards(parsed), type);
                break;
            case equipment:
                wrapper(checkEquipment(parsed), type);
                break;
            case equipmentType:
                wrapper(checkEquipmentType(parsed), type);
                break;
            case menuDishes:
                wrapper(checkMenuDishes(parsed), type);
                break;
            case operationTypes:
                wrapper(checkOperationTypes(parsed), type);
                break;
            case productTypes:
                wrapper(checkProductTypes(parsed), type);
                break;
            case products:
                wrapper(checkProducts(parsed), type);
                break;
            case visitorsOrders:
                wrapper(checkVisitorsOrders(parsed), type);
                break;
            default:
                log.warning(String.format("Unrecognized data type %s", type.getFilename()));
        }
    }

    private static void wrapper(String field, Data data) {
        if (field != null) {
            JsonObject error = new JsonObject();
            error.addProperty("err_type", "input_error");
            error.addProperty("err_entity", data.getFilename());
            error.addProperty("err_field", field);
            errors.add(error);
            JsonObject wrap = new JsonObject();
            wrap.add("errors", errors);
            DataProvider.write(Data.errors, wrap.toString());
        }
    }

    private static String checkFields(JsonObject json, List<String> fieds) {
        for (String field : fieds) {
            if (!json.has(field) || json.get(field).isJsonNull()) {
                return field;
            }
        }
        return null;
    }

    private static String checkCookers(JsonObject content) {
        String field = "cookers";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("cook_id", "cook_name", "cook_active"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkDishCards(JsonObject content) {
        String field = "dish_cards";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            JsonObject json = jsonElement.getAsJsonObject();
            String result = checkFields(json, List.of("card_id", "card_time", "operations"));
            if (result != null) {
                return result;
            }
            for (JsonElement jsonElement2 : json.getAsJsonArray("operations")) {
                result =
                        checkFields(
                                jsonElement2.getAsJsonObject(),
                                List.of("oper_type", "equip_type", "oper_time", "oper_products"));
                if (result != null) {
                    return result;
                }
                for (JsonElement jsonElement3 : jsonElement2.getAsJsonObject().getAsJsonArray("oper_products")) {
                    result =
                            checkFields(
                                    jsonElement3.getAsJsonObject(),
                                    List.of("prod_type", "prod_quantity"));
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private static String checkEquipment(JsonObject content) {
        String field = "equipment";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("equip_id", "equip_type", "equip_name", "equip_active"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkEquipmentType(JsonObject content) {
        String field = "equipment_type";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("equip_type_id", "equip_type_name"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkMenuDishes(JsonObject content) {
        String field = "menu_dishes";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("menu_dish_id", "menu_dish_card", "menu_dish_price", "menu_dish_active"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkOperationTypes(JsonObject content) {
        String field = "operation_types";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("oper_type_id", "oper_type_name"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkProductTypes(JsonObject content) {
        String field = "product_types";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("prod_type_id", "prod_type_name", "prod_is_food"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkProducts(JsonObject content) {
        String field = "products";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            String result =
                    checkFields(
                            jsonElement.getAsJsonObject(),
                            List.of("prod_item_id", "prod_item_type", "prod_item_name", "prod_item_quantity"));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String checkVisitorsOrders(JsonObject content) {
        String field = "visitors_orders";
        if (!content.has(field)) {
            return field;
        }
        for (JsonElement jsonElement : content.getAsJsonArray(field)) {
            JsonObject json = jsonElement.getAsJsonObject();
            String result = checkFields(json, List.of("vis_ord_started", "vis_ord_total", "vis_ord_dishes"));
            if (result != null) {
                return result;
            }
            for (JsonElement jsonElement2 : json.getAsJsonArray("vis_ord_dishes")) {
                result =
                        checkFields(
                                jsonElement2.getAsJsonObject(),
                                List.of("ord_dish_id", "menu_dish"));
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
