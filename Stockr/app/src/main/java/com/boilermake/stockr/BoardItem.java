package com.boilermake.stockr;

/**
 * Created by ilee on 10/19/14.
 */
public class BoardItem {
    String symbol;
    int type;
    double value;
    long timestamp;
    int association;
    String[] types = {"Instant", "Spatial", "Time"};
    String[] associations = {"Greater", "Lesser"};

    public BoardItem(String symbol, int type, double value, long timestamp, int association) {
        this.symbol = symbol;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
        this.association = association;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return types[type];
    }

    public double getValue() {
        return value;
    }
    public String getAssociation() {
        return associations[association];
    }

    public long getTimestamp() {
        return timestamp;
    }
}
