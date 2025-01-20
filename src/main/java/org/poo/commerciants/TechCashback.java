package org.poo.commerciants;

public class TechCashback extends NrOfTransactionsCashback {
    private final double percent = 10;
    /**
     * Calculates the cashback for the given amount.
     * <p>
     * This method calculates the cashback as one-tenth
     * (1/10) of the provided {@code amount}.
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the calculated cashback value (one-tenth of the amount)
     */
    @Override
    public double getCashback(final double amount) {
        return amount / percent;
    }

}
