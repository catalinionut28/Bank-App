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

    /**
     * Adds a pending payment to the list of pending payments.
     * <p>
     * This method adds the specified
     * {@code PendingPayment} object
     * to the {@code pendingPayments} list.
     * </p>
     *
     * @param p the {@code PendingPayment} to add
     */
    public void addPayment(final PendingPayment p) {
        pendingPayments.add(p);
    }

    /**
     * Retrieves the list of users.
     * <p>
     * This method returns the {@code ArrayList<User>}
     * representing the users associated with the current instance.
     * </p>
     *
     * @return an {@code ArrayList<User>} containing the users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Retrieves the type.
     * <p>
     * This method returns the {@code type}
     * value associated with the current instance.
     * </p>
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the list of pending payments.
     * <p>
     * This method returns the {@code ArrayList<PendingPayment>}
     * representing the pending payments.
     * </p>
     *
     * @return an {@code ArrayList<PendingPayment>}
     * containing the pending payments
     */
    public ArrayList<PendingPayment> getPendingPayments() {
        return pendingPayments;
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

    /**
     * Retrieves the timestamp.
     * <p>
     * This method returns the {@code timestamp}
     * value associated with the current instance.
     * </p>
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return timestamp;
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
     * Retrieves the list of original amounts.
     * <p>
     * This method returns an {@code ArrayList<Double>}
     * representing the original amounts associated with the current instance.
     * </p>
     *
     * @return an {@code ArrayList<Double>} containing the original amounts
     */
    public ArrayList<Double> getOriginalAmounts() {
        return originalAmounts;
    }
}
