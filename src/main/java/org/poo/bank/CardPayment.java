package org.poo.bank;

public class CardPayment extends Transaction {
    private String commerciant;
    private double amount;

    public CardPayment(int timestamp,
                       String commerciant,
                       double amount) {
        this.setTimestamp(timestamp);
        this.setDescription("Card payment");
        this.setType("CardPayment");
        this.commerciant = commerciant;
        this.amount = amount;

    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public double getAmount() {
        return amount;
    }

    public String getCommerciant() {
        return commerciant;
    }
}
