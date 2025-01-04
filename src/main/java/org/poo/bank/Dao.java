package org.poo.bank;

import java.util.List;

public interface Dao {
    public List<DaoObject> getAll();
    public DaoObject get(String identifier);
    public void update(DaoObject object);
    public void delete(String identifier);
}
