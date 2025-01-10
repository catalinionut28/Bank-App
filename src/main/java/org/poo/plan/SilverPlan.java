package org.poo.plan;

public class SilverPlan extends ServicePlan {
    private final double goldFee = 250;

    public SilverPlan() {
        commission = 0.1;
        type = "silver";
    }

    @Override
    public double upgrade(String type) {
        if (type.equals("gold")) {
            return goldFee;
        }
        return 0;
    }

    public double calculateCommission(double amount, double ronAmount) {
        if (ronAmount < 500) {
            return 0;
        }
        return commission * amount / 100;
    }
}
