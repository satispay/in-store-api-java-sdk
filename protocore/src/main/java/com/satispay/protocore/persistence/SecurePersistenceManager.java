package com.satispay.protocore.persistence;

/**
 * Describes how a secure persistence manager should behave.
 */
public interface SecurePersistenceManager {

    String KMASTER_KEY = "kMaster";
    String KSAFE_APP_KEY = "kSafeApp";
    String ACTIVE_KEY = "active";
    String SEQUENCE_KEY = "sequence";
    String USER_KEY_ID_KEY = "userKeyId";

    /**
     * Persist securely a value, indexing it by a certain key
     *
     * @param key   the index used to retrieve the stored value
     * @param value the value to store
     */
    void persistSecurely(String key, String value);

    /**
     * Retrieve the value indexed by the provided key.
     *
     * @param key the key which point to the desired value
     * @return the value indexed by the provided key
     */
    String getPersistedData(String key);

    /**
     * This method is triggered when the session is finished. This means a 401 HTTP error is received.
     * It could be triggers also manually if a logout process is needed by the implementor.
     */
    void clearData();

}
