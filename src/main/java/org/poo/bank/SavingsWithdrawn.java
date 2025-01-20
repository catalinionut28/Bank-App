package org.poo.bank;

public class SavingsWithdrawn extends Transaction {
    public SavingsWithdrawn(final int timestamp,
                            final String description) {
        this.setDescription(description);
        this.setTimestamp(timestamp);
        this.setType("SavingsWithdrawn");
    }
}
