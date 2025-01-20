package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Dao;
import org.poo.bank.DeleteError;
import org.poo.bank.User;

class DeleteAccount implements Command {
    private final User user;
    private final String iban;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    DeleteAccount(final User user,
                  final String iban,
                  final int timestamp,
                  final ObjectMapper objectMapper,
                  final ArrayNode output) {
        this.user = user;
        this.iban = iban;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to print all users,
     * their accounts, and associated cards to the output.
     * This method iterates over all users retrieved from the
     * {@code userDao}.
     * For each user, it creates a structured
     * JSON object that includes the user's first name,
     * last name, email, and for each of their accounts,
     * it includes the account's IBAN,
     * balance, currency, type, and associated cards.
     * The resulting data is added to the output.
     * The final output is formatted in
     * a structured way and includes a timestamp for the execution.
     */
    public void execute() {
        try {
            Dao accountData = user.getAccountDao();
            Account account = (Account) accountData.get(iban);
            if (account.getBalance() > 0) {
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("command", "deleteAccount");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("error",
                        "Account couldn't be deleted - see org.poo.transactions for details");
                outputNode.put("timestamp", timestamp);
                errorNode.set("output", outputNode);
                errorNode.put("timestamp", timestamp);
                output.add(errorNode);
                user.getTransactions().add(new DeleteError(timestamp,
                        "Account couldn't be deleted - "
                                + "there are funds remaining"));
                return;
            }
            accountData.delete(iban);
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "deleteAccount");
            ObjectNode successNode = objectMapper.createObjectNode();
            successNode.put("success", "Account deleted");
            successNode.put("timestamp", timestamp);
            outputNode.set("output", successNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}
