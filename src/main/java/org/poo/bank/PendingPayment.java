package org.poo.bank;
public class PendingPayment {
    private double amount;
    private int timestamp;
    private Account accountInvolved;
    private boolean accepted;
    private boolean rejected;

    private MultiplePayment splitPayment;

    public PendingPayment(final double amount,
                          final String currency,
                          final int timestamp,
                          final Account account,
                          final MultiplePayment splitPayment) {
        this.amount = amount;
        this.timestamp = timestamp;
        this.accountInvolved = account;
        this.accepted = false;
        this.rejected = false;
        this.splitPayment = splitPayment;
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
     * Retrieves the account involved in the transaction.
     * <p>
     * This method returns the {@code Account}
     * object associated with the current transaction.
     * </p>
     *
     * @return the {@code Account} involved in the transaction
     */
    public Account getAccountInvolved() {
        return accountInvolved;
    }

    /**
     * Retrieves the timestamp of the transaction.
     * <p>
     * This method returns the {@code timestamp}
     * value associated with the current transaction.
     * </p>
     *
     * @return the timestamp of the transaction
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Checks if the transaction has been accepted.
     * <p>
     * This method returns {@code true}
     * if the transaction is accepted, {@code false} otherwise.
     * </p>
     *
     * @return {@code true} if the transaction is accepted,
     * {@code false} otherwise
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Checks if the transaction has been rejected.
     * <p>
     * This method returns {@code true}
     * if the transaction is rejected, {@code false} otherwise.
     * </p>
     *
     * @return {@code true} if the transaction is rejected,
     * {@code false} otherwise
     */
    public boolean isRejected() {
        return rejected;
    }

    /**
     * Retrieves the split payment details.
     * <p>
     * This method returns the {@code MultiplePayment}
     * object associated with the current transaction
     * if the payment is split.
     * </p>
     *
     * @return the {@code MultiplePayment}
     * associated with the transaction, or {@code null} if not applicable
     */
    public MultiplePayment getSplitPayment() {
        return splitPayment;
    }

    /**
     * Marks the transaction as rejected.
     * <p>
     * This method sets the {@code rejected}
     * flag to {@code true}, indicating the transaction has
     * been rejected.
     * </p>
     */
    public void reject() {
        rejected = true;
    }

    /**
     * Marks the transaction as accepted.
     * <p>
     * This method sets the {@code accepted}
     * flag to {@code true}, indicating the transaction has
     * been accepted.
     * </p>
     */
    public void accept() {
        accepted = true;
    }
}
