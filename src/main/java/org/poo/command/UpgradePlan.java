package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.UpgradePlanTransaction;
import org.poo.bank.User;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;
import org.poo.plan.ServicePlan;

class UpgradePlan implements Command {
    private User user;
    private Account account;
    private String type;
    private int timestamp;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    UpgradePlan(final User user,
                final Account account,
                final String type,
                final int timestamp,
                final CurrencyGraph graph,
                final ObjectMapper objectMapper,
                final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.type = type;
        this.graph = graph;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the upgrade plan command. It checks if the account
     * exists, compares the current plan with the new plan, and
     * ensures that the account has enough balance for the upgrade.
     * If valid, it deducts the fee and performs the upgrade.
     * A transaction is then created to log the upgrade.
     */
    @Override
    public void execute() {
        if (account == null) {
            // print the error
            System.out.println("The account doesn t exist");
            return;
        }
        ServicePlan newPlan = ServicePlan.createPlan(type);
        if (user.getPlan().compareTo(newPlan) > 0) {
            return;
        }
        if (user.getPlan().compareTo(newPlan) == 0) {
            return;
        }
        double convertedFee = graph
                .exchange(new Node("RON", 1),
                new Node(account.getCurrency(), 1),
                user.getPlan().upgrade(type));
        if (convertedFee > account.getBalance()) {
            return;
        }
        account.payUpgradeFee(convertedFee);
        user.setPlan(newPlan);
        user.upgradePlan();
        UpgradePlanTransaction transaction =
                new UpgradePlanTransaction(timestamp,
                                            account.getIban(),
                                            type);
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}
