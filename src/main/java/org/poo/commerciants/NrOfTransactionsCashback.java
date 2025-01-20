package org.poo.commerciants;

public abstract class NrOfTransactionsCashback implements CashbackStrategy {
    private final String strategy = "nrOfTransactions";

    /**
     * Calculates the cashback for a given amount based on the specific implementation.
     * <p>
     * This method is intended to be implemented
     * by subclasses to calculate the cashback based
     * on the specific rules for the respective class,
     * using the provided {@code amount}.
     * </p>
     *
     * @param amount the amount on
     * which cashback is calculated
     * @return the calculated
     * cashback value
     */
    @Override
    public abstract double getCashback(double amount);

    /**
     * Retrieves the strategy.
     * <p>
     * This method returns the {@code strategy} value associated with the current instance.
     * </p>
     *
     * @return the strategy
     */
    @Override
    public String getStrategy() {
        return strategy;
    }
}
