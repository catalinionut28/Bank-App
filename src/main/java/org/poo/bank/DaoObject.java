package org.poo.bank;

public interface DaoObject {

    /**
     * Retrieves the unique identifier of the data object.
     * This identifier is used to uniquely identify an object within a collection.
     *
     * @return the unique identifier of the object
     */
    String getIdentifier();
}
