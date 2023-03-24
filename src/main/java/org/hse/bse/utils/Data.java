package org.hse.bse.utils;

public enum Data {
    cookers("cookers.json"),
    dishCards("dish_cards.json"),
    equipment("equipment.json"),
    equipmentType("equipment_type.json"),
    menuDishes("menu_dishes.json"),
    operationLog("operation_log.json"),
    operationTypes("operation_types.json"),
    processLog("process_log.json"),
    productTypes("product_types.json"),
    products("products.json"),
    visitorsOrders("visitors_orders.json");

    private String filename;
    Data(String filename) {
       this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }
}
