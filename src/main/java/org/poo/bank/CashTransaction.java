package org.poo.bank;

public class CashTransaction extends Transaction {
    private final double ronAmount;


    public CashTransaction(final int timestamp,
                           final double ronAmount) {
        this.ronAmount = ronAmount;
        this.setTimestamp(timestamp);
        this.setType("CashTransaction");
        this.setDescription(createDescription(ronAmount));
    }

    /**
     * Creates a description for a cash withdrawal.
     * <p>
     * This method generates a string description
     * for a cash withdrawal, formatted to one decimal
     * place, using the specified {@code ronAmount}.
     * </p>
     *
     * @param ronAmount the amount of money withdrawn in RON
     * @return a string description of the cash withdrawal
     */
    private String createDescription(final double ronAmount) {
        return "Cash withdrawal of " + String.format("%.1f", ronAmount);
    }

    /**
     * Retrieves the amount in RON.
     * <p>
     * This method returns the {@code ronAmount}
     * value associated with the current instance.
     * </p>
     *
     * @return the amount in RON
     *//**
     * Retrieves the amount in RON.
     * <p>
     * This method returns the {@code ronAmount}
     * value associated with the current instance.
     * </p>
     *
     * @return the amount in RON
     */
    public double getRonAmount() {
        return ronAmount;
    }
}
