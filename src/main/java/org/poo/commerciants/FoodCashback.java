package org.poo.commerciants;

public class FoodCashback extends NrOfTransactionsCashback {
    private final double percent = 100;

    /**
     * Calculates the cashback for the
     * given amount based on the food cashback strategy.
     * <p>
     * This method returns 2% of the
     * provided amount as cashback for food merchants.
     * </p>
     *
     * @param amount the amount on
     * which cashback is calculated
     * @return the calculated cashback value
     */
    @Override
    public double getCashback(final double amount) {
        return 2 * amount / percent;
    }
}
