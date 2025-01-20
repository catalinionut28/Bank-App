package org.poo.plan;

public class SilverPlan extends ServicePlan {
    private final double goldFee = 250;

    private final double silverCommission = 0.1;
    private final double percent = 100;

    private final double allowedAmount = 500;

    public SilverPlan() {
        commission = silverCommission;
        type = "silver";
    }

    /**
     * Upgrades the service plan to the specified type.
     *
     * @param type The type to upgrade to (e.g., "gold").
     * @return     The upgrade fee for the "gold" plan type,
     *             or 0 if the type is not "gold".
     */
    @Override
    public double upgrade(final String type) {
        if (type.equals("gold")) {
            return goldFee;
        }
        return 0;
    }

    /**
     * Calculates the commission based on the given amount.
     *
     * @param amount     The transaction amount.
     * @param ronAmount  The amount in RON used to calculate the commission.
     * @return           The commission amount, or 0 if the RON
     *                   amount is below the allowed threshold.
     */
    public double calculateCommission(final double amount,
                                      final double ronAmount) {
        if (ronAmount < allowedAmount) {
            return 0;
        }
        return commission * amount / percent;
    }
}
