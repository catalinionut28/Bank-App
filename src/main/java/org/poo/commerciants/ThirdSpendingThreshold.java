package org.poo.commerciants;

public class ThirdSpendingThreshold extends SpendingThreshold {

    public ThirdSpendingThreshold(String plan) {
        this.setPlanType(plan);
    }

    @Override
    public double getCashback(double amount) {
        String plan = getPlanType();
        switch (plan) {
            case "standard":
                return 0.25 * amount / 100;
            case "silver":
                return 0.5* amount / 100;
            case "gold":
                return 0.7 * amount / 100;
            case "student":
                return 0.25 * amount / 100;
            default:
                break;
        }
        return 0;
    }
}
