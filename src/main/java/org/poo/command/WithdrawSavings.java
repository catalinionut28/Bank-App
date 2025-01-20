package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;

class WithdrawSavings implements Command {
    private User user;
    private Account savingsAccount;
    private Account account;
    private double amount;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;
    private final int MINIMUM_AGE = 21;

    WithdrawSavings(final User user,
                    final Account savingsAccount,
                    final Account account,
                    final double amount,
                    final int timestamp,
                    final ObjectMapper objectMapper,
                    final ArrayNode output) {
        this.user = user;
        this.savingsAccount = savingsAccount;
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the withdrawal from the savings account to the classic
     * account. It checks whether the savings account exists, if the
     * account type is correct, if there are sufficient funds, and if
     * the user meets the minimum age requirement.
     * If all conditions are met, the withdrawal is processed.
     */
    @Override
    public void execute() {
        if (savingsAccount == null) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "Account not found"));
            return;
        }
        if (!savingsAccount.getType().equals("savings")) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "Account is not of type savings"));
            return;
        }
        if (savingsAccount.getBalance() <= 0) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "Insufficient funds"));
        }
        if (account == null) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "You do not have a classic account."));
            return;
        }
        if (user.getAge() < MINIMUM_AGE) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "You don't have the minimum age required."));
            return;
        }
        ((SavingsAccount) savingsAccount)
                .withdraw(((ClassicAccount) account), amount);
        user.getTransactions().add(new SavingsWithdrawn(timestamp,
                "Savings withdrawal"));
    }
}
