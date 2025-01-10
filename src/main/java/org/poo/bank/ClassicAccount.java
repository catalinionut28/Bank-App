package org.poo.bank;

import org.poo.plan.ServicePlan;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassicAccount extends Account {
    private ArrayList<CardPayment> spendingsReport;

    public ClassicAccount(final String currency,
                          final ArrayList<Transaction> transactions,
                          final ServicePlan plan) {
        this.setBalance(0);
        this.setCards(new ArrayList<>());
        this.setIban(Utils.generateIBAN());
        this.setType("classic");
        this.setCurrency(currency);
        this.setUserTransactions(transactions);
        this.setTransactionHistory(new ArrayList<Transaction>());
        this.setSpendingsReport(new ArrayList<>());
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
     * Retrieves the list of card payments representing the spending report.
     *
     * @return an ArrayList of CardPayment objects representing the spending report.
     */

    public ArrayList<CardPayment> getSpendingsReport() {
        return spendingsReport;
    }

    /**
     * Sets the list of card payments for the spending report.
     *
     * @param spendingsReport an ArrayList of CardPayment objects to be set as the spending report.
     */

    public void setSpendingsReport(final ArrayList<CardPayment> spendingsReport) {
        this.spendingsReport = spendingsReport;
    }
}
