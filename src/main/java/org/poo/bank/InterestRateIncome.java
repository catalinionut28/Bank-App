package org.poo.bank;

public class InterestRateIncome extends Transaction {
    private double amount;
    private String currency;

    public InterestRateIncome(final double amount,
                              final String currency,
                              final int timestamp) {
        this.amount = amount;
        this.currency = currency;
        this.setTimestamp(timestamp);
        this.setType("InterestRateIncome");
        this.setDescription("Interest rate income");
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
