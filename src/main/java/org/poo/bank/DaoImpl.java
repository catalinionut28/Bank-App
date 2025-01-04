package org.poo.bank;

import java.util.ArrayList;
import java.util.List;

public class DaoImpl implements Dao {
    List<DaoObject> data;

    public DaoImpl() {
        data = new ArrayList<>();
    }

    public List<DaoObject> getAll() {
        return data;
    }

    public DaoObject get(String identifier) {
        for (DaoObject d: data) {
            System.out.println("Identifcator: " + d.getIdentifier());
            if (d.getIdentifier().equals(identifier)) {
                return d;
            }
        }
        return null;
    }

    public void update(DaoObject object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException("The user/account doesn't exist");
        }
        data.add(object);
    }

    public void delete(String identifier) throws IllegalArgumentException {
        DaoObject object = get(identifier);
        if (object == null) {
            throw new IllegalArgumentException("No user/account found");
        }
        data.remove(object);
    }




}
