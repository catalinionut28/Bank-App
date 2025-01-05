package org.poo.bank;

public class DeleteError extends Transaction {
    public DeleteError(final int timestamp, final String description) {
        this.setTimestamp(timestamp);
        this.setDescription(description);
        this.setType("DeleteError");
    }
}
