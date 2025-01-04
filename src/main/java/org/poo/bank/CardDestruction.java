package org.poo.bank;

public class CardDestruction extends Transaction {
    private String card;
    private String cardHolder;
    private String account;

    public CardDestruction(int timestamp,
                           String card,
                           String cardHolder,
                           String account) {
        this.setTimestamp(timestamp);
        this.setType("CardDestruction");
        this.setDescription("The card has been destroyed");
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
