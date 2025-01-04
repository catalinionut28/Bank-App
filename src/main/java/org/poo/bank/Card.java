package org.poo.bank;
import org.poo.utils.Utils;

public class Card {
    private String cardNumber;
    private String status;

    public Card() {
        cardNumber = Utils.generateCardNumber();
        System.out.println("S-a creeat cardul:" + cardNumber);
        status = "active";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

}

class OneTimeCard extends Card {

    public OneTimeCard() {
        super();
    }
}

