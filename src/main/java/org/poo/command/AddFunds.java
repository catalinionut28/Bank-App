package org.poo.command;

import org.poo.bank.Account;

class AddFunds implements Command {
    private final Account account;
    private final double amount;

    AddFunds(final Account account,
             final double amount) {
        this.account = account;
        this.amount = amount;
    }

    /**
     * Executes the command to add funds to the specified account.
     * This method checks if the account is valid (non-null).
     * If the account exists,
     * the specified amount will be added
     * to the account's balance using the addFunds(double) method.
     */
    public void execute() {
        if (account == null) {
            return;
        }
        account.addFunds(amount);
    }
}
