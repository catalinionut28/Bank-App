package org.poo.commerciants;

public class TechCashback extends NrOfTransactionsCashback {
    @Override
    public double getCashback(double amount) {
        return amount / 10;
    }

}
