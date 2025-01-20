package org.poo.commerciants;

public class ClothesCashback extends NrOfTransactionsCashback {
    private final double percent = 100;
    private final double clothesPercentage = 5;

    /**
     * Calculates the cashback for
     * the given amount based on the clothes cashback strategy.
     * <p>
     * This method returns 5% of the
     * provided amount as cashback for clothes merchants.
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the calculated cashback value
     */
    @Override
    public double getCashback(final double amount) {
        return clothesPercentage * amount / percent;
    }
}
