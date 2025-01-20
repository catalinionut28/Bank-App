package org.poo.commerciants;

public class CashbackContext {
    private CashbackStrategy cashbackStrategy;


    public CashbackContext(final CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Sets the cashback strategy for this context.
     * <p>
     * This method allows changing the cashback strategy dynamically.
     * </p>
     *
     * @param cashbackStrategy the cashback strategy to set
     */
    public void setCashbackStrategy(final CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Performs the cashback calculation based on the current strategy and the provided amount.
     * <p>
     * This method delegates the cashback calculation to the strategy's {@code getCashback} method.
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the cashback value calculated by the strategy
     */
    public double performCashback(final double amount) {
        return cashbackStrategy.getCashback(amount);
    }
}
