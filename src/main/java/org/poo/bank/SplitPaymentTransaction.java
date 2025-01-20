package org.poo.bank;

import java.util.ArrayList;

public class SplitPaymentTransaction extends Transaction {
    private final String currency;
    private final double amount;
    private ArrayList<String> involvedAccounts;
    private ArrayList<Double> amountForUsers;
    private final String error;
    private final String splitType;

    public SplitPaymentTransaction(final int timestamp,
                                   final String description,
                                   final String splitType,
                                   final String currency,
                                   final double amount,
                                   final ArrayList<String> involvedAccounts,
                                   final ArrayList<Double> amountForUsers,
                                   final String error) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("SplitPayment");
        this.currency = currency;
        this.splitType = splitType;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.error = error;
        this.amountForUsers = amountForUsers;
    }

    /**
     * Gets the amount involved in the split payment transaction.
     *
     * @return the amount of the transaction.
     */
    public double getAmount() {
        return amount;
    }

    public String getSplitType() {
        return splitType;
    }

    public ArrayList<Double> getAmountForUsers() {
        return amountForUsers;
    }

    /**
     * Gets the currency used in the split payment transaction.
     *
     * @return the currency used in the transaction.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Gets the list of account identifiers involved in the split payment.
     *
     * @return a list of account identifiers involved in the transaction.
     */
    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     * Gets any error message related to the split payment transaction.
     *
     * @return the error message, or {@code null} if no error occurred.
     */
    public String getError() {
        return error;
    }
}
