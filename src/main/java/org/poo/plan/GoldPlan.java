package org.poo.plan;

public class GoldPlan extends ServicePlan {

    public GoldPlan() {
        commission = 0;
        type = "gold";
    }

    @Override
    public double upgrade(String type) {
        return 0;
    }

}
