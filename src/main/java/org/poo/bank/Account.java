package org.poo.bank;

import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;
import org.poo.utils.Utils;

import java.util.ArrayList;

public abstract class Account implements DaoObject {
    private String IBAN;
    private ArrayList<Card> cards;
    private double balance;
    private double minimumBalance;
    private String currency;
    private String type;
    private ArrayList<Transaction> userTransactions;
    private ArrayList<Transaction> transactionHistory;

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getType() {
        return type;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserTransactions(ArrayList<Transaction> userTransactions) {
        this.userTransactions = userTransactions;
    }

    public ArrayList<Transaction> getUserTransactions() {
        return userTransactions;
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public void createCard() {
        Card card = new Card();
        cards.add(card);
    }

    public String getIdentifier() {
        return IBAN;
    }

    public void createOneTimeCard() {
        cards.add(new OneTimeCard());
    }

    public void setMinimumBalance(double amount) {
        minimumBalance = amount;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public Card getCard(String cardNumber) {
        for (Card card: cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    public void payOnline(double amount) {
        balance -= amount;
    }

    public void splitPay(double amount) {
        balance -= amount;
    }

    public void receiveMoney(double amount) {
        balance += amount;
    }

    public void sendMoney(Account receiver, double amount,
                          CurrencyGraph exchangeGraph,
                          int timestamp,
                          String description) {
        this.balance -= amount;
        String formattedAmount = String.valueOf(amount)
                                + " " + currency;
        SendReceive transaction = new SendReceive(timestamp,
                                                description,
                                                IBAN,
                                                receiver.getIBAN(),
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
                                    IBAN,
                                    receiver.getIBAN(),
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
                            IBAN,
                            receiver.getIBAN(),
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


class ClassicAccount extends Account {

    public ClassicAccount(String currency,
                          ArrayList<Transaction> transactions) {
        this.setBalance(0);
        this.setCards(new ArrayList<>());
        this.setIBAN(Utils.generateIBAN());
        this.setType("classic");
        this.setCurrency(currency);
        this.setUserTransactions(transactions);
        this.setTransactionHistory(new ArrayList<Transaction>());
    }
}
class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency,
                          ArrayList<Transaction> transactions) {
        this.setBalance(0);
        this.setCards(new ArrayList<>());
        this.setIBAN(Utils.generateIBAN());
        this.setType("savings");
        this.setCurrency(currency);
        this.setUserTransactions(transactions);
        this.setTransactionHistory(new ArrayList<>());
        this.interestRate = 0;
    }
}
