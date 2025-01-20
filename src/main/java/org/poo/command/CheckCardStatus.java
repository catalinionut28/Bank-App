package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Card;
import org.poo.bank.FrozenPayment;
import org.poo.bank.User;

class CheckCardStatus implements Command {
    private User user;
    private Card card;
    private Account account;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;
    private final int warningLimit = 30;

    CheckCardStatus(final User user,
                    final Card card,
                    final Account account,
                    final int timestamp,
                    final ObjectMapper objectMapper,
                    final ArrayNode output) {
        this.account = account;
        this.user = user;
        this.card = card;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the "checkCardStatus" command
     * by checking the status of the card.
     * The method checks the account
     * balance to determine whether
     * the card should be frozen or
     * issued a warning.
     * If the account balance is
     * less than or equal to the minimum balance,
     * the card status is set to "frozen".
     * If the balance is within a warning threshold, the card
     * status is set to "warning".
     * If the user or card is null,
     * an error response is returned.
     */
    public void execute() {
        if (user == null || card == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "checkCardStatus");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");
            errorNode.set("output", outputNode);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        if (account.getBalance() <= account.getMinimumBalance()) {
            card.setStatus("frozen");
            FrozenPayment frozenPayment = new FrozenPayment(timestamp,
                    "You have reached the minimum amount of funds, the card will be frozen");
            user.getTransactions().add(frozenPayment);
            account.getTransactionHistory().add(frozenPayment);
            return;
        }
        if (account.getBalance() <= account.getMinimumBalance() + warningLimit) {
            card.setStatus("warning");
        }
    }
}
