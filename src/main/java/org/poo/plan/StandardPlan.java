package org.poo.plan;

public class StandardPlan extends ServicePlan {

    private final double silverFee = 100;
    private final double goldFee = 350;

    private final double standardCommission = 0.2;

    private final double percent = 100;

    public StandardPlan() {
        commission = standardCommission;
        type = "standard";
    }

    /**
     * Returns the fee required to upgrade to a specified
     * plan type.
     *
     * This method checks the requested upgrade type and
     * returns the corresponding upgrade fee for the "silver"
     * or "gold" plan. If the type is neither "silver" nor
     * "gold", the method returns 0, indicating no upgrade
     * fee.
     *
     * @param type The type of the plan to upgrade to (either
     *             "silver" or "gold").
     * @return     The fee required for the upgrade, or 0 if
     *             the type is not valid.
     */
    @Override
    public double upgrade(final String type) {
        if (type.equals("silver")) {
            return silverFee;
        }
        if (type.equals("gold")) {
            return goldFee;
        }
        return 0;
    }

    /**
     * Calculates the commission for a given amount.
     *
     * This method computes the commission based on the
     * percentage of the amount and the defined commission
     * rate.
     *
     * @param amount The amount for which the commission
     *               is being calculated.
     * @return      The calculated commission.
     */
    public double calculateCommission(final double amount) {
        return commission * amount / percent;
    }
}
