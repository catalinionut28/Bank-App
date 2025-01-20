package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.CardPayment;
import org.poo.bank.ClassicAccount;

import java.util.TreeMap;

class SpendingsReport implements Command {
    private Account account;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;


    SpendingsReport(final Account account,
                    final int startTimestamp,
                    final int endTimestamp,
                    final int timestamp,
                    final ObjectMapper objectMapper,
                    final ArrayNode arrayNode) {
        this.account = account;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = arrayNode;
    }

    /**
     * Executes the spending report command for
     * the given account within a specified timestamp range.
     * This method generates a report
     * for a given account, which includes:
     * - The account's IBAN, balance, and currency
     * - A list of `CardPayment` transactions that occurred within the specified timestamp range
     * - A summary of total spending per commerciant,
     * calculated by adding up the amounts
     * spent on each merchant.
     * If the account is not found,
     * an error is returned indicating that the account does not exist.
     * If the account type is "savings",
     * an error is returned stating that this report is not supported
     * for savings accounts.
     */
    public void execute() {
        TreeMap<String, Double> totalMap = new TreeMap<>();
        if (account == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "spendingsReport");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("description", "Account not found");
            errorNode.put("timestamp", timestamp);
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        if (account.getType().equals("savings")) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "spendingsReport");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("error",
                    "This kind of report is not "
                            + "supported for a saving account");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "spendingsReport");
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", account.getIban());
        reportNode.put("balance", account.getBalance());
        reportNode.put("currency", account.getCurrency());
        ArrayNode transactions = objectMapper.createArrayNode();
        for (CardPayment transaction
                : ((ClassicAccount) account).getSpendingsReport()) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {
                ObjectNode transactionNode = objectMapper.createObjectNode();
                if (!totalMap.containsKey(transaction.getCommerciant())) {
                    totalMap.put(transaction.getCommerciant(), transaction.getAmount());
                } else {
                    Double amount = totalMap.get(transaction.getCommerciant());
                    totalMap.put(transaction.getCommerciant(),
                            amount + transaction.getAmount());
                }
                transactionNode.put("timestamp", transaction.getTimestamp());
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("amount", transaction.getAmount());
                transactionNode.put("commerciant", transaction.getCommerciant());
                transactions.add(transactionNode);
            }
        }
        reportNode.set("transactions", transactions);
        ArrayNode commerciants = objectMapper.createArrayNode();
        for (String commerciant : totalMap.keySet()) {
            ObjectNode commerciantNode = objectMapper.createObjectNode();
            commerciantNode.put("commerciant", commerciant);
            commerciantNode.put("total", totalMap.get(commerciant));
            commerciants.add(commerciantNode);
        }
        reportNode.set("commerciants", commerciants);
        outputNode.set("output", reportNode);
        outputNode.put("timestamp", timestamp);
        output.add(outputNode);
    }
}
