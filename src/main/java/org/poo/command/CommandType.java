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
    REPORT("report"),
    SPENDINGS_REPORT("spendingsReport"),
    ADD_INTEREST("addInterest"),
    CHANGE_INTEREST_RATE("changeInterestRate"),
    WITHDRAW_SAVINGS("withdrawSavings");

    private final String command;

    CommandType(final String command) {
        this.command = command;
    }

    /**
     * Converts a string command to its corresponding CommandType enum.
     *
     * @param command The string representation of the command.
     * @return The corresponding CommandType, or {@code null} if no matching command is found.
     */
    public static CommandType fromString(final String command) {
        for (CommandType commandType: CommandType.values()) {
            if (commandType.command.equals(command)) {
                return commandType;
            }
        }
        return null;
    }
}
