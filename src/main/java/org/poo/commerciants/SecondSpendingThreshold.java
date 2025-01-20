package org.poo.commerciants;

public class SecondSpendingThreshold extends SpendingThreshold {
    private final double percent = 100;
    private final double standard = 0.2;
    private final double silver = 0.4;
    private  final double gold = 0.55;
    public SecondSpendingThreshold(final String plan) {
        this.setPlanType(plan);
    }


    /**
     * Calculates the cashback for the
     * given amount based on the spending threshold strategy.
     * <p>
     * This method calculates
     * the cashback based on the user's plan type. It returns:
     * - 0.2% for standard plan
     * - 0.4% for silver plan
     * - 0.55% for gold plan
     * - 0.2% for student plan
     * </p>
     *
     * @param amount the amount on which cashback is calculated
     * @return the cashback value based on the plan type and amount
     */
    @Override
    public double getCashback(double amount) {
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
