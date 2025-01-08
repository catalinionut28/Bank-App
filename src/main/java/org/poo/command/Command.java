package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.commerciants.Merchant;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public interface Command {

    /**
     * Executes the command.
     * This method should contain the logic that is executed when the command is invoked.
     */
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

    AddAccount(final User user,
                      final String accountType,
                      final String currency,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode arrayNode) {
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

    CreateCard(final User user,
                      final Account account,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
        this.error = null;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to add a new account for the user.
     * This method creates either a classic
     * or savings account based on the provided {@code accountType}.
     * If the user does not exist, an error message is added to the output.
     */
    public void execute() {
        if (user == null || account == null) {
            return;
        }
        account.createCard();
        CardCreation transaction = new CardCreation(timestamp,
                account.getCards().getLast().getCardNumber(),
                user.getEmail(),
                account.getIban());
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}

class AddFunds implements Command {
    private final Account account;
    private final double amount;

    AddFunds(final Account account,
                    final double amount) {
        this.account = account;
        this.amount = amount;
    }

    /**
     * Executes the command to add funds to the specified account.
     * This method checks if the account is valid (non-null).
     * If the account exists,
     * the specified amount will be added
     * to the account's balance using the addFunds(double) method.
     */
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

    PrintUsers(final Dao userDao,
               final int timestamp,
               final ObjectMapper objectMapper,
               final ArrayNode output) {
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
            ArrayNode accountsArray = objectMapper.createArrayNode();
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());
            for (DaoObject accountData: user.getAccountDao().getAll()) {
                Account account = (Account) accountData;
                ArrayNode cardsArray = objectMapper.createArrayNode();
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
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
    private final String iban;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    DeleteAccount(final User user,
                         final String iban,
                         final int timestamp,
                         final ObjectMapper objectMapper,
                         final ArrayNode output) {
        this.user = user;
        this.iban = iban;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to print all users,
     * their accounts, and associated cards to the output.
     * This method iterates over all users retrieved from the
     * {@code userDao}.
     * For each user, it creates a structured
     * JSON object that includes the user's first name,
     * last name, email, and for each of their accounts,
     * it includes the account's IBAN,
     * balance, currency, type, and associated cards.
     * The resulting data is added to the output.
     * The final output is formatted in
     * a structured way and includes a timestamp for the execution.
     */
    public void execute() {
        try {
            Dao accountData = user.getAccountDao();
            Account account = (Account) accountData.get(iban);
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
                user.getTransactions().add(new DeleteError(timestamp,
                        "Account couldn't be deleted - "
                                + "there are funds remaining"));
                return;
            }
            accountData.delete(iban);
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "deleteAccount");
            ObjectNode successNode = objectMapper.createObjectNode();
            successNode.put("success", "Account deleted");
            successNode.put("timestamp", timestamp);
            outputNode.set("output", successNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
        } catch (IllegalArgumentException e) {
            return;
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

    DeleteCard(final User user,
                      final Account account,
                      final String cardNumber,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to delete the
     * specified card from the user's account and records the transaction.
     * This method searches for the card with the given
     * card number in the specified account's card list.
     * If the card is found, it is removed from the account,
     * and a {@link CardDestruction} transaction is created.
     * This transaction is then added to both
     * the user's and the account's transaction histories.
     */
    @Override
    public void execute() {
        if (account == null) {
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
                cardNumber, user.getEmail(), account.getIban());
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


    CreateOneTimeCard(final User user,
                             final Account account,
                             final int timestamp,
                             final ObjectMapper objectMapper,
                             final ArrayNode output) {
        this.user = user;
        this.account = account;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public void execute() {
        if (account == null) {
            return;
        }
        account.createOneTimeCard();
        CardCreation transaction = new CardCreation(timestamp,
                account.getCards().getLast().getCardNumber(),
                user.getEmail(),
                account.getIban());
        user.getTransactions().add(transaction);
        account.getTransactionHistory().add(transaction);
    }
}
/**
 * Executes the command to create a one-time-use
 * card for the specified account and records the transaction.
 * This method calls {@link Account#createOneTimeCard()}
 * to create the one-time card for the account.
 * A {@link CardCreation} transaction is then created,
 * logging the details of the card creation,
 * and the transaction is added to both the
 * user's and the account's transaction histories.
 */
class SetMinimumBalance implements Command {
    private Account account;
    private final double amount;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    SetMinimumBalance(final Account account,
                      final double amount,
                      final int timestamp,
                      final ObjectMapper objectMapper,
                      final ArrayNode output) {
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the command to set the minimum balance for the specified account.
     * This method sets the specified
     * amount as the minimum balance requirement for the account
     * using setMinimumBalance(double).
     */
    public void execute() {
        if (account != null) {
            account.setMinimumBalance(amount);
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
        if (account.getCurrency().equals(currency)) {
            if (account.getBalance() < amount) {
                InsufficientFunds insufficientFunds =
                        new InsufficientFunds(timestamp);
                user.getTransactions().add(insufficientFunds);
                account.getTransactionHistory().add(insufficientFunds);
                return;
            }
            CardPayment transaction =
                    new CardPayment(timestamp, commerciant.getName(), amount);
            account.payOnline(amount, cardNumber, commerciant, ronAmount);
            user.getTransactions().add(transaction);
            account.getTransactionHistory().add(transaction);
            if (account.getType().equals("classic")) {
                ((ClassicAccount) account).getSpendingsReport().add(transaction);
            }
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
                    new CardPayment(timestamp, commerciant.getName(), newAmount);
            account.payOnline(newAmount, cardNumber, commerciant, ronAmount);
            user.getTransactions().add(transaction);
            account.getTransactionHistory().add(transaction);
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
    private HashMap<String, Account> aliasMap;
    private Account account;
    private String alias;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    SetAlias(final HashMap<String, Account> aliasMap,
             final Account account,
             final String alias,
             final int timestamp,
             final ObjectMapper objectMapper,
             ArrayNode output) {
        this.aliasMap = aliasMap;
        this.account = account;
        this.alias = alias;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the "setAlias" command,
     * which assigns an alias to the given account.
     * The alias is stored in the aliasMap,
     * associating the alias with the account.
     * If the account
     * is null, the command does nothing.
     */
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
                    transactionNode.put("amount", splitPayment.getAmount());
                    if (splitPayment.getError() != null) {
                        transactionNode.put("error", splitPayment.getError());
                    }
                    ArrayNode involvedAccountsArray = objectMapper.createArrayNode();
                    for (int i = splitPayment.getInvolvedAccounts().size() - 1;
                    i >= 0; i--) {
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
    private final int warningLimit = 30;

    CheckCardStatus(final User user,
                           final Card card,
                           final Account account,
                           final int timestamp,
                           final ObjectMapper objectMapper,
                           final ArrayNode output) {
        this.account = account;
        this.user = user;
        this.card = card;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the "checkCardStatus" command
     * by checking the status of the card.
     * The method checks the account
     * balance to determine whether
     * the card should be frozen or
     * issued a warning.
     * If the account balance is
     * less than or equal to the minimum balance,
     * the card status is set to "frozen".
     * If the balance is within a warning threshold, the card
     * status is set to "warning".
     * If the user or card is null,
     * an error response is returned.
     */
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
        if (account.getBalance() <= account.getMinimumBalance() + warningLimit) {
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

    SplitPayment(final ArrayList<Account> accounts,
                        final double amount,
                        final String currency,
                        final int timestamp,
                        final CurrencyGraph graph,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.graph = graph;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    /**
     * Executes the split payment
     * command by dividing the amount among multiple accounts.
     * The total amount is split equally among the provided accounts.
     * Each account's balance is
     * checked to ensure it has enough funds for
     * its portion of the split. If any account has
     * insufficient funds,
     * the split payment fails, and an error message is recorded. Otherwise,
     * the payment is processed,
     * and a transaction record is added for each account.
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
                for (Transaction transaction : account.getTransactionHistory()) {
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
                break;
            case "savings":
                for (Transaction transaction
                        : ((SavingsAccount) account).getReport()) {
                    if (transaction.getTimestamp() >= startTimestamp
                            && transaction.getTimestamp() <= endTimestamp) {
                        ObjectNode transactionNode = objectMapper.createObjectNode();
                        switch (transaction.getType()) {
                            case "InterestChanged":
                                InterestChanged interestChanged = (InterestChanged) transaction;
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
 *   spent on each merchant.
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
        for (String commerciant: totalMap.keySet()) {
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

class ChangeInterestRate implements Command {
    private Account account;
    private double interestRate;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    ChangeInterestRate(final Account account,
                              final double interestRate,
                              final int timestamp,
                              final ObjectMapper objectMapper,
                              final ArrayNode output) {
        this.account = account;
        this.interestRate = interestRate;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

/**
 * Executes the command to change
 * the interest rate for a savings account.
 * This method first checks whether the provided account
 * is of type "savings". If the account is
 * not a savings account,
 * an error message is returned
 * indicating that the operation is not
 * supported for non-savings accounts.
 * If the account is a savings account,
 * the interest rate is updated to the provided value.
 * A transaction of type `InterestChanged`
 * is created and recorded in both the account's
 * transaction history and report.
 */
    public void execute() {
        if (account == null) {
            return;
        }
        if (!account.getType().equals("savings")) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "changeInterestRate");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description",
                    "This is not a savings account");
            outputNode.put("timestamp", timestamp);
            errorNode.set("output", outputNode);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        SavingsAccount savingsAccount = (SavingsAccount) account;
        savingsAccount.setInterestRate(interestRate);
        InterestChanged interestChanged =
                new InterestChanged(timestamp,
                                    interestRate);
        savingsAccount.getReport().add(interestChanged);
        savingsAccount.getUserTransactions()
                        .add(interestChanged);
    }
}

class AddInterest implements Command {
    private final Account account;
    private final int timestamp;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    AddInterest(final Account account,
                final int timestamp,
                final ObjectMapper objectMapper,
                final ArrayNode output) {
        this.account = account;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

/**
 * Executes the command to add interest to a savings account.
 * This method first checks if the provided `account` is of type "savings".
 * If the account is
 * not a savings account,
 * an error message is created and added to the output indicating that
 * the operation is not supported for non-savings accounts.
 * If the account is a savings account,
 * the method calls the `addInterestRate()`
 * method on the `SavingsAccount`
 * to apply the interest
 * rate changes.
 */
    @Override
    public void execute() {
        if (account == null) {
            return;
        }
        if (!account.getType().equals("savings")) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "addInterest");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description",
                    "This is not a savings account");
            outputNode.put("timestamp", timestamp);
            errorNode.set("output", outputNode);
            errorNode.put("timestamp", timestamp);
            output.add(errorNode);
            return;
        }
        SavingsAccount savingsAccount = (SavingsAccount) account;
        savingsAccount.addInterestRate();
    }
}

class WithdrawSavings implements Command {
    private User user;
    private Account savingsAccount;
    private Account account;
    private double amount;
    private int timestamp;
    private ObjectMapper objectMapper;
    private ArrayNode output;
    private final static int MINIMUM_AGE = 21;

    WithdrawSavings(final User user,
                    final Account savingsAccount,
                    final Account account,
                    final double amount,
                    final int timestamp,
                    final ObjectMapper objectMapper,
                    final ArrayNode output) {
        this.user = user;
        this.savingsAccount = savingsAccount;
        this.account = account;
        this.amount = amount;
        this.timestamp = timestamp;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    @Override
    public void execute() {
        if (savingsAccount == null) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "Account not found"));
            return;
        }
        if (!savingsAccount.getType().equals("savings")) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "Account is not of type savings"));
            return;
        }
        if (savingsAccount.getBalance() <= 0) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "Insufficient funds"));
        }
        if (account == null) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "You do not have a classic account."));
            return;
        }
        if (user.getAge() < MINIMUM_AGE) {
            user.getTransactions()
                    .add(new SavingsWithdrawn(timestamp,
                            "You don't have the minimum age required."));
            return;
        }
        ((SavingsAccount) savingsAccount)
                .withdraw(((ClassicAccount) account), amount);
        user.getTransactions().add(new SavingsWithdrawn(timestamp,
                "Savings withdrawal"));
    }
}



