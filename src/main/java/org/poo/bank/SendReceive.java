package org.poo.bank;

public class SendReceive extends Transaction {
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;

    public SendReceive(int timestamp,
                       String description,
                       String senderIBAN,
                       String receiverIBAN,
                       String amount,
                       String transferType) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("Transfer");
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.transferType = transferType;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public String getAmount() {
        return amount;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setReceiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
