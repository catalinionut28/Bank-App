package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.SplitPaymentTransaction;
import org.poo.bank.PendingPayment;
import org.poo.bank.User;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;

import java.util.ArrayList;

class SplitPayment implements Command {
    private ArrayList<User> users;
    private ArrayList<Account> accounts;
    private double amount;
    private String currency;
    private int timestamp;
    private ArrayList<Double> amounts;
    private String type;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    SplitPayment(final ArrayList<User> users,
                 final ArrayList<Account> accounts,
                 final double amount,
                 final String type,
                 final String currency,
                 final int timestamp,
                 final ArrayList<Double> amounts,
                 final CurrencyGraph graph,
                 final ObjectMapper objectMapper,
                 final ArrayNode output) {
        this.users = users;
        this.accounts = accounts;
        this.amounts = amounts;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.graph = graph;
        this.objectMapper = objectMapper;
        this.output = output;
        this.type = type;
    }

    /**
     * Command implementation for performing a split payment among multiple accounts.
     * <p>
     * This class is responsible for dividing
     * the total amount of a payment among a list of accounts.
     * Each account's balance is checked to ensure
     * sufficient funds are available for its portion of
     * the split. If any account has insufficient
     * funds, the split payment fails and an error message is
     * generated. If all accounts have enough funds,
     * the payment is processed and a transaction record
     * is created for each account involved in the split.
     * </p>
     */
    public void execute() {
        double[] amountsExchanged = new double[accounts.size()];
        ArrayList<String> involvedAccounts = new ArrayList<>();
        String errorMessage = null;
        String error = null;
        for (int i = accounts.size() - 1; i >= 0; i--) {
            Account account = accounts.get(i);
            if (account == null) {
                return;
            }
            amountsExchanged[i] = graph.exchange(new Node(currency, 1),
                    new Node(account.getCurrency(), 1),
                    amount / accounts.size());
            if (account.getBalance() < amountsExchanged[i]) {
                errorMessage = "Account "
                        + account.getIban()
                        + " has insufficient funds "
                        + "for a split payment.";
                if (error == null) {
                    error = errorMessage;
                }
            }
            involvedAccounts.add(account.getIban());
        }
        String description = "Split payment of "
                + String.format("%.2f", amount)
                + " "
                + currency;
        if (error != null) {
            SplitPaymentTransaction err =
                    new SplitPaymentTransaction(timestamp,
                            description, currency,
                            amount / accounts.size(),
                            involvedAccounts, error);
            for (Account account: accounts) {
                account.getUserTransactions().add(err);
                account.getTransactionHistory().add(err);
            }
            return;
        }
        for (int i = 0; i < accounts.size(); i++) {
            accounts.get(i).splitPay(amountsExchanged[i]);
            SplitPaymentTransaction transaction = new SplitPaymentTransaction(timestamp,
                    description, currency,
                    amount / accounts.size(),
                    involvedAccounts, error);
            accounts.get(i)
                    .getUserTransactions()
                    .add(transaction);
            accounts.get(i)
                    .getTransactionHistory()
                    .add(transaction);
        }
    }
}

