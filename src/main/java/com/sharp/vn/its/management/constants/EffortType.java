package com.sharp.vn.its.management.constants;

public enum EffortType {

    //AMS
    AMS(1),

    //Support, Trouble shoot, Monitor, Q&A
    TYPES(2);

    private final int id;

    EffortType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
