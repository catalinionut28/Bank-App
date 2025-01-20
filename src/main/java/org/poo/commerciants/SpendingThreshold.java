package org.poo.commerciants;

public abstract class SpendingThreshold implements CashbackStrategy {
    private String strategy = "spendingThreshold";
    private String planType;

    /**
     * Abstract method to calculate cashback based on the specified amount.
     * <p>
     * This method must be implemented
     * by subclasses to provide specific cashback
     * calculation logic based on
     * the spending threshold and the given amount.
     * </p>
     *
     * @param amount the amount
     * to calculate cashback for
     * @return the cashback value calculated for the specified amount
     */
    public abstract double getCashback(double amount);

    /**
     * Retrieves the strategy type.
     * <p>
     * This method returns the {@code strategy} value associated with the current instance.
     * </p>
     *
     * @return the strategy type as a {@code String}
     */
    @Override
    public String getStrategy() {
        return strategy;
    }

    /**
     * Retrieves the plan type.
     * <p>
     * This method returns the {@code planType} value associated with the current instance,
     * indicating the type of plan.
     * </p>
     *
     * @return the plan type as a {@code String}
     */
    public String getPlanType() {
        return planType;
    }

    /**
     * Sets the plan type.
     * <p>
     * This method assigns a {@code planType}
     * value to indicate the type of plan associated
     * with the current instance.
     * </p>
     *
     * @param planType the plan type to set
     */
    public void setPlanType(final String planType) {
        this.planType = planType;
    }
}
