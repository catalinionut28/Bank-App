package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.InterestRateIncome;
import org.poo.bank.SavingsAccount;

class AddInterest implements Command {
    private final Account account;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    AddInterest(final Account account,
                final int timestamp,
                final ObjectMapper objectMapper,
                final ArrayNode output) {
        this.account = account;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to add interest to a savings account.
     * This method first checks if the provided `account` is of type "savings".
     * If the account is
     * not a savings account,
     * an error message is created and added to the output indicating that
     * the operation is not supported for non-savings accounts.
     * If the account is a savings account,
     * the method calls the `addInterestRate()`
     * method on the `SavingsAccount`
     * to apply the interest
     * rate changes.
     */
    @Override
    public void execute() {
        if (account == null) {
            return;
        }
        if (!account.getType().equals("savings")) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "addInterest");
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
        double income = savingsAccount.addInterestRate();
        account.getUserTransactions().add(new InterestRateIncome(income,
                account.getCurrency(), timestamp));
    }
}
