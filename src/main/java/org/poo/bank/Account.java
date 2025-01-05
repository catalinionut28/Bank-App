package org.poo.bank;

import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;

import java.util.ArrayList;

public abstract class Account implements DaoObject {
    private String iban;
    private ArrayList<Card> cards;
    private double balance;
    private double minimumBalance;
    private String currency;
    private String type;
    private ArrayList<Transaction> userTransactions;
    private ArrayList<Transaction> transactionHistory;

    /**
     * Retrieves the transaction history.
     *
     * @return an ArrayList of Transaction objects representing the transaction history.
     */

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Sets the transaction history.
     *
     * @param transactionHistory an ArrayList
     *                           of Transaction objects
     *                           representing the transaction history.
     */

    public void setTransactionHistory(final ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    /**
     * Retrieves the list of cards associated with the account.
     *
     * @return an ArrayList of Card objects.
     */

    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Retrieves the account balance.
     *
     * @return the current balance as a double.
     */

    public double getBalance() {
        return balance;
    }

    /**
     * Retrieves the account currency.
     *
     * @return a String representing the currency type.
     */

    public String getCurrency() {
        return currency;
    }

    /**
     * Retrieves the IBAN of the account.
     *
     * @return a String representing the IBAN.
     */

    public String getIban() {
        return iban;
    }

    /**
     * Retrieves the account type.
     *
     * @return a String representing the account type.
     */

    public String getType() {
        return type;
    }

    /**
     * Updates the account balance.
     *
     * @param balance the new balance as a double.
     */

    public void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     * Sets the list of cards associated with the account.
     *
     * @param cards an ArrayList of Card objects.
     */

    public void setCards(final ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Sets the currency of the account.
     *
     * @param currency a String representing the currency.
     */

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Sets the IBAN of the account.
     *
     * @param iban a String representing the IBAN.
     */

    public void setIban(final String iban) {
        this.iban = iban;
    }

    /**
     * Sets the account type.
     *
     * @param type a String representing the account type.
     */

    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Sets the list of user transactions.
     *
     * @param userTransactions an ArrayList of Transaction objects.
     */

    public void setUserTransactions(final ArrayList<Transaction> userTransactions) {
        this.userTransactions = userTransactions;
    }

    /**
     * Retrieves the list of user transactions.
     *
     * @return an ArrayList of Transaction objects.
     */

    public ArrayList<Transaction> getUserTransactions() {
        return userTransactions;
    }

    /**
     * Adds funds to the account.
     *
     * @param amount the amount to be added as a double.
     */

    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     * Creates a new card and adds it to the list of cards.
     */

    public void createCard() {
        Card card = new Card();
        cards.add(card);
    }

    /**
     * Retrieves the account identifier (IBAN).
     *
     * @return a String representing the IBAN.
     */

    public String getIdentifier() {
        return iban;
    }

    /**
     * Creates a one-time use card and adds it to the list of cards.
     */

    public void createOneTimeCard() {
        cards.add(new OneTimeCard());
    }

    /**
     * Sets the minimum balance for the account.
     *
     * @param amount the minimum balance as a double.
     */

    public void setMinimumBalance(final double amount) {
        minimumBalance = amount;
    }

    /**
     * Retrieves the minimum balance of the account.
     *
     * @return the minimum balance as a double.
     */

    public double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * Retrieves a card by its card number.
     *
     * @param cardNumber a String representing the card number.
     * @return the corresponding Card object, or null if not found.
     */

    public Card getCard(final String cardNumber) {
        for (Card card: cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Makes an online payment using a specified card.
     *
     * @param amount the amount to be paid as a double.
     * @param cardNumber a String representing the card number used for payment.
     */

    public void payOnline(final double amount, final String cardNumber) {
        balance -= amount;
        Card card = getCard(cardNumber);
        if (card.isOneTimeCard()) {
            cards.remove(card);
            createOneTimeCard();
        }
    }

    /**
     * Splits a payment and deducts the specified amount from the account balance.
     *
     * @param amount the amount to be deducted as a double.
     */

    public void splitPay(final double amount) {
        balance -= amount;
    }

    /**
     * Receives money and adds it to the account balance.
     *
     * @param amount the amount to be added as a double.
     */

    public void receiveMoney(final double amount) {
        balance += amount;
    }

    /**
     * Sends money to another account, processes the transaction,
     * and records the transaction history.
     *
     * @param receiver the receiving Account object.
     * @param amount the amount to be sent as a double.
     * @param exchangeGraph the CurrencyGraph object for currency conversion.
     * @param timestamp the transaction timestamp as an int.
     * @param description a String describing the transaction.
     */

    public void sendMoney(final Account receiver,
                          double amount,
                          final CurrencyGraph exchangeGraph,
                          final int timestamp,
                          final String description) {
        this.balance -= amount;
        String formattedAmount = String.valueOf(amount)
                                + " " + currency;
        SendReceive transaction = new SendReceive(timestamp,
                                                description,
                                                iban,
                                                receiver.getIban(),
                                                formattedAmount,
                                                "sent");
        userTransactions.add(transaction);
        transactionHistory.add(transaction);

        if (this.currency.equals(receiver.getCurrency())) {
            receiver.receiveMoney(amount);
            formattedAmount = String.valueOf(amount)
                    + " " + receiver.getCurrency();
            SendReceive receiveTransaction =
                    new SendReceive(timestamp,
                                    description,
                                    iban,
                                    receiver.getIban(),
                                    formattedAmount,
                            "received");
            receiver.getUserTransactions()
                    .add(receiveTransaction);
            receiver.getTransactionHistory()
                    .add(receiveTransaction);
        } else {
            amount = exchangeGraph.exchange(
                    new Node(this.currency, 1),
                    new Node(receiver.getCurrency(), 1),
                    amount);
            formattedAmount = String.valueOf(amount)
                    + " " + receiver.getCurrency();
            SendReceive receiveTransaction =
                    new SendReceive(timestamp,
                            description,
                            iban,
                            receiver.getIban(),
                            formattedAmount,
                            "received");
            receiver.getUserTransactions()
                    .add(receiveTransaction);
            receiver.getTransactionHistory()
                    .add(receiveTransaction);
            receiver.receiveMoney(amount);
        }
    }
}


