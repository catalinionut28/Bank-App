package org.poo.bank;

import org.poo.fileio.UserInput;

import java.util.ArrayList;


public class User implements DaoObject {
    private String email;
    private String firstName;
    private String lastName;
    private Dao accountDao;
    private ArrayList<Transaction> transactions;

    public User(UserInput userInput) {
        this.email = userInput.getEmail();
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();;
        this.accountDao = new DaoImpl();
        this.transactions = new ArrayList<>();
    }

    public Dao getAccountDao() {
        return accountDao;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAccountDao(Dao accountDao) {
        this.accountDao = accountDao;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void createClassicAccount(String currency, int timestamp) throws IllegalArgumentException {
        try {
            ClassicAccount account = new ClassicAccount(currency, transactions);
            accountDao.update(account);
            AccountCreation transaction = new AccountCreation(timestamp);
            transactions.add(transaction);
            account.getTransactionHistory().add(transaction);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    public void createSavingsAccount(String currency, int timestamp) throws IllegalArgumentException {
        try {
            accountDao.update(new SavingsAccount(currency, transactions));
            transactions.add(new AccountCreation(timestamp));
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    public String getIdentifier() {
        return email;
    }

    public void deleteAccount(String IBAN) {
        try {
            accountDao.delete(IBAN);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
