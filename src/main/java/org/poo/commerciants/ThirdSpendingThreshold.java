package org.poo.commerciants;

public class ThirdSpendingThreshold extends SpendingThreshold {
    private final double percent = 100;
    private final double standard = 0.25;
    private final double silver = 0.5;
    private final double gold = 0.7;

    public ThirdSpendingThreshold(final String plan) {
        this.setPlanType(plan);
    }


    /**
     * Calculates the cashback for
     * the given amount based
     * on the spending threshold strategy.
     * <p>
     * This method calculates the
     * cashback based on the user's plan type. It returns:
     * - 0.25% for standard plan
     * - 0.5% for silver plan
     * - 0.7% for gold plan
     * - 0.25% for student plan
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the cashback value based on the plan type and amount
     */
    @Override
    public double getCashback(final double amount) {
        String plan = getPlanType();
        switch (plan) {
            case "standard":
                return standard * amount / percent;
            case "silver":
                return silver * amount / percent;
            case "gold":
                return gold * amount / percent;
            case "student":
                return standard * amount / percent;
            default:
                break;
        }
        return 0;
    }
}
