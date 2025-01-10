package org.poo.plan;

public class StandardPlan extends ServicePlan {

    private final double silverFee = 100;
    private final double goldFee = 250;

    public StandardPlan() {
        commission = 0.2;
        type = "standard";
    }

    @Override
    public double upgrade(String type) {
        if (type.equals("silver")) {
            return silverFee;
        }
        if (type.equals("gold")) {
            return goldFee;
        }
        return 0;
    }

    public double calculateCommission(double amount) {
        return commission * amount / 100;
    }
}
