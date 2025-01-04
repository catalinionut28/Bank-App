package org.poo.graph;

public class Node {
    private final String currency;
    private final double rate;

    public Node(String currency, double rate) {
        this.currency = currency;
        this.rate = rate;
    }
    public double getRate() {
        return rate;
    }

    public String getCurrency() {
        return currency;
    }

}
