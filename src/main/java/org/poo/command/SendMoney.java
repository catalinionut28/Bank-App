package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.InsufficientFunds;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;
import org.poo.plan.SilverPlan;
import org.poo.plan.StandardPlan;

class SendMoney implements Command {
    private Account sender;
    private Account receiver;
    private double amount;
    private int timestamp;
    private String description;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    SendMoney(final Account sender,
              final Account receiver,
              final double amount,
              final int timestamp,
              final String description,
              final CurrencyGraph graph,
              final ObjectMapper objectMapper,
              final ArrayNode output) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = timestamp;
        this.graph = graph;
        this.objectMapper = objectMapper;
        this.output = output;
        this.description = description;
    }

    /**
     * Executes the "sendMoney" command to transfer
     * funds from the sender to the receiver.
     * The method checks if both sender and receiver are valid,
     * and if the sender has sufficient
     * funds in their account.
     * If there are insufficient funds,
     * an {@link InsufficientFunds}
     * transaction is added to the sender's transaction history.
     * Otherwise, the money is sent
     * and the transaction is recorded in both
     * the sender's and receiver's transaction histories.
     */
    public void execute() {
        if (sender == null || receiver == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "sendMoney");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", timestamp);
            errorNode.put("description", "User not found");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        if (amount == 0) {
            return;
        }
        double ronAmount = graph.exchange(new Node(sender.getCurrency(), 1),
                new Node("RON", 1),
                amount);
        if (sender.getBalance() < amount + getCommission(amount, ronAmount)) {
            InsufficientFunds insufficientFunds =
                    new InsufficientFunds(timestamp);
            sender.getUserTransactions().add(insufficientFunds);
            sender.getTransactionHistory().add(insufficientFunds);
            return;
        }
        sender.sendMoney(receiver,
                amount,
                graph,
                timestamp,
                description);
    }


    private double getCommission(final double amountParameter,
                                 final double ronAmount) {
        double commission = 0;
        switch (sender.getPlan().getType()) {
            case "standard":
                commission = ((StandardPlan) sender
                        .getPlan())
                        .calculateCommission(amountParameter);
                break;
            case "silver":
                commission = ((SilverPlan) sender.getPlan())
                        .calculateCommission(amountParameter,
                                ronAmount);
                break;
            default:
                break;
        }
        return commission;
    }
}
