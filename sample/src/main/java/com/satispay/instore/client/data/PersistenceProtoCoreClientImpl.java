package com.satispay.instore.client.data;

import com.satispay.instore.client.SatispayInStoreApplication;
import com.satispay.protocore.ProtoCoreMessage;
import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.active.PersistenceProtoCore;
import com.satispay.protocore.active.ProtoCoreProvider;
import com.satispay.protocore.active.SdkDeviceInfo;
import com.satispay.protocore.models.transactions.TransactionProposal;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.PersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.session.SessionManager;
import rx.Observable;

import java.util.ArrayList;

/**
 * It might be important to implement the {@link PersistenceProtoCore} interface using the singleton pattern, depending on the client
 * implementor architecture.
 */
public class PersistenceProtoCoreClientImpl implements PersistenceProtoCore {

    private static volatile PersistenceProtoCoreClientImpl instance = new PersistenceProtoCoreClientImpl();

    public static PersistenceProtoCoreClientImpl getInstance() {
        return instance;
    }

    private PersistenceProtoCoreClientImpl() {
    }

    @Override
    public PersistenceManager getPersistenceManager() {
        return new PersistenceManager() {

            @Override
            public void persistTransactions(ArrayList<TransactionProposal> transactionProposals) {
                //==> Here data should be persisted in some way if needed.
            }

            @Override
            public void persistClosedTransaction(TransactionProposal transactionProposal) {
                //==> Here data should be persisted in some way if needed.
            }

            @Override
            public void persistTransactionsPolling(ArrayList<TransactionProposal> transactionProposals) {
                //==> Here data should be persisted in some way if needed.
            }

        };
    }

    @Override
    public ProtoCoreProvider getProtoCoreProvider() {
        return new ProtoCoreProvider() {
            @Override
            public SatispayContext getSatispayContext() {
                return SatispayInStoreApplication.SATISPAY_CONTEXT;
            }

            @Override
            public SecurePersistenceManager getSecurePersistenceManager() {
                return MemoryPersistenceManager.getInstance();
            }

            @Override
            public SessionManager getSessionManager() {
                return SessionManagerClientImpl.getInstance();
            }

            @Override
            public SdkDeviceInfo getSdkDeviceInfo() {
                return SatispayInStoreApplication.SDK_DEVICE_INFO;
            }

        };
    }

    @Override
    public Observable<ProtoCoreMessage> testSignature() {
        return null;
    }
}