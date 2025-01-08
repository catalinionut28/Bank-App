package org.poo.commerciants;

public class FoodCashback extends NrOfTransactionsCashback {
    @Override
    public double getCashback(double amount) {
        return 2 * amount / 100;
    }
}
