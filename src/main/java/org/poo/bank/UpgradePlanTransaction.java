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

    /**
     * Retrieves the new plan type.
     * <p>
     * This method returns the
     * {@code newPlanType} value associated with the current instance.
     * </p>
     *
     * @return the new plan type
     */
    public String getNewPlanType() {
        return newPlanType;
    }

    /**
     * Retrieves the account IBAN.
     * <p>
     * This method returns the
     * {@code accountIban} value associated with the current instance.
     * </p>
     *
     * @return the account IBAN
     */
    public String getAccountIban() {
        return accountIban;
    }
}
