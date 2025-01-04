package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;


public interface Command {
    void execute();
}

class AddAccount implements Command {

    private final User user;
    private final String accountType;
    private final String currency;
    private final int timestamp;
    private String error;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public AddAccount(User user,
                      String accountType,
                      String currency,
                      int timestamp,
                      ObjectMapper objectMapper,
                      ArrayNode arrayNode) {
        this.user = user;
        this.accountType = accountType;
        this.currency = currency;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = arrayNode;
        this.error = null;
    }

    public void execute() {
        if (user == null) {
            error = "The user doesn't exist";
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "addAccount");
            errorNode.put("error", error);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        if (accountType.equals("classic")) {
            user.createClassicAccount(currency, timestamp);
        } else {
            user.createSavingsAccount(currency, timestamp);
        }
    }
}

class CreateCard implements Command {
    private User user;
    private final Account account;
    private final int timestamp;
    private String error;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public CreateCard(User user, Account account,
                      int timestamp,
                      ObjectMapper objectMapper,
                      ArrayNode output) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
        this.error = null;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        if (user == null || account == null) {
            return;
        }
        account.createCard();
        CardCreation transaction = new CardCreation(timestamp,
                account.getCards().getLast().getCardNumber(),
                user.getEmail(),
                account.getIBAN());
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}

class AddFunds implements Command {
    private final Account account;
    private final double amount;

    public AddFunds(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public void execute() {
        if (account == null) {
            return;
        }
        account.addFunds(amount);
    }
}

class PrintUsers implements Command {
    private final Dao userDao;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    PrintUsers(Dao userDao,
               int timestamp,
               ObjectMapper objectMapper,
               ArrayNode output) {
        this.userDao = userDao;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "printUsers");
        ArrayNode usersArray = objectMapper.createArrayNode();
        for (DaoObject userData: userDao.getAll()) {
            User user = (User) userData;
            System.out.println(user.getEmail());
            ArrayNode accountsArray = objectMapper.createArrayNode();
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());
            for (DaoObject accountData: user.getAccountDao().getAll()) {
                Account account = (Account) accountData;
                ArrayNode cardsArray = objectMapper.createArrayNode();
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIBAN());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getType());
                for (Card card: account.getCards()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("cardNumber", card.getCardNumber());
                    cardNode.put("status", card.getStatus());
                    cardsArray.add(cardNode);
                }
                accountNode.set("cards", cardsArray);
                accountsArray.add(accountNode);
            }
            userNode.set("accounts", accountsArray);
            usersArray.add(userNode);
        }
        outputNode.set("output", usersArray);
        outputNode.put("timestamp", timestamp);
        output.add(outputNode);
    }
}

class DeleteAccount implements Command {
    private final User user;
    private final String IBAN;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public DeleteAccount(User user,
                         String IBAN,
                          int timestamp,
                         ObjectMapper objectMapper,
                         ArrayNode output) {
        this.user = user;
        this.IBAN = IBAN;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        try {
            Dao accountData = user.getAccountDao();
            Account account = (Account) accountData.get(IBAN);
            if (account.getBalance() > 0) {
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("command", "deleteAccount");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("error",
                            "Account couldn't be deleted - see org.poo.transactions for details");
                outputNode.put("timestamp", timestamp);
                errorNode.set("output", outputNode);
                errorNode.put("timestamp", timestamp);
                output.add(errorNode);
                return;
            }
            accountData.delete(IBAN);
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "deleteAccount");
            ObjectNode successNode = objectMapper.createObjectNode();
            successNode.put("success", "Account deleted");
            successNode.put("timestamp", timestamp);
            outputNode.set("output", successNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
        } catch (IllegalArgumentException e) {
            // TODO: print the error
        }
    }
}

class DeleteCard implements Command {
    private User user;
    private Account account;
    private String cardNumber;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public DeleteCard(User user,
                      Account account,
                      String cardNumber,
                      int timestamp,
                      ObjectMapper objectMapper,
                      ArrayNode output) {
        this.user = user;
        this.account = account;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    @Override
    public void execute() {
        if (account == null) {
            // implement the error
            return;
        }
        Card card = null;
        for (Card c: account.getCards()) {
            if (c.getCardNumber().equals(cardNumber)) {
                card = c;
            }
        }
        account.getCards().remove(card);
        CardDestruction destruction = new CardDestruction(timestamp,
                cardNumber, user.getEmail(), account.getIBAN());
        user.getTransactions().add(destruction);
        account.getTransactionHistory().add(destruction);
    }
}

class CreateOneTimeCard implements Command {
    private User user;
    private Account account;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;


