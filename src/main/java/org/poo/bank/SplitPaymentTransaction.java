package org.poo.bank;

import java.util.ArrayList;

public class SplitPaymentTransaction extends Transaction {
    private final String currency;
    private final double amount;
    ArrayList<String> involvedAccounts;

    public SplitPaymentTransaction(int timestamp,
                                   String description,
                                   String currency,
                                   double amount,
                                   ArrayList<String> involvedAccounts) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("SplitPayment");
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }
}
