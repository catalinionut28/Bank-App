package org.poo.bank;

public class CardDestruction extends Transaction {
    private String card;
    private String cardHolder;
    private String account;

    public CardDestruction(final int timestamp,
                           final String card,
                           final String cardHolder,
                           final String account) {
        this.setTimestamp(timestamp);
        this.setType("CardDestruction");
        this.setDescription("The card has been destroyed");
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }

    /**
     * Retrieves the associated account number.
     *
     * @return a String representing the account number.
     */

    public String getAccount() {
        return account;
    }

    /**
     * Retrieves the email of the cardholder.
     *
     * @return a String representing the cardholder's email.
     */

    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * Retrieves the card number.
     *
     * @return a String representing the card number.
     */

    public String getCard() {
        return card;
    }

    /**
     * Sets the card number.
     *
     * @param card a String representing the card number.
     */

    public void setCard(final String card) {
        this.card = card;
    }

    /**
     * Sets the cardholder's email.
     *
     * @param cardHolder a String representing the cardholder's email.
     */

    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }

    /**
     * Sets the associated account number.
     *
     * @param account a String representing the account number.
     */

    public void setAccount(final String account) {
        this.account = account;
    }
}
