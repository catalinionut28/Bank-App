package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;

class Report implements Command {
    private Account account;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    Report(final Account account,
           final int startTimestamp,
           final int endTimestamp,
           final int timestamp,
           final ObjectMapper objectMapper,
           final ArrayNode output) {
        this.account = account;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the report command for the given account.
     * This method generates a report for a specific account,
     * including details such as the account's
     * IBAN, balance, currency, and a list
     * of transactions within a specified timestamp range.
     * The transactions are filtered based on the account type
     * (e.g., "classic" or "savings"). If the
     * account is not found, an error is returned.
     */
    @Override
    public void execute() {
        if (account == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "report");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("description", "Account not found");
            errorNode.put("timestamp", timestamp);
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "report");
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", account.getIban());
        reportNode.put("balance", account.getBalance());
        reportNode.put("currency", account.getCurrency());
        ArrayNode transactions = objectMapper.createArrayNode();
        switch (account.getType()) {
            case "classic":
                for (Transaction transaction
                        : account.getTransactionHistory()) {
                    if (transaction.getTimestamp() >= startTimestamp
                            && transaction.getTimestamp() <= endTimestamp) {
                        ObjectNode transactionNode = objectMapper.createObjectNode();
                        switch (transaction.getType()) {
                            case "AccountCreation":
                                AccountCreation accountCreation = (AccountCreation) transaction;
                                transactionNode
                                        .put("timestamp", accountCreation.getTimestamp());
                                transactionNode
                                        .put("description", accountCreation.getDescription());
                                break;
                            case "Transfer":
                                SendReceive sendReceive = (SendReceive) transaction;
                                transactionNode
                                        .put("timestamp", sendReceive.getTimestamp());
                                transactionNode
                                        .put("description", sendReceive.getDescription());
                                transactionNode
                                        .put("senderIBAN", sendReceive.getSenderIban());
                                transactionNode
                                        .put("receiverIBAN", sendReceive.getReceiverIban());
                                transactionNode
                                        .put("amount", sendReceive.getAmount());
                                transactionNode
                                        .put("transferType", sendReceive.getTransferType());
                                break;
                            case "CardCreation":
                                CardCreation cardCreation = (CardCreation) transaction;
                                transactionNode.put("timestamp", cardCreation.getTimestamp());
                                transactionNode.put("description", cardCreation.getDescription());
                                transactionNode.put("card", cardCreation.getCard());
                                transactionNode.put("cardHolder", cardCreation.getCardHolder());
                                transactionNode.put("account", cardCreation.getAccount());
                                break;
                            case "CardPayment":
                                CardPayment cardPayment = (CardPayment) transaction;
                                transactionNode.put("timestamp", cardPayment.getTimestamp());
                                transactionNode.put("description", cardPayment.getDescription());
                                transactionNode.put("amount", cardPayment.getAmount());
                                transactionNode.put("commerciant", cardPayment.getCommerciant());
                                break;
                            case "Rejected":
                                InsufficientFunds error = (InsufficientFunds) transaction;
                                transactionNode.put("timestamp", error.getTimestamp());
                                transactionNode.put("description", error.getDescription());
                                break;
                            case "CardDestruction":
                                CardDestruction cardDestruction = (CardDestruction) transaction;
                                transactionNode.put("timestamp", cardDestruction.getTimestamp());
                                transactionNode
                                        .put("description", cardDestruction.getDescription());
                                transactionNode.put("card", cardDestruction.getCard());
                                transactionNode.put("cardHolder", cardDestruction.getCardHolder());
                                transactionNode.put("account", cardDestruction.getAccount());
                                break;
                            case "FrozenNotification":
                                FrozenPayment frozen = (FrozenPayment) transaction;
                                transactionNode.put("timestamp", frozen.getTimestamp());
                                transactionNode.put("description", frozen.getDescription());
                                break;
                            case "SplitPayment":
                                SplitPaymentTransaction splitPayment =
                                        (SplitPaymentTransaction) transaction;
                                transactionNode.put("timestamp", splitPayment.getTimestamp());
                                transactionNode.put("description", splitPayment.getDescription());
                                transactionNode.put("currency", splitPayment.getCurrency());
                                transactionNode.put("amount", splitPayment.getAmount());
                                if (splitPayment.getError() != null) {
                                    transactionNode.put("error", splitPayment.getError());
                                }
                                ArrayNode involvedAccountsArray = objectMapper.createArrayNode();
                                for (int i = 0;
                                     i < splitPayment.getInvolvedAccounts().size(); i++) {
                                    involvedAccountsArray.add(splitPayment
                                            .getInvolvedAccounts()
                                            .get(i));
                                }
                                transactionNode.set("involvedAccounts", involvedAccountsArray);
                                break;
                            case "UpgradePlan":
                                UpgradePlanTransaction planTransaction =
                                        (UpgradePlanTransaction) transaction;
                                transactionNode.put("timestamp",
                                        planTransaction.getTimestamp());
                                transactionNode.put("description",
                                        planTransaction.getDescription());
                                transactionNode.put("accountIBAN",
                                        planTransaction.getAccountIban());
                                transactionNode.put("newPlanType",
                                        planTransaction.getNewPlanType());
                                break;
                            default:
                                break;
                        }
                        transactions.add(transactionNode);
                    }
                }
                break;
            case "savings":
                for (Transaction transaction
                        : ((SavingsAccount) account).getReport()) {
                    if (transaction.getTimestamp() >= startTimestamp
                            && transaction.getTimestamp() <= endTimestamp) {
                        ObjectNode transactionNode = objectMapper.createObjectNode();
                        switch (transaction.getType()) {
                            case "InterestChanged":
                                InterestChanged interestChanged =
                                        (InterestChanged) transaction;
                                transactionNode.put("description",
                                        interestChanged.getDescription());
                                transactionNode.put("timestamp",
                                        interestChanged.getTimestamp());
                                break;
                            default:
                                break;
                        }
                        transactions.add(transactionNode);
                    }
                }
                break;
            default:
                break;
        }
        reportNode.set("transactions", transactions);
        outputNode.set("output", reportNode);
        outputNode.put("timestamp", timestamp);
        output.add(outputNode);
    }
}
