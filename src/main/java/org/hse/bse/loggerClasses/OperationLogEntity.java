package org.hse.bse.loggerClasses;

import java.time.LocalDateTime;

public class OperationLogEntity {
    public int oper_id;
    public int oper_proc;
    public int oper_card;
    public LocalDateTime oper_started;
    public LocalDateTime oper_ended;
    public String oper_equip_id;
    public String oper_cooker_id;
    public boolean oper_active;

    private static int operCounter = 0;

    public OperationLogEntity(
            int currentOperProc,
            int currentOperCard,
            LocalDateTime started,
            String currentEquipID,
            String currentCookerID) {
        oper_id = operCounter++;
        oper_proc = currentOperProc;
        oper_card = currentOperCard;
        oper_started = started;
        oper_ended = LocalDateTime.now();
        oper_equip_id = currentEquipID;
        oper_cooker_id = currentCookerID;
        oper_active = false;
    }
}
