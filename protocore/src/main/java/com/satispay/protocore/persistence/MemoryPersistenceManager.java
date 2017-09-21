package com.satispay.protocore.persistence;

import java.util.HashMap;

/**
 * Here is a simple implementation of a secure persistence manager !!WARN!! this implementation does not persist data in
 * a safe and secure way it just use a {@link HashMap} to save data in memory, killing the application the saved data
 * will be lost.
 */
public class MemoryPersistenceManager implements SecurePersistenceManager {

    private HashMap<String, String> valuesMap;

    private static MemoryPersistenceManager instance;

    private MemoryPersistenceManager() {
        valuesMap = new HashMap<>();
    }

    public static MemoryPersistenceManager getInstance() {

        if (instance == null) {
            instance = new MemoryPersistenceManager();
        }
        return instance;

    }

    @Override
    public void persistSecurely(String key, String value) {
        valuesMap.put(key, value);
    }

    @Override
    public String getPersistedData(String key) {
        return valuesMap.get(key);
    }

    @Override
    public void clearData() {
        valuesMap.clear();
    }
}
