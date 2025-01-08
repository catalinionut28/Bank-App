package org.poo.command;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;
import org.poo.commerciants.Merchant;
import org.poo.fileio.*;
import org.poo.graph.CurrencyGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private Invoker invoker;
    private Dao userDao;
    private CurrencyGraph currencyGraph;
    private ArrayList<Merchant> merchants;
    private HashMap<String, Account> aliasMap;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public Client(final ObjectInput inputData,
                  final ObjectMapper objectMapper,
                  final ArrayNode output) {
        invoker = new Invoker();
        userDao = new DaoImpl();
        aliasMap = new HashMap<>();
        currencyGraph = new CurrencyGraph();
        merchants = new ArrayList<>();
        for (UserInput userInput: inputData.getUsers()) {
            userDao.update(new User(userInput));
        }
        for (ExchangeInput exchange: inputData.getExchangeRates()) {
            currencyGraph.addEdge(exchange.getFrom(), exchange.getTo(), exchange.getRate());
        }
        for (CommerciantInput commerciantInput: inputData.getCommerciants()) {
            merchants.add(new Merchant(commerciantInput));
        }
        this.objectMapper = objectMapper;
        this.output = output;
    }


    /**
     * Factory method that generates the appropriate
     * command based on the given {@link CommandType}.
     * This method acts as a factory for creating
     * specific command objects based on the type provided.
     * It takes a {@link CommandType} and
     * {@link CommandInput},
     * retrieves the necessary data (such as
     * user or account information),
     * and returns an instance of the corresponding command.
     * If the
     * command type is unrecognized or there is an error,
     * the method returns null.
     *
     * @param type The type of command to create (e.g., ADD_ACCOUNT, ADD_FUNDS, etc.).
     * @param commandInput The input data needed to create the command.
     * @return A {@link Command} object corresponding
     * to the provided {@link CommandType}, or null if the
     * command type is invalid or an
     * error occurs during object creation.
     */
    private Command getCommand(final CommandType type,
                               final CommandInput commandInput) {
        try {
            Command command;
            User user = null;
            Account account = null;
            switch (type) {
                case ADD_ACCOUNT:
                    user = (User) userDao.get(commandInput.getEmail());
                    command = new AddAccount(user,
                            commandInput.getAccountType(),
                            commandInput.getCurrency(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case PRINT_USERS:
                    command = new PrintUsers(userDao,
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case CREATE_CARD:
                    user = (User) userDao.get(commandInput.getEmail());
                    account = (Account) user.getAccountDao().get(commandInput.getAccount());
                    command = new CreateCard(user, account,
                                            commandInput.getTimestamp(),
                                            objectMapper, output);
                    return command;
                case ADD_FUNDS:
                    for (DaoObject userData : userDao.getAll()) {
                        user = (User) userData;
                        account = (Account) user.getAccountDao().get(commandInput.getAccount());
                        if (account != null) {
                            break;
                        }
                    }
                    command = new AddFunds(account, commandInput.getAmount());
                    return command;
                case DELETE_ACCOUNT:
                    user = (User) userDao.get(commandInput.getEmail());
                    command = new DeleteAccount(user,
                            commandInput.getAccount(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case DELETE_CARD:
                    user = (User) userDao.get(commandInput.getEmail());
                    for (DaoObject accountData: user.getAccountDao().getAll()) {
                        Account acc = (Account) accountData;
                        for (Card card : acc.getCards()) {
                            if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                                account = acc;
                            }
                        }
                    }
                    command = new DeleteCard(user,
                            account,
                            commandInput.getCardNumber(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case CREATE_ONE_TIME_CARD:
                    user = (User) userDao.get(commandInput.getEmail());
                    account = (Account) user.getAccountDao().get(commandInput.getAccount());
                    command = new CreateOneTimeCard(user,
                            account,
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case SET_MINIMUM_BALANCE:
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        for (DaoObject accountData: usr.getAccountDao().getAll()) {
                            Account acc = (Account) accountData;
                            if (acc.getIban().equals(commandInput.getAccount())) {
                                account = acc;
                            }
                        }
                    }
                    command = new SetMinimumBalance(account,
                            commandInput.getAmount(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case PAY_ONLINE:
                    user = (User) userDao.get(commandInput.getEmail());
                    for (DaoObject accData: user.getAccountDao().getAll()) {
                        Account acc = (Account) accData;
                        for (Card card: acc.getCards()) {
                            if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                                account = acc;
                                break;
                            }
                        }
                    }
                    Merchant commerciant = null;
                    for (Merchant merchant: merchants) {
                        if (merchant.getName().equals(commandInput.getCommerciant())) {
                            commerciant = merchant;
                        }
                    }
                    command = new PayOnline(user,
                                            account,
                                            commandInput.getCardNumber(),
                                            commandInput.getAmount(),
                                            commandInput.getCurrency(),
                                            commandInput.getTimestamp(),
                                            currencyGraph,
                                            commerciant,
                                            objectMapper,
                                            output);
                    return command;
                case SEND_MONEY:
                    User sender = (User) userDao.get(commandInput.getEmail());
                    Account senderAcc = (Account) sender.getAccountDao()
                                                        .get(commandInput.getAccount());
                    Account receiverAcc = aliasMap.get(commandInput.getReceiver());
                    if (receiverAcc == null) {
                        for (DaoObject userDaoObject : userDao.getAll()) {
                            User usr = (User) userDaoObject;
                            receiverAcc = (Account) usr.getAccountDao()
                                                        .get(commandInput.getReceiver());
                            if (receiverAcc != null) {
                                break;
                            }
                        }
                    }
                    command = new SendMoney(senderAcc,
                                            receiverAcc,
                                            commandInput.getAmount(),
                                            commandInput.getTimestamp(),
                                            commandInput.getDescription(),
                                            currencyGraph,
                                            objectMapper,
                                            output);
                    return command;
                case SET_ALIAS:
                    for (DaoObject usrData: userDao.getAll()) {
                        User usr = (User) usrData;
                        for (DaoObject accData: usr.getAccountDao().getAll()) {
                            Account acc = (Account) accData;
                            if (acc.getIban().equals(commandInput.getAccount())) {
                                account = acc;
                                break;
                            }
                        }
                    }
                    command = new SetAlias(aliasMap,
                                            account,
                                            commandInput.getAlias(),
                                            commandInput.getTimestamp(),
                                            objectMapper,
                                            output);
                    return command;
                case PRINT_TRANSACTIONS:
                    user = (User) userDao.get(commandInput.getEmail());
                    command = new PrintTransactions(user,
                                                    commandInput.getTimestamp(),
                                                    objectMapper,
                                                    output);
                    return command;
                case CHECK_CARD_STATUS:
                    Card card = null;
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        for (DaoObject accountData: usr.getAccountDao().getAll()) {
                            Account acc = (Account) accountData;
                            Card cardFound = acc.getCard(commandInput.getCardNumber());
                            if (cardFound != null) {
                                card = cardFound;
                                user = usr;
                                account = acc;
                                break;
                            }
                        }
                    }
                    command = new CheckCardStatus(user,
                            card, account, commandInput.getTimestamp(),
                            objectMapper, output);
                    return command;
                case SPLIT_PAYMENT:
                    ArrayList<Account> accounts = new ArrayList<>();
                    for (String iban: commandInput.getAccounts()) {
                        for (DaoObject userData: userDao.getAll()) {
                            User usr = (User) userData;
                            Account acc = (Account) usr.getAccountDao().get(iban);
                            if (acc != null) {
                                account = acc;
                            }
                        }
                        accounts.add(account);
                        account = null;
                    }
                    command = new SplitPayment(accounts,
                            commandInput.getAmount(),
                            commandInput.getCurrency(),
                            commandInput.getTimestamp(),
                            currencyGraph,
                            objectMapper,
                            output);
                    return command;
                case REPORT:
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        Account acc = (Account) usr
                                .getAccountDao()
                                .get(commandInput.getAccount());
                        if (acc != null) {
                            account = acc;
                        }
                    }
                    command = new Report(account,
                            commandInput.getStartTimestamp(),
                            commandInput.getEndTimestamp(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case SPENDINGS_REPORT:
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        Account acc = (Account) usr
                                .getAccountDao()
                                .get(commandInput.getAccount());
                        if (acc != null) {
                            account = acc;
                        }
                    }
                    command = new SpendingsReport(account,
                            commandInput.getStartTimestamp(),
                            commandInput.getEndTimestamp(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case ADD_INTEREST:
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        Account acc = (Account) usr
                                .getAccountDao()
                                .get(commandInput.getAccount());
                        if (acc != null) {
                            account = acc;
                        }
                    }
                    command = new AddInterest(account,
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                case CHANGE_INTEREST_RATE:
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        Account acc = (Account) usr
                                .getAccountDao()
                                .get(commandInput.getAccount());
                        if (acc != null) {
                            account = acc;
                        }
                    }
                    command = new ChangeInterestRate(account,
                            commandInput.getInterestRate(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;

                case WITHDRAW_SAVINGS:
                    for (DaoObject userData: userDao.getAll()) {
                        User usr = (User) userData;
                        Account acc = (Account) usr
                                .getAccountDao()
                                .get(commandInput.getAccount());
                        if (acc != null) {
                            account = acc;
                            user = usr;
                        }
                    }
                    Account classicAccount = null;
                    try {
                        for (DaoObject accData : user.getAccountDao().getAll()) {
                            Account acc = (Account) accData;
                            if (acc.getType().equals("classic")
                                    && acc.getCurrency().equals(account.getCurrency())) {
                                classicAccount = acc;
                            }
                        }
                    } catch (NullPointerException e) {
                    }
                    command = new WithdrawSavings(user, account,
                            classicAccount, commandInput.getAmount(),
                            commandInput.getTimestamp(),
                            objectMapper,
                            output);
                    return command;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }


    /**
     * Executes the action associated with the given command name and command input.
     * This method is responsible for determining the appropriate command type from the provided
     * command name, creating the corresponding
     * {@link Command} object using the {@link CommandInput},
     * and executing it through the provided
     * {@link Invoker}.
     * If the command is invalid or cannot be
     * created, an {@link IllegalArgumentException} is thrown.
     *
     * @param commandName The name of the command to execute (as a string).
     * @param commandInput The input data required to execute the command.
     * @throws IllegalArgumentException
     * If the command name is invalid or if the command cannot be created.
     */
    public void executeAction(final String commandName,
                              final CommandInput commandInput) throws IllegalArgumentException {
        CommandType commandType = CommandType.fromString(commandName);
        Command command = getCommand(commandType, commandInput);
        if (command == null) {
            throw new IllegalArgumentException("Invalid command!");
        }
        invoker.execute(command);
    }
}
