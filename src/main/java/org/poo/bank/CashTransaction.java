package org.poo.bank;

public class CashTransaction extends Transaction {
    private double ronAmount;


    public CashTransaction(final int timestamp,
                           final double ronAmount) {
        this.ronAmount = ronAmount;
        this.setTimestamp(timestamp);
        this.setType("CashTransaction");
        this.setDescription(createDescription(ronAmount));
    }

    private String createDescription(double ronAmount) {
        return "Cash withdrawal of " + String.format("%.1f", ronAmount);
    }

    public double getRonAmount() {
        return ronAmount;
    }
}
