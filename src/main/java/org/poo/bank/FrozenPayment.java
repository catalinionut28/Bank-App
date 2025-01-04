package org.poo.bank;

public class FrozenPayment extends Transaction {

    public FrozenPayment(int timestamp, String description) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("FrozenNotification");
    }
}
