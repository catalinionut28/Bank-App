package org.poo.bank;

public class FrozenPayment extends Transaction {

    public FrozenPayment(final int timestamp, final String description) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("FrozenNotification");
    }
}
