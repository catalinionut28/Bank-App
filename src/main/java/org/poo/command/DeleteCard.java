package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Card;
import org.poo.bank.CardDestruction;
import org.poo.bank.User;

class DeleteCard implements Command {
    private User user;
    private Account account;
    private String cardNumber;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    DeleteCard(final User user,
               final Account account,
               final String cardNumber,
               final int timestamp,
               final ObjectMapper objectMapper,
               final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to delete the
     * specified card from the user's account and records the transaction.
     * This method searches for the card with the given
     * card number in the specified account's card list.
     * If the card is found, it is removed from the account,
     * and a {@link CardDestruction} transaction is created.
     * This transaction is then added to both
     * the user's and the account's transaction histories.
     */
    @Override
    public void execute() {
        if (account == null) {
            return;
        }
        Card card = null;
        for (Card c : account.getCards()) {
            if (c.getCardNumber().equals(cardNumber)) {
                card = c;
            }
        }
        account.getCards().remove(card);
        CardDestruction destruction = new CardDestruction(timestamp,
                cardNumber, user.getEmail(), account.getIban());
        user.getTransactions().add(destruction);
        account.getTransactionHistory().add(destruction);
    }
}
