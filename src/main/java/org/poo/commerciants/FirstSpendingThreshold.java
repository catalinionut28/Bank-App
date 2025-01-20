package org.poo.commerciants;

public class FirstSpendingThreshold extends SpendingThreshold {
    private final double percent = 100;
    private final double cashbackTypeOne = 0.1;
    private final double cashbackTypeTwo = 0.3;
    private final double cashbackTypeThree = 0.5;

    public FirstSpendingThreshold(final String plan) {
        this.setPlanType(plan);
    }

    /**
     * Calculates the cashback based on the account plan and the given amount.
     * <p>
     * This method calculates the cashback
     * percentage based on the account's plan type
     * and returns the calculated cashback for the given {@code amount}.
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the calculated cashback value
     */
    public double getCashback(final double amount) {
        String plan = getPlanType();
        switch (plan) {
            case "standard":
                return cashbackTypeOne * amount / percent;
            case "silver":
                return cashbackTypeTwo * amount / percent;
            case "gold":
                return cashbackTypeThree * amount / percent;
            case "student":
                return cashbackTypeOne * amount / percent;
            default:
                break;
        }
        return 0;
    }
}
