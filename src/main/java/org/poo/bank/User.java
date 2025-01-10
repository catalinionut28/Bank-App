package org.poo.bank;

import org.poo.fileio.UserInput;
import org.poo.plan.StandardPlan;
import org.poo.plan.StudentPlan;
import org.poo.utils.Utils;
import org.poo.plan.ServicePlan;

import java.util.ArrayList;


public class User implements DaoObject {
    private String email;
    private String firstName;
    private String lastName;
    private Dao accountDao;
    private int age;
    private String occupation;
    private ServicePlan plan;
    private ArrayList<Transaction> transactions;

    public User(final UserInput userInput) {
        this.email = userInput.getEmail();
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.accountDao = new DaoImpl();
        this.transactions = new ArrayList<>();
        this.occupation = userInput.getOccupation();
        this.age = Utils.calculateAge(userInput.getBirthDate());
        if (occupation.equals("student")) {
            this.plan = new StudentPlan();
        } else {
            this.plan = new StandardPlan();
        }
    }

    public ServicePlan getPlan() {
        return plan;
    }

    public void setPlan(ServicePlan plan) {
        this.plan = plan;
    }



    public String getOccupation() {
        return occupation;
    }

    public int getAge() {
        return age;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Retrieves the account DAO instance.
     *
     * @return a Dao representing the account DAO.
     */

    public Dao getAccountDao() {
        return accountDao;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return a String representing the email address.
     */

    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the user's first name.
     *
     * @return a String representing the first name.
     */

    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return a String representing the last name.
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the account DAO instance.
     *
     * @param accountDao a Dao representing the account DAO.
     */

    public void setAccountDao(final Dao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * Sets the user's email address.
     *
     * @param email a String representing the email address.
     */

    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName a String representing the first name.
     */

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName a String representing the last name.
     */

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Creates a classic account with the specified currency and records its creation timestamp.
     *
     * @param currency a String representing the account currency.
     * @param timestamp an integer representing the account creation timestamp.
     */

    public void createClassicAccount(final String currency,
                                     final int timestamp) {
        try {
            ClassicAccount account = new ClassicAccount(currency, transactions, plan);
            accountDao.update(account);
            AccountCreation transaction = new AccountCreation(timestamp);
            transactions.add(transaction);
            account.getTransactionHistory().add(transaction);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Creates a savings account with the specified currency and records its creation timestamp.
     *
     * @param currency a String representing the account currency.
     * @param timestamp an integer representing the account creation timestamp.
     */

    public void createSavingsAccount(final String currency,
                                     final int timestamp,
                                     final double interestRate) {
        try {
            accountDao.update(new SavingsAccount(currency, transactions, interestRate, plan));
            transactions.add(new AccountCreation(timestamp));
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Retrieves the user's unique identifier (email).
     *
     * @return a String representing the email address.
     */

    public String getIdentifier() {
        return email;
    }

    /**
     * Deletes an account identified by its iban.
     *
     * @param iban a String representing the iban of the account to be deleted.
     */

    public void deleteAccount(final String iban) {
        try {
            accountDao.delete(iban);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    public void upgradePlan() {
        for (DaoObject accData: accountDao.getAll()) {
            Account account = (Account) accData;
            account.setPlan(plan);
        }
    }

    /**
     * Sets the list of transactions associated with the user.
     *
     * @param transactions an ArrayList of Transaction objects.
     */

    public void setTransactions(final ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Retrieves the list of transactions associated with the user.
     *
     * @return an ArrayList of Transaction objects.
     */

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
