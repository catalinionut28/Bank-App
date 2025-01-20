package org.poo.plan;

public class StudentPlan extends ServicePlan {
    private final double silverFee = 100;
    private final double goldFee = 350;

    public StudentPlan() {
        commission = 0;
        type = "student";
    }

    /**
     * Upgrades the service plan to the specified type.
     *
     * @param newType The type to upgrade to
     *             (e.g., "silver", "gold").
     * @return     The upgrade fee for the new plan type.
     */
    public double upgrade(final String newType) {
        if (newType.equals("silver")) {
            return silverFee;
        }
        if (newType.equals("gold")) {
            return goldFee;
        }
        return 0;
    }


}
