package org.poo.plan;

public abstract class ServicePlan {
    protected String type;
    protected double commission;


    public String getType() {
        return type;
    }
    public double getCommission() {
        return commission;
    }
    public abstract double upgrade(String type);

    public int compareTo(ServicePlan servicePlan) {
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

    public static ServicePlan createPlan(String type) {
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