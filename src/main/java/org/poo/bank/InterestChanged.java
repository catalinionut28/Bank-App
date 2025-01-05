package org.poo.bank;

public class InterestChanged extends Transaction {

    public InterestChanged(final int timestamp,
                           final double interestRate) {
        this.setTimestamp(timestamp);
        String description =
                "Interest rate of "
                        + "the account changed to "
                        + String.format("%.2f", interestRate);
        this.setDescription(description);
        this.setType("InterestChanged");
    }
}
