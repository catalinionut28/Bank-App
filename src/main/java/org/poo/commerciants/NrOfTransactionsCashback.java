package org.poo.commerciants;

public abstract class NrOfTransactionsCashback implements CashbackStrategy {
    private final String strategy = "nrOfTransactions";
    @Override
    public abstract double getCashback(double amount);

    @Override
    public String getStrategy() {
        return strategy;
    }
}
