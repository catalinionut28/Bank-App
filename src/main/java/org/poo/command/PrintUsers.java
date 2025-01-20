package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;

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
        for (DaoObject userData : userDao.getAll()) {
            User user = (User) userData;
            ArrayNode accountsArray = objectMapper.createArrayNode();
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());
            for (DaoObject accountData : user.getAccountDao().getAll()) {
                Account account = (Account) accountData;
                ArrayNode cardsArray = objectMapper.createArrayNode();
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getType());
                for (Card card : account.getCards()) {
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
