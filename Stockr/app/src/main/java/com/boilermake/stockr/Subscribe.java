package com.boilermake.stockr;

import java.io.Serializable;

/**
 * Created by ilee on 10/18/14.
 */
public class Subscribe implements Serializable {
    int id;
    int type;
    Double value;
    String symbol;
    int association;
    int timewindow;

    public Subscribe(int id, int type, Double value, String symbol, int association, int timewindow) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.symbol = symbol;
        this.association = association;
        this.timewindow = timewindow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getAssociation() {
        return association;
    }

    public void setAssociation(int association) {
        this.association = association;
    }

    public int getTimewindow() {
        return timewindow;
    }

    public void setTimewindow(int timewindow) {
        this.timewindow = timewindow;
    }
}
