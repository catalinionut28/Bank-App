package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
class SetMinimumBalance implements Command {
    private Account account;
    private final double amount;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    SetMinimumBalance(final Account account,
                      final double amount,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode output) {
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to set the minimum balance for the specified account.
     * This method sets the specified
     * amount as the minimum balance requirement for the account
     * using setMinimumBalance(double).
     */
    public void execute() {
        if (account != null) {
            account.setMinimumBalance(amount);
        }
    }
}
