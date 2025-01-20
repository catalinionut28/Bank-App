package org.poo.bank;

import org.poo.commerciants.*;
import org.poo.graph.CurrencyGraph;
import org.poo.graph.Node;
import org.poo.plan.ServicePlan;
import org.poo.plan.SilverPlan;
import org.poo.plan.StandardPlan;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Account implements DaoObject {
    private String iban;
    private ArrayList<Card> cards;
    private double balance;
    private double minimumBalance;
    private String currency;
    private String type;
    private ArrayList<Transaction> userTransactions;
    private ArrayList<Transaction> transactionHistory;
    private HashMap<Merchant, Integer> commerciantMap;
    private SpendingThreshold spendingThreshold;
    private double spendingTotal;
    private boolean hadFoodCashback;
    private boolean hadClothesCashback;
    private boolean hadTechCashback;
    private FoodCashback foodCashback;
    private TechCashback techCashback;
    private ClothesCashback clothesCashback;
    private ServicePlan plan;

    /**
     * Retrieves the current service plan.
     * <p>
     * This method returns the {@code ServicePlan} object associated with the current instance.
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
     * This method assigns the specified {@code ServicePlan} object to the {@code plan} field,
     * updating the service plan associated with the current instance.
     * </p>
     *
     * @param plan the {@code ServicePlan} object to set as the current service plan
     */
    public void setPlan(final ServicePlan plan) {
        this.plan = plan;
    }

    /**
     * Sets the map of merchants and their associated transaction counts.
     * <p>
     * This method assigns the provided {@code HashMap} of {@code Merchant} objects and their
     * respective transaction counts to the {@code commerciantMap} field.
     * </p>
     *
     * @param commerciantMap the {@code HashMap} containing {@code Merchant} objects as keys and
     *                       their transaction counts as values
     */
    public void setCommerciantMap(final HashMap<Merchant, Integer> commerciantMap) {
        this.commerciantMap = commerciantMap;
    }

    /**
     * Sets the food cashback.
     * <p>
     * This method assigns the specified {@code FoodCashback} object to the {@code foodCashback}
     * field, updating the food cashback associated with the current instance.
     * </p>
     *
     * @param foodCashback the {@code FoodCashback} object to set
     */
    public void setFoodCashback(final FoodCashback foodCashback) {
        this.foodCashback = foodCashback;
    }

    /**
     * Sets the flag indicating whether the clothes cashback has been applied.
     * <p>
     * This method updates the {@code hadClothesCashback} field to reflect whether the clothes
     * cashback has been applied to the current instance.
     * </p>
     *
     * @param hadClothesCashback {@code true}
     * if the clothes cashback has been applied, {@code false} otherwise
     */
    public void setHadClothesCashback(final boolean hadClothesCashback) {
        this.hadClothesCashback = hadClothesCashback;
    }

    /**
     * Sets the flag indicating whether the food cashback has been applied.
     * <p>
     * This method updates the {@code hadFoodCashback} field to reflect whether the food
     * cashback has been applied to the current instance.
     * </p>
     *
     * @param hadFoodCashback {@code true}
     * if the food cashback has been applied, {@code false} otherwise
     */
    public void setHadFoodCashback(final
                                   boolean hadFoodCashback) {
        this.hadFoodCashback = hadFoodCashback;
    }

    /**
     * Sets the clothes cashback.
     * <p>
     * This method assigns the specified
     * {@code ClothesCashback} object to the {@code clothesCashback}
     * field, updating the clothes cashback associated with the current instance.
     * </p>
     *
     * @param clothesCashback the
     * {@code ClothesCashback} object to set
     */
    public void setClothesCashback(final ClothesCashback clothesCashback) {
        this.clothesCashback = clothesCashback;
    }

    /**
     * Sets the flag indicating whether the tech cashback has been applied.
     * <p>
     * This method updates the {@code hadTechCashback} field to reflect whether the tech
     * cashback has been applied to the current instance.
     * </p>
     *
     * @param hadTechCashback {@code true}
     * if the tech cashback has been applied, {@code false} otherwise
     */
    public void setHadTechCashback(final boolean hadTechCashback) {
        this.hadTechCashback = hadTechCashback;
    }

    /**
     * Sets the spending threshold.
     * <p>
     * This method assigns the specified
     * {@code SpendingThreshold} object to the {@code spendingThreshold}
     * field, updating the spending threshold associated with the current instance.
     * </p>
     *
     * @param spendingThreshold the {@code SpendingThreshold} object to set
     */
    public void setSpendingThreshold(final SpendingThreshold spendingThreshold) {
        this.spendingThreshold = spendingThreshold;
    }

    /**
     * Sets the total spending amount.
     * <p>
     * This method updates the {@code spendingTotal} field to reflect the total spending amount
     * associated with the current instance.
     * </p>
     *
     * @param spendingTotal the total spending amount to set
     */
    public void setSpendingTotal(final double spendingTotal) {
        this.spendingTotal = spendingTotal;
    }

    /**
     * Sets the tech cashback.
     * <p>
     * This method assigns the specified {@code TechCashback}
     * object to the {@code techCashback}
     * field, updating the tech cashback associated with the current instance.
     * </p>
     *
     * @param techCashback the {@code TechCashback} object to set
     */
    public void setTechCashback(final TechCashback techCashback) {
        this.techCashback = techCashback;
    }

    /**
     * Retrieves the transaction history.
     *
     * @return an ArrayList of Transaction objects representing the transaction history.
     */

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Sets the transaction history.
     *
     * @param transactionHistory an ArrayList
     *                           of Transaction objects
     *                           representing the transaction history.
     */

    public void setTransactionHistory(final ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    /**
     * Retrieves the list of cards associated with the account.
     *
     * @return an ArrayList of Card objects.
     */

    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Retrieves the account balance.
     *
     * @return the current balance as a double.
     */

    public double getBalance() {
        return balance;
    }

    /**
     * Retrieves the account currency.
     *
     * @return a String representing the currency type.
     */

    public String getCurrency() {
        return currency;
    }

    /**
     * Retrieves the IBAN of the account.
     *
     * @return a String representing the IBAN.
     */

    public String getIban() {
        return iban;
    }

    /**
     * Retrieves the account type.
     *
     * @return a String representing the account type.
     */

    public String getType() {
        return type;
    }

    /**
     * Updates the account balance.
     *
     * @param balance the new balance as a double.
     */

    public void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     * Sets the list of cards associated with the account.
     *
     * @param cards an ArrayList of Card objects.
     */

    public void setCards(final ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Sets the currency of the account.
     *
     * @param currency a String representing the currency.
     */

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Sets the IBAN of the account.
     *
     * @param iban a String representing the IBAN.
     */

    public void setIban(final String iban) {
        this.iban = iban;
    }

    /**
     * Sets the account type.
     *
     * @param type a String representing the account type.
     */

    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Sets the list of user transactions.
     *
     * @param userTransactions an ArrayList of Transaction objects.
     */

    public void setUserTransactions(final ArrayList<Transaction> userTransactions) {
        this.userTransactions = userTransactions;
    }

    /**
     * Retrieves the list of user transactions.
     *
     * @return an ArrayList of Transaction objects.
     */

    public ArrayList<Transaction> getUserTransactions() {
        return userTransactions;
    }

    /**
     * Adds funds to the account.
     *
     * @param amount the amount to be added as a double.
     */

    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     * Creates a new card and adds it to the list of cards.
     */

    public void createCard() {
        Card card = new Card();
        cards.add(card);
    }

    /**
     * Retrieves the account identifier (IBAN).
     *
     * @return a String representing the IBAN.
     */

    public String getIdentifier() {
        return iban;
    }

    /**
     * Creates a one-time use card and adds it to the list of cards.
     */

    public void createOneTimeCard() {
        cards.add(new OneTimeCard());
    }

    /**
     * Sets the minimum balance for the account.
     *
     * @param amount the minimum balance as a double.
     */

    public void setMinimumBalance(final double amount) {
        minimumBalance = amount;
    }

    /**
     * Retrieves the minimum balance of the account.
     *
     * @return the minimum balance as a double.
     */

    public double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * Retrieves a card by its card number.
     *
     * @param cardNumber a String representing the card number.
     * @return the corresponding Card object, or null if not found.
     */

    public Card getCard(final String cardNumber) {
        for (Card card: cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Makes an online payment using a specified card.
     *
     * @param amount the amount to be paid as a double.
     * @param cardNumber a String representing the card number used for payment.
     */

    public void payOnline(final double amount, final String cardNumber, final Merchant commerciant,
                          final double ronAmount) {
        balance -= amount;
        Card card = getCard(cardNumber);
        if (card.isOneTimeCard()) {
            cards.remove(card);
            createOneTimeCard();
        }
        getCashback(commerciant.getType(), amount);
        createCashback(commerciant, amount, ronAmount);
    }

    /**
     * Applies cashback based on the merchant type and the specified amount.
     * <p>
     * This method checks the merchant type and applies the corresponding cashback strategy
     * (Food, Tech, or Clothes) if available. After performing the cashback, the associated
     * cashback object is reset to {@code null}.
     * </p>
     *
     * @param commerciantType the type of the merchant (e.g., "Food", "Tech", "Clothes")
     * @param amount the amount for which cashback is to be applied
     */
    private void getCashback(final String commerciantType, final double amount) {
        CashbackContext cashbackContext = new CashbackContext(null);
        switch (commerciantType) {
            case "Food":
                if (foodCashback != null) {
                    cashbackContext.setCashbackStrategy(foodCashback);
                    addFunds(cashbackContext.performCashback(amount));
                    foodCashback = null;
                }
                break;
            case "Tech":
                if (techCashback != null) {
                    cashbackContext.setCashbackStrategy(techCashback);
                    addFunds(cashbackContext.performCashback(amount));
                    techCashback = null;
                }
                break;
            case "Clothes":
                if (clothesCashback != null) {
                    cashbackContext.setCashbackStrategy(clothesCashback);
                    addFunds(cashbackContext.performCashback(amount));
                    clothesCashback = null;
                }
                break;
            default:
                break;
        }

    }
    /**
     * Creates a cashback for a merchant based on the specified strategy.
     * <p>
     * This method handles two types of
     * cashback strategies: "nrOfTransactions" and "spendingThreshold".
     * For "nrOfTransactions", cashback
     * is granted based on the number of transactions made at
     * specific merchant types
     * (e.g., Food, Clothes, Tech). For "spendingThreshold", cashback is
     * granted once a spending threshold is met, based on the total spending amount.
     * </p>
     *
     * @param commerciant the {@code Merchant} providing cashback offers
     * @param amount the amount to be used for cashback calculation
     * @param ronAmount the amount in RON for the cashback calculation
     */
    private void createCashback(final Merchant commerciant,
                                final double amount,
                                final double ronAmount) {
        switch (commerciant.getCashbackStrategy()) {
            case "nrOfTransactions":
                if (!commerciantMap.containsKey(commerciant)) {
                    commerciantMap.put(commerciant, 1);
                } else {
                    Integer payments = commerciantMap.get(commerciant);
                    commerciantMap.put(commerciant, payments + 1);
                }
                switch (commerciant.getType()) {
                    case "Food":
                        if (hadFoodCashback) {
                            return;
                        }
                        if (commerciantMap.get(commerciant) == 2) {
                            foodCashback = new FoodCashback();
                            hadFoodCashback = true;
                        }
                        break;
                    case "Clothes":
                        if (hadClothesCashback) {
                            return;
                        }
                        if (commerciantMap.get(commerciant) == 5) {
                            clothesCashback = new ClothesCashback();
                            hadClothesCashback = true;
                        }
                        break;
                    case "Tech":
                        if (hadTechCashback) {
                            return;
                        }
                        if (commerciantMap.get(commerciant) == 10) {
                            techCashback = new TechCashback();
                            hadTechCashback = true;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "spendingThreshold":
                spendingTotal += ronAmount;
                if (spendingTotal >= 500) {
                    spendingThreshold = new ThirdSpendingThreshold(plan.getType());
                } else if (spendingTotal >= 300) {
                    spendingThreshold = new SecondSpendingThreshold(plan.getType());
                } else if (spendingTotal >= 100) {
                    spendingThreshold = new FirstSpendingThreshold(plan.getType());
                }
                if (spendingThreshold != null) {
                    CashbackContext cashbackContext = new CashbackContext(spendingThreshold);
                    addFunds(cashbackContext.performCashback(amount));
                }
                break;
            default:
                break;
        }
    }

    /**
     * Splits a payment and deducts the specified amount from the account balance.
     *
     * @param amount the amount to be deducted as a double.
     */

    public void splitPay(final double amount) {
        balance -= amount;
    }

    /**
     * Receives money and adds it to the account balance.
     *
     * @param amount the amount to be added as a double.
     */

    public void receiveMoney(final double amount) {
        balance += amount;
    }

    /**
     * Sends money to another account, processes the transaction,
     * and records the transaction history.
     *
     * @param receiver the receiving Account object.
     * @param amount the amount to be sent as a double.
     * @param exchangeGraph the CurrencyGraph object for currency conversion.
     * @param timestamp the transaction timestamp as an int.
     * @param description a String describing the transaction.
     */

    public void sendMoney(final Account receiver,
                          double amount,
                          final CurrencyGraph exchangeGraph,
                          final int timestamp,
                          final String description) {
        this.balance -= amount;
        String formattedAmount = String.valueOf(amount)
                                + " " + currency;
        SendReceive transaction = new SendReceive(timestamp,
                                                description,
                                                iban,
                                                receiver.getIban(),
                                                formattedAmount,
                                                "sent");
        userTransactions.add(transaction);
        transactionHistory.add(transaction);
        switch (plan.getType()) {
            case "standard":
                balance -= ((StandardPlan) plan).calculateCommission(amount);
                break;
            case "silver":
                double ronAmount = exchangeGraph
                        .exchange(new Node(this.getCurrency(), 1),
                                new Node("RON", 1),
                                amount);
                balance -= ((SilverPlan) plan).calculateCommission(amount, ronAmount);
                break;
            default:
                break;
        }

        if (this.currency.equals(receiver.getCurrency())) {
            receiver.receiveMoney(amount);
            formattedAmount = String.valueOf(amount)
                    + " " + receiver.getCurrency();
            SendReceive receiveTransaction =
                    new SendReceive(timestamp,
                                    description,
                                    iban,
                                    receiver.getIban(),
                                    formattedAmount,
                            "received");
            receiver.getUserTransactions()
                    .add(receiveTransaction);
            receiver.getTransactionHistory()
                    .add(receiveTransaction);
        } else {
            amount = exchangeGraph.exchange(
                    new Node(this.currency, 1),
                    new Node(receiver.getCurrency(), 1),
                    amount);
            formattedAmount = String.valueOf(amount)
                    + " " + receiver.getCurrency();
            SendReceive receiveTransaction =
                    new SendReceive(timestamp,
                            description,
                            iban,
                            receiver.getIban(),
                            formattedAmount,
                            "received");
            receiver.getUserTransactions()
                    .add(receiveTransaction);
            receiver.getTransactionHistory()
                    .add(receiveTransaction);
            receiver.receiveMoney(amount);
        }
    }
    /**
     * Pays the upgrade fee by deducting the specified amount from the balance.
     * <p>
     * This method subtracts the given {@code feeConverted} from the current balance,
     * effectively paying the upgrade fee.
     * </p>
     *
     * @param feeConverted the fee amount to be deducted from the balance
     */
    public void payUpgradeFee(final double feeConverted) {
        balance -= feeConverted;
    }
    /**
     * Withdraws cash from the account, applying the appropriate commission based on the plan.
     * <p>
     * This method checks the currency
     * and deducts the specified {@code amount} (or {@code ronAmount})
     * from the balance.
     * It also applies the commission based on the user's plan type.
     * The commission is calculated
     * using different methods depending on whether the plan is
     * "standard" or "silver".
     * </p>
     *
     * @param amount the amount to withdraw in the selected currency
     * @param ronAmount the equivalent amount in RON if the currency is "RON"
     * @param card the {@code Card} object associated with the transaction
     */
    public void withdrawCash(double amount,
                             final double ronAmount,
                             final Card card) {
        if (currency.equals("RON")) {
            balance -= ronAmount;
            amount = ronAmount;
        } else {
            balance -= amount;
        }
        switch (plan.getType()) {
            case "standard":
                balance -= ((StandardPlan) plan).calculateCommission(amount);
                break;
            case "silver":
                balance -= ((SilverPlan) plan).calculateCommission(amount, ronAmount);
                break;
            default:
                break;
        }
    }
}


