package org.poo.bank;

public class SendReceive extends Transaction {
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;

    public SendReceive(final int timestamp,
                       final String description,
                       final String senderIBAN,
                       final String receiverIBAN,
                       final String amount,
                       final String transferType) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("Transfer");
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.transferType = transferType;
    }

    /**
     * Retrieves the IBAN of the receiver in the transfer.
     *
     * @return a String representing the receiver's IBAN.
     */

    public String getReceiverIban() {
        return receiverIBAN;
    }

    /**
     * Retrieves the transfer amount.
     *
     * @return a String representing the amount of the transfer.
     */

    public String getAmount() {
        return amount;
    }

    /**
     * Retrieves the IBAN of the sender in the transfer.
     *
     * @return a String representing the sender's IBAN.
     */

    public String getSenderIban() {
        return senderIBAN;
    }

    /**
     * Retrieves the type of the transfer.
     *
     * @return a String representing the transfer type.
     */

    public String getTransferType() {
        return transferType;
    }

    /**
     * Sets the IBAN of the receiver in the transfer.
     *
     * @param receiverIBAN a String representing the receiver's IBAN.
     */

    public void setReceiverIBAN(final String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    /**
     * Sets the transfer amount.
     *
     * @param amount a String representing the amount of the transfer.
     */

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * Sets the IBAN of the sender in the transfer.
     *
     * @param senderIBAN a String representing the sender's IBAN.
     */

    public void setSenderIBAN(final String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    /**
     * Sets the type of the transfer.
     *
     * @param transferType a String representing the transfer type.
     */

    public void setTransferType(final String transferType) {
        this.transferType = transferType;
    }
}
