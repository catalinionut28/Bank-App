package org.poo.plan;

public class GoldPlan extends ServicePlan {

    public GoldPlan() {
        commission = 0;
        type = "gold";
    }

    /**
     * Prevents further upgrades to the Gold plan by returning 0.
     *
     * @param type The type of service plan to upgrade to.
     * @return     0, as no further upgrades are allowed.
     */
    @Override
    public double upgrade(String type) {
        return 0;
    }

}
