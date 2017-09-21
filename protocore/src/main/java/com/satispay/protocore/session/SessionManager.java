package com.satispay.protocore.session;

import com.satispay.protocore.persistence.SecurePersistenceManager;

public interface SessionManager {
    SecurePersistenceManager getSecurePersistenceManager();

    default void clearData() {
        getSecurePersistenceManager().clearData();
    }
}
