package org.poo.bank;

public class InsufficientFunds extends Transaction {

    public InsufficientFunds(final int timestamp) {
        this.setTimestamp(timestamp);
        this.setType("Rejected");
        this.setDescription("Insufficient funds");
    }

}
