package org.poo.command;

public enum CommandType {
    PRINT_USERS("printUsers"),
    ADD_ACCOUNT("addAccount"),
    CREATE_CARD("createCard"),
    ADD_FUNDS("addFunds"),
    DELETE_ACCOUNT("deleteAccount"),
    CREATE_ONE_TIME_CARD("createOneTimeCard"),
    DELETE_CARD("deleteCard"),
    SET_MINIMUM_BALANCE("setMinimumBalance"),
    PAY_ONLINE("payOnline"),
    SEND_MONEY("sendMoney"),
    SET_ALIAS("setAlias"),
    PRINT_TRANSACTIONS("printTransactions"),
    CHECK_CARD_STATUS("checkCardStatus"),
    SPLIT_PAYMENT("splitPayment"),
    REPORT("report");

    public final String command;

    CommandType(String command) {
        this.command = command;
    }

    public static CommandType fromString(String command) {
        for (CommandType commandType: CommandType.values()) {
            if (commandType.command.equals(command)) {
                return commandType;
            }
        }
        return null;
    }
}
