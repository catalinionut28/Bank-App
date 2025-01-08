package org.poo.commerciants;

public class CashbackContext {
    private CashbackStrategy cashbackStrategy;

    public CashbackContext(CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    public void setCashbackStrategy(CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    public double performCashback(double amount) {
        return cashbackStrategy.getCashback(amount);
    }
}
