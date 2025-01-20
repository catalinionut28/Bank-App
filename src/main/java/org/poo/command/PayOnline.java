package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.commerciants.Merchant;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;
import org.poo.plan.SilverPlan;
import org.poo.plan.StandardPlan;

class PayOnline implements Command {
    private User user;
    private Account account;
    private String cardNumber;
    private double amount;
    private String currency;
    private int timestamp;
    private Merchant commerciant;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    PayOnline(final User user,
              final Account account,
              final String cardNumber,
              final double amount,
              final String currency,
              final int timestamp,
              final CurrencyGraph graph,
              final Merchant commerciant,
              final ObjectMapper objectMapper,
              final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.graph = graph;
        this.commerciant = commerciant;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the "payOnline" command for a given account and card.
     * This method checks if the provided account
     * is valid and if the card is active.
     * It processes
     * an online payment by verifying the card's status,
     * account balance, and currency compatibility.
     * It handles cases for insufficient funds, frozen cards,
     * and currency conversions if necessary.
     * Transactions are logged in the user's and account's transaction history.
     */
    public void execute() {
        if (account == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "payOnline");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", timestamp);
            errorNode.put("description", "Card not found");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        if (amount == 0) {
            return;
        }
        Card lastCard = account.getCards().getLast();
        if (account.getCard(cardNumber).getStatus().equals("frozen")) {
            FrozenPayment frozenPayment = new FrozenPayment(timestamp,
                    "The card is frozen");
            user.getTransactions().add(frozenPayment);
            account.getTransactionHistory().add(frozenPayment);
            return;
        }
        double ronAmount = graph.exchange(new Node(currency, 1),
                new Node("RON", 1),
                amount);
        double commission;
        if (account.getCurrency().equals(currency)) {
            commission = getCommission(amount, ronAmount);
            if (account.getBalance() < amount + commission) {
                InsufficientFunds insufficientFunds =
                        new InsufficientFunds(timestamp);
                user.getTransactions().add(insufficientFunds);
                account.getTransactionHistory().add(insufficientFunds);
                return;
            }
            CardPayment transaction =
                    new CardPayment(timestamp, commerciant.getName(), amount);
            account.payOnline(amount, cardNumber, commerciant, ronAmount);
            account.setBalance(account.getBalance() - commission);
            user.getTransactions().add(transaction);
            account.getTransactionHistory().add(transaction);
            if (account.getType().equals("classic")) {
                ((ClassicAccount) account).getSpendingsReport().add(transaction);
            }
        } else {
            double newAmount = graph.exchange(new Node(currency, 1.0),
                    new Node(account.getCurrency(), 1),
                    amount);
            commission = getCommission(newAmount, ronAmount);
            if (account.getBalance() < newAmount + commission) {
                InsufficientFunds insufficientFunds =
                        new InsufficientFunds(timestamp);
                user.getTransactions().add(insufficientFunds);
                account.getTransactionHistory().add(insufficientFunds);
                return;
            }
            CardPayment transaction =
                    new CardPayment(timestamp, commerciant.getName(), newAmount);
            account.payOnline(newAmount, cardNumber, commerciant, ronAmount);
            user.getTransactions().add(transaction);
            account.getTransactionHistory().add(transaction);
            account.setBalance(account.getBalance() - commission);
            if (account.getType().equals("classic")) {
                ((ClassicAccount) account).getSpendingsReport().add(transaction);
            }
        }
        if (!lastCard.equals(account.getCards().getLast())) {
            CardCreation cardCreation = new CardCreation(timestamp,
                    account.getCards().getLast().getCardNumber(),
                    user.getEmail(), account.getIban());
            CardDestruction cardDestruction =
                    new CardDestruction(timestamp,
                            cardNumber,
                            user.getEmail(),
                            account.getIban());
            user.getTransactions().add(cardDestruction);
            account.getTransactionHistory().add(cardDestruction);
            user.getTransactions().add(cardCreation);
            account.getTransactionHistory().add(cardCreation);
        }
    }

    /**
     * Calculates the commission for a given withdrawal amount based on
     * the user's plan type.
     *
     * Depending on whether the user has a "standard" or "silver" plan,
     * the method calculates the commission differently. For a "standard" plan,
     * it uses a method from `StandardPlan` to calculate the commission.
     * For a "silver" plan, it uses the `SilverPlan` method to calculate
     * the commission considering both the withdrawal amount and its
     * equivalent in RON.
     *
     * @param amount      The amount being withdrawn.
     * @param ronAmount  The amount being withdrawn in RON.
     * @return           The commission for the given withdrawal amount.
     */
    private double getCommission(final double amount,
                                 final double ronAmount) {
        double commission = 0;
        switch (user.getPlan().getType()) {
            case "standard":
                commission = ((StandardPlan) user
                        .getPlan())
                        .calculateCommission(amount);
                break;
            case "silver":
                commission = ((SilverPlan) user.getPlan())
                        .calculateCommission(amount, ronAmount);
                break;
            default:
                break;
        }
        return commission;
    }
}
