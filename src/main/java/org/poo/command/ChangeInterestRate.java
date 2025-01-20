package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.InterestChanged;
import org.poo.bank.SavingsAccount;

class ChangeInterestRate implements Command {
    private Account account;
    private double interestRate;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    ChangeInterestRate(final Account account,
                       final double interestRate,
                       final int timestamp,
                       final ObjectMapper objectMapper,
                       final ArrayNode output) {
        this.account = account;
        this.interestRate = interestRate;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to change
     * the interest rate for a savings account.
     * This method first checks whether the provided account
     * is of type "savings". If the account is
     * not a savings account,
     * an error message is returned
     * indicating that the operation is not
     * supported for non-savings accounts.
     * If the account is a savings account,
     * the interest rate is updated to the provided value.
     * A transaction of type `InterestChanged`
     * is created and recorded in both the account's
     * transaction history and report.
     */
    public void execute() {
        if (account == null) {
            return;
        }
        if (!account.getType().equals("savings")) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "changeInterestRate");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description",
                    "This is not a savings account");
            outputNode.put("timestamp", timestamp);
            errorNode.set("output", outputNode);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        SavingsAccount savingsAccount = (SavingsAccount) account;
        savingsAccount.setInterestRate(interestRate);
        InterestChanged interestChanged =
                new InterestChanged(timestamp,
                        interestRate);
        savingsAccount.getReport().add(interestChanged);
        savingsAccount.getUserTransactions()
                .add(interestChanged);
    }
}
