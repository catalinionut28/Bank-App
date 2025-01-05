package org.poo.bank;

import java.util.ArrayList;
import java.util.List;

public class DaoImpl implements Dao {
    private List<DaoObject> data;

    public DaoImpl() {
        data = new ArrayList<>();
    }

    /**
     * Retrieves all data objects stored in the list.
     *
     * @return a list of all data objects
     */
    public List<DaoObject> getAll() {
        return data;
    }

    /**
     * Retrieves a data object based on its unique identifier.
     *
     * @param identifier the unique identifier of the data object
     * @return the matching data object, or {@code null} if not found
     */
    public DaoObject get(final String identifier) {
        for (DaoObject d: data) {
            if (d.getIdentifier().equals(identifier)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Adds a new data object to the list. If the provided object is null,
     * an {@link IllegalArgumentException} is thrown.
     *
     * @param object the data object to add
     * @throws IllegalArgumentException if the object is null
     */
    public void update(final DaoObject object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException("The user/account doesn't exist");
        }
        data.add(object);
    }

    /**
     * Deletes a data object identified by its unique identifier. If no matching object
     * is found, an {@link IllegalArgumentException} is thrown.
     *
     * @param identifier the unique identifier of the object to delete
     * @throws IllegalArgumentException if the object is not found
     */
    public void delete(final String identifier) throws IllegalArgumentException {
        DaoObject object = get(identifier);
        if (object == null) {
            throw new IllegalArgumentException("No user/account found");
        }
        data.remove(object);
    }
}
