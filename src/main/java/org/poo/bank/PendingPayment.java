package org.poo.bank;

import org.poo.bank.Account;
import org.poo.bank.User;

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

    public double getAmount() {
        return amount;
    }
    public Account getAccountInvolved() {
        return accountInvolved;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isRejected() {
        return rejected;
    }

    public MultiplePayment getSplitPayment() {
        return splitPayment;
    }

    public void reject() {
        rejected = true;
    }

    public void accept() {
        accepted = true;
    }
}
