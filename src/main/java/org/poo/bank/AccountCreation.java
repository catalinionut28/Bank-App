package org.poo.bank;

public class AccountCreation extends Transaction {
    public AccountCreation(int timestamp) {
        this.setTimestamp(timestamp);
        this.setDescription("New account created");
        this.setType("AccountCreation");
    }
}
