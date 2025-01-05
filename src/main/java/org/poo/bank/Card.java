package org.poo.bank;
import org.poo.utils.Utils;

public class Card {
    private final String cardNumber;
    private String status;
    protected boolean oneTime;

    public Card() {
        cardNumber = Utils.generateCardNumber();
        status = "active";
        oneTime = false;
    }

    /**
     * Gets the unique card number.
     *
     * @return the card number as a String.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Gets the current status of the card.
     *
     * @return the status of the card as a String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the card.
     *
     * @param status the new status of the card.
     */
    public void setStatus(final String status) {
        this.status = status;
    }


    /**
     * Checks if the card is a one-time use card.
     *
     * @return true if the card is a one-time card, false otherwise.
     */
    public boolean isOneTimeCard() {
        return oneTime;
    }

    /**
     * Compares the current card with another card to see if they have the same card number.
     *
     * @param obj the card to compare with the current card.
     * @return true if both cards have the same card number, false otherwise.
     */
    public boolean equals(final Card obj) {
        return cardNumber.equals(obj.getCardNumber());
    }
}

class OneTimeCard extends Card {

    OneTimeCard() {
        super();
        oneTime = true;
    }
}

