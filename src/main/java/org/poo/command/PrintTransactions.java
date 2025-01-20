package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;

class PrintTransactions implements Command {
    private User user;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    PrintTransactions(final User user,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode output) {
        this.user = user;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the "printTransactions" command
     * by retrieving and formatting the user's
     * transactions as a JSON object.
     * This method processes each transaction
     * in the user's transaction history and adds
     * the formatted information to the output.
     * The type of each transaction determines
     * the specific fields included in the output.
     * If the user is null, no action is taken.
     * The transactions are structured in a
     * format suitable for easy
     * consumption by the calling system or application.
     */
    @Override
    public void execute() {
        if (user == null) {
            return;
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "printTransactions");
        ArrayNode transactions = objectMapper.createArrayNode();
        for (Transaction transaction : user.getTransactions()) {
            ObjectNode transactionNode = objectMapper.createObjectNode();
            switch (transaction.getType()) {
                case "AccountCreation":
                    AccountCreation accountCreation = (AccountCreation) transaction;
                    transactionNode.put("timestamp", accountCreation.getTimestamp());
                    transactionNode.put("description", accountCreation.getDescription());
                    break;
                case "Transfer":
                    SendReceive sendReceive = (SendReceive) transaction;
                    transactionNode.put("timestamp", sendReceive.getTimestamp());
                    transactionNode.put("description", sendReceive.getDescription());
                    transactionNode.put("senderIBAN", sendReceive.getSenderIban());
                    transactionNode.put("receiverIBAN", sendReceive.getReceiverIban());
                    transactionNode.put("amount", sendReceive.getAmount());
                    transactionNode.put("transferType", sendReceive.getTransferType());
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
                    transactionNode.put("description", cardDestruction.getDescription());
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
                    SplitPaymentTransaction splitPayment = (SplitPaymentTransaction) transaction;
                    transactionNode.put("timestamp", splitPayment.getTimestamp());
                    transactionNode.put("description", splitPayment.getDescription());
                    transactionNode.put("currency", splitPayment.getCurrency());
                    transactionNode.put("splitPaymentType", splitPayment.getSplitType());
                    ArrayNode amounts = objectMapper.createArrayNode();
                    for (Double amount : splitPayment.getAmountForUsers()) {
                        amounts.add(amount);
                    }
                    if (splitPayment.getSplitType().equals("custom")) {
                        transactionNode.set("amountForUsers", amounts);
                    } else {
                        transactionNode.put("amount", splitPayment
                                .getAmountForUsers()
                                .get(0));
                    }
                    if (splitPayment.getError() != null) {
                        transactionNode.put("error", splitPayment.getError());
                    }
                    ArrayNode involvedAccountsArray = objectMapper.createArrayNode();
                    for (int i = 0;
                         i < splitPayment
                                 .getInvolvedAccounts()
                                 .size(); i++) {
                        involvedAccountsArray.add(splitPayment
                                .getInvolvedAccounts()
                                .get(i));
                    }
                    transactionNode.set("involvedAccounts", involvedAccountsArray);
                    break;
                case "DeleteError":
                    DeleteError err = (DeleteError) transaction;
                    transactionNode.put("timestamp", err.getTimestamp());
                    transactionNode.put("description", err.getDescription());
                    break;
                case "InterestChanged":
                    InterestChanged interestChanged = (InterestChanged) transaction;
                    transactionNode.put("description",
                            interestChanged.getDescription());
                    transactionNode.put("timestamp",
                            interestChanged.getTimestamp());
                    break;
                case "SavingsWithdrawn":
                    SavingsWithdrawn savingsWithdrawn = (SavingsWithdrawn) transaction;
                    transactionNode.put("timestamp", savingsWithdrawn.getTimestamp());
                    transactionNode.put("description", savingsWithdrawn.getDescription());
                    break;
                case "UpgradePlan":
                    UpgradePlanTransaction planTransaction =
                            (UpgradePlanTransaction) transaction;
                    transactionNode.put("timestamp", planTransaction.getTimestamp());
                    transactionNode.put("description", planTransaction.getDescription());
                    transactionNode.put("accountIBAN", planTransaction.getAccountIban());
                    transactionNode.put("newPlanType", planTransaction.getNewPlanType());
                    break;
                case "CashTransaction":
                    CashTransaction cashTransaction =
                            (CashTransaction) transaction;
                    transactionNode.put("timestamp", cashTransaction.getTimestamp());
                    transactionNode.put("description", cashTransaction.getDescription());
                    transactionNode.put("amount", cashTransaction.getRonAmount());
                    break;
                case "InterestRateIncome":
                    InterestRateIncome interestRateIncome =
                            (InterestRateIncome) transaction;
                    transactionNode.put("amount",
                            interestRateIncome.getAmount());
                    transactionNode.put("currency",
                            interestRateIncome.getCurrency());
                    transactionNode.put("description",
                            interestRateIncome.getDescription());
                    transactionNode.put("timestamp",
                            interestRateIncome.getTimestamp());
                    break;
                default:
                    break;
            }
            transactions.add(transactionNode);
        }
        outputNode.set("output", transactions);
        outputNode.put("timestamp", timestamp);
        output.add(outputNode);
    }
}
