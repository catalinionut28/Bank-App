package org.poo.bank;

public class UpgradePlanTransaction extends Transaction {
    private final String accountIban;
    private final String newPlanType;

    public UpgradePlanTransaction(final int timestamp,
                                  final String accountIban,
                                  final String newPlanType) {
        this.setType("UpgradePlan");
        this.setDescription("Upgrade plan");
        this.setTimestamp(timestamp);
        this.accountIban = accountIban;
        this.newPlanType = newPlanType;
    }

    public String getNewPlanType() {
        return newPlanType;
    }

    public String getAccountIban() {
        return accountIban;
    }
}
