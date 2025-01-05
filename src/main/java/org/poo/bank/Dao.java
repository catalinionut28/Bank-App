package org.poo.bank;

import java.util.List;

public interface Dao {

    /**
     * Retrieves all objects managed by the DAO.
     *
     * @return a List of DaoObject instances representing all stored objects.
     */

    List<DaoObject> getAll();

    /**
     * Retrieves an object by its unique identifier.
     *
     * @param identifier a String representing the unique identifier of the object.
     * @return a DaoObject matching the given identifier, or null if not found.
     */

    DaoObject get(String identifier);

    /**
     * Updates or inserts a data object into the DAO.
     * If the object already exists, it will be updated.
     *
     * @param object a DaoObject instance to be stored or updated.
     */

    void update(DaoObject object);

    /**
     * Deletes an object by its unique identifier.
     *
     * @param identifier a String representing the unique identifier of the object to be deleted.
     * @throws IllegalArgumentException if the identifier is not found.
     */
    void delete(String identifier);
}
