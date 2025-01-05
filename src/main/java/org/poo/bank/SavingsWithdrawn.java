package org.poo.bank;

public class SavingsWithdrawn extends Transaction {
    public SavingsWithdrawn(int timestamp,
                            String description) {
        this.setDescription(description);
        this.setTimestamp(timestamp);
        this.setType("SavingsWithdrawn");
    }
}
