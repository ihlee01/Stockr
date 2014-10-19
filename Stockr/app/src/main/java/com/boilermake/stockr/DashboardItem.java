package com.boilermake.stockr;

import java.io.Serializable;

/**
 * Created by Yongsun on 10/18/14.
 */
public class DashboardItem implements Serializable {
    private String jsonArray;
    private double value;
    private int subId;
    private String msg;
    private int type;
    private int association;
    private int timewindow;

    public DashboardItem(String jsonArray, double value, int subId, String msg, int type, int association, int timewindow) {
        this.jsonArray = jsonArray;
        this.value = value;
        this.subId = subId;
        this.msg = msg;
        this.type = type;
        this.association = association;
        this.timewindow = timewindow;
    }

    public String getInformation() {
        return "Test Fuck you";
    }

    public String getMessage() {
        return msg;
    }
}