    public CreateOneTimeCard(User user,
                             Account account,
                             int timestamp,
                             ObjectMapper objectMapper,
                             ArrayNode output) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        if (account == null) {
            // TODO: treat the error
            return;
        }
        account.createOneTimeCard();
        CardCreation transaction = new CardCreation(timestamp,
                account.getCards().getLast().getCardNumber(),
                user.getEmail(),
                account.getIBAN());
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}

class SetMinimumBalance implements Command {
    private Account account;
    private final double amount;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public SetMinimumBalance(Account account,
                             double amount,
                             int timestamp,
                             ObjectMapper objectMapper,
                             ArrayNode output) {
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        if (account != null) {
            account.setMinimumBalance(amount);
        } else {
            // TODO: treat the error
        }
    }
}

class PayOnline implements Command {
    private User user;
    private Account account;
    private String cardNumber;
    private double amount;
    private String currency;
    private int timestamp;
    private String commerciant;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public PayOnline(User user,
                     Account account,
                     String cardNumber,
                     double amount,
                     String currency,
                     int timestamp,
                     CurrencyGraph graph,
                     String commerciant,
                     ObjectMapper objectMapper,
                     ArrayNode output) {
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

    @Override
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
        if (account.getCard(cardNumber).getStatus().equals("frozen")) {
            FrozenPayment frozenPayment = new FrozenPayment(timestamp,
                                                "The card is frozen");
            user.getTransactions().add(frozenPayment);
            account.getTransactionHistory().add(frozenPayment);
            return;
        }
        if (account.getCurrency().equals(currency)) {
            if (account.getBalance() < amount) {
                InsufficientFunds insufficientFunds =
                        new InsufficientFunds(timestamp);
                user.getTransactions().add(insufficientFunds);
                account.getTransactionHistory().add(insufficientFunds);
                return;
            }
            CardPayment transaction =
                    new CardPayment(timestamp, commerciant, amount);
            account.payOnline(amount);
            user.getTransactions().add(transaction);
            account.getTransactionHistory().add(transaction);

        } else {
            double newAmount = graph.exchange(new Node(currency, 1.0),
                                            new Node(account.getCurrency(), 1),
                                            amount);
            if (account.getBalance() < newAmount) {
                InsufficientFunds insufficientFunds =
                        new InsufficientFunds(timestamp);
                user.getTransactions().add(insufficientFunds);
                account.getTransactionHistory().add(insufficientFunds);
                return;
            }
            CardPayment transaction =
                    new CardPayment(timestamp, commerciant, newAmount);
            account.payOnline(newAmount);
            user.getTransactions().add(transaction);
            account.getTransactionHistory().add(transaction);
        }
    }
}

class SendMoney implements Command {
    private Account sender;
    private Account receiver;
    private double amount;
    private int timestamp;
    private String description;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public SendMoney(Account sender,
                     Account receiver,
                     double amount,
                     int timestamp,
                     String description,
                     CurrencyGraph graph,
                     ObjectMapper objectMapper,
                     ArrayNode output) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = timestamp;
        this.graph = graph;
        this.objectMapper = objectMapper;
        this.output = output;
        this.description = description;
    }

