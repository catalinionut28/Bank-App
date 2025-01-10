package org.poo.bank;

import org.poo.plan.ServicePlan;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class SavingsAccount extends Account {
    private double interestRate;
    private ArrayList<Transaction> report;

    public SavingsAccount(final String currency,
                          final ArrayList<Transaction> transactions,
                          final double interestRate,
                          final ServicePlan plan) {
        this.setBalance(0);
        this.setCards(new ArrayList<>());
        this.setIban(Utils.generateIBAN());
        this.setType("savings");
        this.setCurrency(currency);
        this.setUserTransactions(transactions);
        this.setTransactionHistory(new ArrayList<>());
        this.report = new ArrayList<>();
        this.interestRate = interestRate;
        this.setClothesCashback(null);
        this.setFoodCashback(null);
        this.setTechCashback(null);
        this.setHadClothesCashback(false);
        this.setHadFoodCashback(false);
        this.setHadTechCashback(false);
        this.setCommerciantMap(new HashMap<>());
        this.setSpendingThreshold(null);
        this.setSpendingTotal(0);
        this.setPlan(plan);
    }


    /**
     * Sets the interest rate for the account.
     *
     * @param interestRate the interest rate to set.
     *                     The value should be a positive double (e.g., 0.05 for 5%).
     */
    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Adds the interest rate to the current balance of the account.
     * The balance is updated by multiplying the current
     * balance by the interest rate and adding it to the balance.
     */
    public void addInterestRate() {
        setBalance(getBalance() + getBalance() * interestRate);
    }

    /**
     * Gets the report of transactions for the account.
     *
     * @return an ArrayList of Transaction objects representing the report of all transactions.
     */
    public ArrayList<Transaction> getReport() {
        return report;
    }

    /**
     * Sets the report of transactions for the account.
     *
     * @param report an ArrayList of Transaction objects to be set as the report for the account.
     */
    public void setReport(final ArrayList<Transaction> report) {
        this.report = report;
    }

    public void withdraw(final ClassicAccount account,
                         final double amount) {
        this.setBalance(this.getBalance() - amount);
        account.setBalance(account.getBalance() + amount);
    }
}
