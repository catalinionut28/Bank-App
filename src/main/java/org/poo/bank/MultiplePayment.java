package org.poo.bank;

import java.util.ArrayList;

public class MultiplePayment {
    private ArrayList<User> users;
    private ArrayList<PendingPayment> pendingPayments;
    private double amount;
    private String currency;
    private int timestamp;
    private ArrayList<Double> originalAmounts;
    private String type;

    public MultiplePayment(final double amount,
                           final String currency,
                           final int timestamp,
                           final String type,
                           final ArrayList<Double> originalAmounts,
                           final ArrayList<User> users) {
        this.pendingPayments = new ArrayList<>();
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.type = type;
        this.originalAmounts = originalAmounts;
        this.users = users;
    }

    public void addPayment(PendingPayment p) {
        pendingPayments.add(p);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getType() {
        return type;
    }

    public ArrayList<PendingPayment> getPendingPayments() {
        return pendingPayments;
    }

    public String getCurrency() {
        return currency;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public ArrayList<Double> getOriginalAmounts() {
        return originalAmounts;
    }
}