    public void execute() {
        if (sender == null || receiver == null) {
            return;
        }
        if (sender.getBalance() < amount) {
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
}

class SetAlias implements Command {
    HashMap<String, Account> aliasMap;
    private Account account;
    private String alias;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public SetAlias(HashMap<String, Account> aliasMap,
                    Account account,
                    String alias,
                    int timestamp,
                    ObjectMapper objectMapper,
                    ArrayNode output) {
        this.aliasMap = aliasMap;
        this.account = account;
        this.alias = alias;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    @Override
    public void execute() {
        if (account == null) {
            return;
        }
        aliasMap.put(alias, account);
    }
}

class PrintTransactions implements Command {
    private User user;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public PrintTransactions(User user,
                             int timestamp,
                             ObjectMapper objectMapper,
                             ArrayNode output) {
        this.user = user;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

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
                    transactionNode.put("senderIBAN", sendReceive.getSenderIBAN());
                    transactionNode.put("receiverIBAN", sendReceive.getReceiverIBAN());
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
                    transactionNode.put("amount", splitPayment.getAmount());
                    ArrayNode involvedAccountsArray = objectMapper.createArrayNode();
                    for (int i = splitPayment.getInvolvedAccounts().size() - 1;
                    i >= 0; i--) {
                        involvedAccountsArray.add(splitPayment
                                .getInvolvedAccounts()
                                .get(i));
                    }
                    transactionNode.set("involvedAccounts", involvedAccountsArray);
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

class CheckCardStatus implements Command {
    private User user;
    private Card card;
    private Account account;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;
    private final int WARNING_LIMIT = 30;

    public CheckCardStatus(User user,
                           Card card,
                           Account account,
                           int timestamp,
                           ObjectMapper objectMapper,
                           ArrayNode output) {
        this.account = account;
        this.user = user;
        this.card = card;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        if (user == null || card == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "checkCardStatus");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");
            errorNode.set("output", outputNode);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        if (account.getBalance() <= account.getMinimumBalance()) {
            card.setStatus("frozen");
            FrozenPayment frozenPayment = new FrozenPayment(timestamp,
                    "You have reached the minimum amount of funds, the card will be frozen");
            user.getTransactions().add(frozenPayment);
            account.getTransactionHistory().add(frozenPayment);
            return;
        }
        if (account.getBalance() <= account.getMinimumBalance() + WARNING_LIMIT) {
            card.setStatus("warning");
        }
    }
}

class SplitPayment implements Command {
    private ArrayList<Account> accounts;
    private double amount;
    private String currency;
    private int timestamp;
    private CurrencyGraph graph;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public SplitPayment(ArrayList<Account> accounts,
                        double amount,
                        String currency,
                        int timestamp,
                        CurrencyGraph graph,
                        ObjectMapper objectMapper,
                        ArrayNode output) {
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.graph = graph;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        double[] amountsExchanged = new double[accounts.size()];
        ArrayList<String> involvedAccounts = new ArrayList<>();
        for (int i = accounts.size() - 1; i >= 0; i--) {
            Account account = accounts.get(i);
            if (account == null) {
                return;
            }
            amountsExchanged[i] = graph.exchange(new Node(currency, 1),
                    new Node(account.getCurrency(), 1),
                    amount / accounts.size());
            if (account.getBalance() < amountsExchanged[i]) {
                return;
            }
            involvedAccounts.add(account.getIBAN());
        }
        String description = "Split payment of " +
                String.format("%.2f", amount) +
                " " + currency;
        for (int i = 0; i < accounts.size(); i++) {
            accounts.get(i).splitPay(amountsExchanged[i]);
            SplitPaymentTransaction transaction = new SplitPaymentTransaction(timestamp,
                    description, currency,
                    amount / accounts.size(),
                    involvedAccounts);
            accounts.get(i)
                    .getUserTransactions()
                    .add(transaction);
            accounts.get(i)
                    .getTransactionHistory()
                    .add(transaction);
        }
    }
}

class Report implements Command {
    private Account account;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public Report(Account account,
                  int startTimestamp,
                  int endTimestamp,
                  int timestamp,
                  ObjectMapper objectMapper,
                  ArrayNode output) {
        this.account = account;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    @Override
    public void execute() {
        if (account == null) {
            return;
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "report");
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", account.getIBAN());
        reportNode.put("balance", account.getBalance());
        reportNode.put("currency", account.getCurrency());
        ArrayNode transactions = objectMapper.createArrayNode();
        switch (account.getType()) {
            case "classic":
                for (Transaction transaction : account.getTransactionHistory()) {
                    if (transaction.getTimestamp() >= startTimestamp &&
                    transaction.getTimestamp() <= endTimestamp) {
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
                                transactionNode.put("senderIBAN", sendReceive.getSenderIBAN());
                                transactionNode.put("receiverIBAN", sendReceive.getReceiverIBAN());
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
                                transactionNode.put("amount", splitPayment.getAmount());
                                ArrayNode involvedAccountsArray = objectMapper.createArrayNode();
                                for (int i = splitPayment.getInvolvedAccounts().size() - 1;
                                     i >= 0; i--) {
                                    involvedAccountsArray.add(splitPayment
                                                                .getInvolvedAccounts()
                                                                .get(i));
                                }
                                transactionNode.set("involvedAccounts", involvedAccountsArray);
                                break;
                            default:
                                break;
                        }
                        transactions.add(transactionNode);
                    }
                }
            default:
                break;
        }
        reportNode.set("transactions", transactions);
        outputNode.set("output", reportNode);
        outputNode.put("timestamp", timestamp);
        output.add(outputNode);
    }
}



