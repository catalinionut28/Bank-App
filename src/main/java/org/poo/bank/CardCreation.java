package org.poo.bank;

public class CardCreation extends Transaction {
    private String card;
    private String cardHolder;
    private String account;

    public CardCreation(final int timestamp,
                        final String card,
                        final String cardHolder,
                        final String account) {
        this.setTimestamp(timestamp);
        this.setDescription("New card created");
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
        this.setType("CardCreation");
    }

    /**
     * Gets the card number.
     *
     * @return the card number
     */
    public String getCard() {
        return card;
    }

    /**
     * Gets the cardholder's email.
     *
     * @return the cardholder's email
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * Gets the associated account number.
     *
     * @return the associated account number
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the card number.
     *
     * @param card the card number to set
     */
    public void setCard(final String card) {
        this.card = card;
    }

    /**
     * Sets the associated account number.
     *
     * @param account the account number to set
     */
    public void setAccount(final String account) {
        this.account = account;
    }

    /**
     * Sets the cardholder's email.
     *
     * @param cardHolder the cardholder's email to set
     */
    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
