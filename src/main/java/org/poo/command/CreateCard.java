package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.CardCreation;
import org.poo.bank.User;

class CreateCard implements Command {
    private User user;
    private final Account account;
    private final int timestamp;
    private String error;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    CreateCard(final User user,
               final Account account,
               final int timestamp,
               final ObjectMapper objectMapper,
               final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
        this.error = null;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to add a new account for the user.
     * This method creates either a classic
     * or savings account based on the provided {@code accountType}.
     * If the user does not exist, an error message is added to the output.
     */
    public void execute() {
        if (user == null || account == null) {
            return;
        }
        account.createCard();
        CardCreation transaction = new CardCreation(timestamp,
                account.getCards().getLast().getCardNumber(),
                user.getEmail(),
                account.getIban());
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}
