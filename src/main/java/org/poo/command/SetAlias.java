package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;

import java.util.HashMap;

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
             final ArrayNode output) {
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
