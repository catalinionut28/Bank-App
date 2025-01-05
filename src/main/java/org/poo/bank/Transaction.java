package org.poo.bank;

public abstract class Transaction {
    private String description;
    private int timestamp;
    private String type;

    /**
     * Retrieves the timestamp of the transaction.
     *
     * @return an integer representing the timestamp.
     */

    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieves the description of the transaction.
     *
     * @return a String representing the transaction description.
     */

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description a String representing the transaction description.
     */

    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the timestamp of the transaction.
     *
     * @param timestamp an integer representing the transaction timestamp.
     */

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the type of the transaction.
     *
     * @return a String representing the transaction type.
     */

    public String getType() {
        return type;
    }

    /**
     * Sets the type of the transaction.
     *
     * @param type a String representing the transaction type.
     */

    public void setType(final String type) {
        this.type = type;
    }
}

