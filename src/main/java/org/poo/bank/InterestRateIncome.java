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

    /**
     * Retrieves the amount.
     * <p>
     * This method returns the {@code amount}
     * value associated with the current instance.
     * </p>
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Retrieves the currency.
     * <p>
     * This method returns the {@code currency}
     * value associated with the current instance.
     * </p>
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }
}
