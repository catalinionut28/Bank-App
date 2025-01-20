package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;
import org.poo.plan.SilverPlan;
import org.poo.plan.StandardPlan;

class CashWithdrawal implements Command {
    private User user;
    private Account account;
    private Card card;
    private double ronAmount;
    private int timestamp;
    private CurrencyGraph currencyGraph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    CashWithdrawal(final User user,
                   final Account account,
                   final Card card,
                   final double ronAmount,
                   final int timestamp,
                   final ObjectMapper objectMapper,
                   final CurrencyGraph currencyGraph,
                   final ArrayNode output) {
        this.user = user;
        this.card = card;
        this.account = account;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.ronAmount = ronAmount;
        this.output = output;
        this.currencyGraph = currencyGraph;
    }

    /**
     * Executes the cash withdrawal process
     * for the user, verifying the card and account status.
     * <p>
     * If the user or card is not found,
     * an error message is returned. If the card is frozen, a
     * `FrozenPayment` transaction is added.
     * If there are insufficient funds in the account, an
     * `InsufficientFunds` transaction is added. Otherwise, the withdrawal amount is converted
     * from RON to the account's currency, and the cash withdrawal is processed.
     * </p>
     */
    @Override
    public void execute() {
        if (user == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cashWithdrawal");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", timestamp);
            errorNode.put("description", "User not found");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        if (card == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "cashWithdrawal");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", timestamp);
            errorNode.put("description", "Card not found");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        if (card.getStatus().equals("frozen")) {
            FrozenPayment frozenPayment = new FrozenPayment(timestamp,
                    "The card is frozen");
            user.getTransactions().add(frozenPayment);
            account.getTransactionHistory().add(frozenPayment);
            return;
        }
        double amount = currencyGraph.exchange(new Node("RON", 1),
                new Node(account.getCurrency(), 1),
                ronAmount);
        if (account.getBalance() < amount
                + getCommission(amount, ronAmount)) {
            InsufficientFunds insufficientFunds =
                    new InsufficientFunds(timestamp);
            user.getTransactions().add(insufficientFunds);
            account.getTransactionHistory().add(insufficientFunds);
            return;
        }
        account.withdrawCash(amount, ronAmount, card);
        CashTransaction cashTransaction =
                new CashTransaction(timestamp, ronAmount);
        user.getTransactions().add(cashTransaction);
        account.getTransactionHistory().add(cashTransaction);


    }

    /**
     * Calculates the commission for a cash withdrawal based on the user's plan type.
     * <p>
     * The commission is determined differently depending on whether the user's plan is
     * "standard" or "silver".
     * For a "standard" plan, it uses the commission calculation
     * of the `StandardPlan` class.
     * For a "silver" plan, it uses the commission calculation
     * of the `SilverPlan` class.
     * If the user's plan type is different, no commission is applied.
     * </p>
     *
     * @param amount     The amount
     *                   to be withdrawn
     *                   in the account's currency.
     * @param amountRon  The amount in RON to be withdrawn.
     * @return           The calculated commission amount.
     */
    private double getCommission(final double amount,
                                 final double amountRon) {
        double commission = 0;
        switch (user.getPlan().getType()) {
            case "standard":
                commission = ((StandardPlan) user
                        .getPlan())
                        .calculateCommission(amount);
                break;
            case "silver":
                commission = ((SilverPlan) user.getPlan())
                        .calculateCommission(amount, amountRon);
                break;
            default:
                break;
        }
        return commission;
    }
}
