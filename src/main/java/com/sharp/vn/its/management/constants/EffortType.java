package com.sharp.vn.its.management.constants;

public enum EffortType {

    //AMS
    AMS(1),

    //Support, Trouble shoot, Monitor, Q&A
    SUPPORT_TROUBLE_MONITOR_QA(2),

    //OJT, Transfer
    OJT_TRANSFER(3);

    private final int id;

    EffortType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
