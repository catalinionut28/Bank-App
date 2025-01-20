package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.User;

class AddAccount implements Command {

    private final User user;
    private final String accountType;
    private final String currency;
    private final double interestRate;
    private final int timestamp;
    private String error;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    AddAccount(final User user,
               final String accountType,
               final String currency,
               final double interestRate,
               final int timestamp,
               final ObjectMapper objectMapper,
               final ArrayNode arrayNode) {
        this.user = user;
        this.accountType = accountType;
        this.currency = currency;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = arrayNode;
        this.interestRate = interestRate;
        this.error = null;
    }

    public void execute() {
        if (user == null) {
            error = "The user doesn't exist";
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "addAccount");
            errorNode.put("error", error);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        if (accountType.equals("classic")) {
            user.createClassicAccount(currency, timestamp);
        } else {
            user.createSavingsAccount(currency, timestamp, interestRate);
        }
    }
}
