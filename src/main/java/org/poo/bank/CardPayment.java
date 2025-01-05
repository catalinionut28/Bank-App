package org.poo.bank;

public class CardPayment extends Transaction {
    private String commerciant;
    private double amount;

    public CardPayment(final int timestamp,
                       final String commerciant,
                       final double amount) {
        this.setTimestamp(timestamp);
        this.setDescription("Card payment");
        this.setType("CardPayment");
        this.commerciant = commerciant;
        this.amount = amount;

    }

    /**
     * Sets the transaction amount.
     *
     * @param amount a double representing the transaction amount.
     */

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * Sets the name of the merchant involved in the transaction.
     *
     * @param commerciant a String representing the merchant's name.
     */

    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }

    /**
     * Retrieves the transaction amount.
     *
     * @return a double representing the transaction amount.
     */

    public double getAmount() {
        return amount;
    }

    /**
     * Retrieves the name of the merchant involved in the transaction.
     *
     * @return a String representing the merchant's name.
     */

    public String getCommerciant() {
        return commerciant;
    }
}
