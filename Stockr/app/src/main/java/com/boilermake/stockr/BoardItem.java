package com.boilermake.stockr;

import java.io.Serializable;

/**
 * Created by ilee on 10/19/14.
 */
public class BoardItem implements Serializable {
    String symbol;
    int type;
    double value;
    long timestamp;
    int association;
    double original_value;

    String[] types = {"Instant", "Spatial", "Time"};
    String[] associations = {"Greater", "Lesser"};

    public BoardItem(String symbol, int type, double value, long timestamp, int association, double original_value) {
        this.symbol = symbol;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
        this.association = association;
        this.original_value = original_value;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return types[type-1];
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

    public double getOriginal_value() {
        return original_value;
    }
}
