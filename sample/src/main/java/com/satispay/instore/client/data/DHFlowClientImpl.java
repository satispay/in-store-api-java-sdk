package com.satispay.instore.client.data;

import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.dh.DHFlow;
import com.satispay.protocore.dh.UptimeMillisProvider;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;

/**
 * Here is an implementation of the DHFlow interface. Usually the only methods to override are getSecurePersistenceManager
 * and getUptimeMillisProvider.
 * It might be important to implement the {@link DHFlow} interface using the singleton pattern, depending on the client
 * implementor architecture.
 */
public class DHFlowClientImpl implements DHFlow {

    private static volatile DHFlowClientImpl instance = new DHFlowClientImpl();

    public static DHFlowClientImpl getInstance() {
        return instance;
    }

    private DHFlowClientImpl() {
    }

    @Override
    public SatispayContext getSatispayContext() {
        return SatispayContext.STAGING;
    }

    @Override
    public SecurePersistenceManager getSecurePersistenceManager() {
        return MemoryPersistenceManager.getInstance();
    }

    @Override
    public UptimeMillisProvider getUptimeMillisProvider() {
        return UptimeMillisProviderClientImpl.getInstance();
    }
}