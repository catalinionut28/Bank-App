package org.poo.commerciants;

public class ClothesCashback extends NrOfTransactionsCashback {
    @Override
    public double getCashback(double amount) {
        return 5 * amount / 100;
    }
}
