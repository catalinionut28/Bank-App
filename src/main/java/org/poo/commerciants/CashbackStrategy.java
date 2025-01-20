package org.poo.commerciants;

public interface CashbackStrategy {
    /**
     * Calculates the cashback for a given amount.
     * <p>
     * This method should be implemented to
     * calculate the cashback based on specific strategy rules.
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the calculated cashback value
     */

    double getCashback(double amount);
    /**
     * Retrieves the strategy type.
     * <p>
     * This method should return the
     * type of cashback strategy as a string (e.g., "spendingThreshold").
     * </p>
     *
     * @return the strategy type
     */
    String getStrategy();
}
