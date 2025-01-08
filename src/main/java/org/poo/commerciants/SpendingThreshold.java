package org.poo.commerciants;

public abstract class SpendingThreshold implements CashbackStrategy {
    private String strategy = "spendingThreshold";
    private String planType;

    public abstract double getCashback(double amount);

    @Override
    public String getStrategy() {
        return strategy;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}
