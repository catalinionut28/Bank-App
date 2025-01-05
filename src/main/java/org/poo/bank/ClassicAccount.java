package org.poo.bank;

import org.poo.utils.Utils;

import java.util.ArrayList;

public class ClassicAccount extends Account {
    private ArrayList<CardPayment> spendingsReport;

    public ClassicAccount(final String currency,
                          final ArrayList<Transaction> transactions) {
        this.setBalance(0);
        this.setCards(new ArrayList<>());
        this.setIban(Utils.generateIBAN());
        this.setType("classic");
        this.setCurrency(currency);
        this.setUserTransactions(transactions);
        this.setTransactionHistory(new ArrayList<Transaction>());
        this.setSpendingsReport(new ArrayList<>());
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
