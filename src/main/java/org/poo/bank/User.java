package org.poo.bank;

import org.poo.fileio.UserInput;
import org.poo.plan.StandardPlan;
import org.poo.plan.StudentPlan;
import org.poo.utils.Utils;
import org.poo.plan.ServicePlan;
import org.poo.visitor.PaymentVisitor;
import org.poo.visitor.UserDecision;

import java.util.ArrayList;


public class User implements DaoObject, UserDecision {
    private String email;
    private String firstName;
    private String lastName;
    private Dao accountDao;
    private int age;
    private String occupation;
    private ServicePlan plan;
    private ArrayList<Transaction> transactions;
    private ArrayList<PendingPayment> pendingPayments;



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
        this.pendingPayments = new ArrayList<>();
    }

    /**
     * Retrieves the list of pending payments.
     * <p>
     * This method returns an {@code ArrayList}
     * containing all {@code PendingPayment} objects
     * that are currently marked as pending.
     * </p>
     *
     * @return an {@code ArrayList} of
     * {@code PendingPayment} objects representing the pending payments
     */
    public ArrayList<PendingPayment> getPendingPayments() {
        return pendingPayments;
    }

    /**
     * Sets the list of pending payments.
     * <p>
     * This method assigns the specified
     * {@code ArrayList} of {@code PendingPayment} objects
     * to the {@code pendingPayments} field,
     * updating the list of payments that are currently pending.
     * </p>
     *
     * @param pendingPayments an {@code ArrayList}
     * of {@code PendingPayment} objects to set as the pending payments
     */
    public void setPendingPayments(final
                                   ArrayList<PendingPayment> pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

    /**
     * Retrieves the current service plan.
     * <p>
     * This method returns the {@code ServicePlan}
     * object associated with the current instance.
     * The service plan represents the details of the subscribed or assigned plan.
     * </p>
     *
     * @return the {@code ServicePlan} object representing the current service plan
     */
    public ServicePlan getPlan() {
        return plan;
    }


    /**
     * Sets the service plan.
     * <p>
     * This method assigns the specified
     * {@code ServicePlan} object to the {@code plan} field,
     * updating the service plan associated with the current instance.
     * </p>
     *
     * @param plan the {@code ServicePlan} object to set as the current service plan
     */
    public void setPlan(final ServicePlan plan) {
        this.plan = plan;
    }


    /**
     * Retrieves the occupation.
     * <p>
     * This method returns the {@code occupation}
     * associated with the current instance.
     * The occupation represents the job, profession, or role of the individual.
     * </p>
     *
     * @return a {@code String} representing the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Retrieves the age.
     * <p>
     * This method returns the {@code age} of
     * the individual associated with the current instance.
     * The age is represented as an integer value.
     * </p>
     *
     * @return an {@code int} representing the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the occupation.
     * <p>
     * This method assigns the specified {@code String}
     * value to the {@code occupation} field,
     * updating the occupation of the
     * individual associated with the current instance.
     * </p>
     *
     * @param occupation a {@code String} representing the occupation to be set
     */
    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }

    /**
     * Sets the age.
     * <p>
     * This method assigns the specified
     * {@code int} value to the {@code age} field,
     * updating the age of the individual
     * associated with the current instance.
     * </p>
     *
     * @param age an {@code int} representing the age to be set
     */
    public void setAge(final int age) {
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
     * Creates a classic account with the
     * specified currency and records its creation timestamp.
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
    /**
     * Upgrades the service plan for all accounts.
     * <p>
     * This method iterates over all accounts from the {@code accountDao} and updates each
     * account's plan to the current service plan ({@code plan}).
     * </p>
     */
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

    /**
     * Accepts a payment visitor to perform actions based on the current payment state.
     * <p>
     * This method calls the {@code visit} method of the provided {@code PaymentVisitor},
     * passing the current object as the argument for processing.
     * </p>
     *
     * @param payment the {@code PaymentVisitor} that will perform actions on the current payment
     */
    @Override
    public void accept(final PaymentVisitor payment) {
        payment.visit(this);
    }
    /**
     * Rejects a payment visitor to handle rejection actions for the current payment state.
     * <p>
     * This method calls the {@code visit} method of the provided {@code PaymentVisitor},
     * passing the current object as the argument for processing.
     * </p>
     *
     * @param payment the {@code PaymentVisitor} that will perform rejection actions on the payment
     */
    @Override
    public void reject(final PaymentVisitor payment) {
        payment.visit(this);
    }
}
