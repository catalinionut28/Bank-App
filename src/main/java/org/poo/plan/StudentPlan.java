package org.poo.plan;

public class StudentPlan extends ServicePlan {
    private final double silverFee = 100;
    private final double goldFee = 350;

    public StudentPlan() {
        commission = 0;
        type = "student";
    }

    public double upgrade(String type) {
        if (type.equals("silver")) {
            return silverFee;
        }
        if (type.equals("gold")) {
            return goldFee;
        }
        return 0;
    }


}
