package org.poo.commerciants;

public class SecondSpendingThreshold extends SpendingThreshold {
    public SecondSpendingThreshold(String plan) {
        this.setPlanType(plan);
    }

    @Override
    public double getCashback(double amount) {
        String plan = getPlanType();
        switch (plan) {
            case "standard":
                return 0.2 * amount / 100;
            case "silver":
                return 0.4 * amount / 100;
            case "gold":
                return 0.55 * amount / 100;
            case "student":
                return 0.2 * amount / 100;
            default:
                break;
        }
        return 0;
    }
}
