package org.poo.bank;

public class CardCreation extends Transaction {
    private String card;
    private String cardHolder;
    private String account;

    public CardCreation(int timestamp,
                        String card,
                        String cardHolder,
                        String account) {
        this.setTimestamp(timestamp);
        this.setDescription("New card created");
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
        this.setType("CardCreation");
    }

    public String getCard() {
        return card;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getAccount() {
        return account;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
