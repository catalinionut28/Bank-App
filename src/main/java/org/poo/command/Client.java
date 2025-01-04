package org.poo.command;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.graph.CurrencyGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private Invoker invoker;
    private Dao userDao;
    private CurrencyGraph currencyGraph;
    private HashMap<String, Account> aliasMap;
    private ObjectMapper objectMapper;
    private ArrayNode output;

    public Client(ObjectInput inputData, ObjectMapper objectMapper, ArrayNode output) {
        invoker = new Invoker();
        userDao = new DaoImpl();
        aliasMap = new HashMap<>();
        currencyGraph = new CurrencyGraph();
        for (UserInput userInput: inputData.getUsers()) {
            userDao.update(new User(userInput));
        }
        for (ExchangeInput exchange: inputData.getExchangeRates()) {
            currencyGraph.addEdge(exchange.getFrom(), exchange.getTo(), exchange.getRate());
        }
        this.objectMapper = objectMapper;
        this.output = output;
    }

    private Command getCommand(CommandType type, CommandInput commandInput) {
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
                    command = new CreateCard(user, account, commandInput.getTimestamp(), objectMapper, output);
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
                            System.out.println(card.getCardNumber());
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
                            if (acc.getIBAN().equals(commandInput.getAccount())) {
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
                    command = new PayOnline(user,
                                            account,
                                            commandInput.getCardNumber(),
                                            commandInput.getAmount(),
                                            commandInput.getCurrency(),
                                            commandInput.getTimestamp(),
                                            currencyGraph,
                                            commandInput.getCommerciant(),
                                            objectMapper,
                                            output);
                    return command;
                case SEND_MONEY:
                    User sender = (User) userDao.get(commandInput.getEmail());
                    Account senderAcc = (Account) sender.getAccountDao().get(commandInput.getAccount());
                    Account receiverAcc = aliasMap.get(commandInput.getReceiver());
                    if (receiverAcc == null) {
                        for (DaoObject userDaoObject : userDao.getAll()) {
                            User usr = (User) userDaoObject;
                            receiverAcc = (Account) usr.getAccountDao().get(commandInput.getReceiver());
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
                            if (acc.getIBAN().equals(commandInput.getAccount())) {
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
                    for (String IBAN: commandInput.getAccounts()) {
                        for (DaoObject userData: userDao.getAll()) {
                            User usr = (User) userData;
                            Account acc = (Account) usr.getAccountDao().get(IBAN);
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
                default:
                    break;
            }
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

    public void executeAction(String commandName,
                              CommandInput commandInput) throws IllegalArgumentException {
        CommandType commandType = CommandType.fromString(commandName);
        System.out.println(commandType);
        Command command = getCommand(commandType, commandInput);
        if (command == null) {
            throw new IllegalArgumentException("Invalid command!");
        }
        invoker.execute(command);
    }
}
