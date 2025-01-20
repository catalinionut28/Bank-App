package org.poo.plan;

public abstract class ServicePlan {
    protected String type;
    protected double commission;


    /**
     * Gets the type of the service plan.
     *
     * @return The type of the service plan.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the commission rate of the service plan.
     *
     * @return The commission rate.
     */
    public double getCommission() {
        return commission;
    }

    /**
     * Upgrades the service plan based on the given type.
     *
     * @param type The type to upgrade to (e.g., gold, silver).
     * @return     The upgrade fee for the new plan.
     */
    public abstract double upgrade(final String type);

    /**
     * Compares the current service plan with another one.
     *
     * @param servicePlan The service plan to compare with.
     * @return            A positive number if this plan is higher,
     *                    negative if lower, or 0 if equal.
     */
    public int compareTo(final ServicePlan servicePlan) {
        if (this.type.equals("gold")) {
            if (!servicePlan.getType().equals("gold")) {
                return 1;
            }
        }
        if (this.type.equals("silver")) {
            if (servicePlan.getType().equals("gold")) {
                return -1;
            }
            if (servicePlan.getType().equals("standard")
                || servicePlan.getType().equals("student")) {
                return 1;
            }
        }
        if (this.type.equals("standard")
            || this.type.equals("student")) {
            if (servicePlan.getType().equals("gold")
                || servicePlan.getType().equals("silver")) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Creates a service plan based on the given type.
     *
     * @param type The type of plan to create (e.g., student, silver).
     * @return     A new instance of the corresponding service plan.
     */
    public static ServicePlan createPlan(final String type) {
        switch (type) {
            case "student":
                return new StudentPlan();
            case "standard":
                return new StandardPlan();
            case "silver":
                return new SilverPlan();
            case "gold":
                return new GoldPlan();
            default:
                break;
        }
        return null;
    }
}
