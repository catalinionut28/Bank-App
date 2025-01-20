package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.CardCreation;
import org.poo.bank.User;

class CreateOneTimeCard implements Command {
    private User user;
    private Account account;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;


    CreateOneTimeCard(final User user,
                      final Account account,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        if (account == null) {
            return;
        }
        account.createOneTimeCard();
        CardCreation transaction = new CardCreation(timestamp,
                account.getCards().getLast().getCardNumber(),
                user.getEmail(),
                account.getIban());
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}
