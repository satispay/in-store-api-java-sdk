package com.satispay.protocore;

import com.satispay.protocore.active.PersistenceProtoCore;
import com.satispay.protocore.active.ProtoCoreProvider;
import com.satispay.protocore.active.SdkDeviceInfo;
import com.satispay.protocore.dh.DHFlow;
import com.satispay.protocore.dh.UptimeMillisProvider;
import com.satispay.protocore.models.analytics.AppStartedBean;
import com.satispay.protocore.models.profile.ProfileMeV1;
import com.satispay.protocore.models.transactions.TransactionProposal;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.PersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.session.SessionManager;
import rx.Observable;

import java.util.ArrayList;

public class TestMain {

    /**
     * Here is a sample main which show how to instantiate a Protocore instance using Retrofit
     * and making some sample requests
     */
    public static void main(String[] args) {
        final SatispayContext satispayContext = SatispayContext.TEST;
        DHFlow dhFlow = new DHFlow() {
            @Override
            public SatispayContext getSatispayContext() {
                return satispayContext;
            }

            @Override
            public SecurePersistenceManager getSecurePersistenceManager() {
                return MemoryPersistenceManager.getInstance();
            }

            @Override
            public UptimeMillisProvider getUptimeMillisProvider() {
                return () -> 1000L;
            }
        };

        dhFlow.performExchange()
                .switchMap(exchangeResponseBean -> dhFlow.performChallenge())
                .switchMap(dhEncryptedResponseBean -> dhFlow.performTokenVerification("TU76JQ"))
                .switchMap(dhEncryptedResponseBean -> {
                    PersistenceProtoCore persistenceProtoCore = new PersistenceProtoCore() {

                        @Override
                        public PersistenceManager getPersistenceManager() {
                            return new PersistenceManager() {
                                @Override
                                public void persistTransactions(ArrayList<TransactionProposal> transactionProposals) {
                                }


                                @Override
                                public void persistClosedTransaction(TransactionProposal transactionProposal) {
                                }

                                @Override
                                public void persistTransactionsPolling(ArrayList<TransactionProposal> transactionProposals) {
                                }
                            };
                        }

                        @Override
                        public ProtoCoreProvider getProtoCoreProvider() {
                            return new ProtoCoreProvider() {
                                @Override
                                public SatispayContext getSatispayContext() {
                                    return satispayContext;
                                }

                                @Override
                                public SecurePersistenceManager getSecurePersistenceManager() {
                                    return MemoryPersistenceManager.getInstance();
                                }

                                @Override
                                public SessionManager getSessionManager() {
                                    return new SessionManager() {
                                        @Override
                                        public SecurePersistenceManager getSecurePersistenceManager() {
                                            return MemoryPersistenceManager.getInstance();
                                        }

                                        @Override
                                        public void clearData() {

                                        }
                                    };
                                }

                                @Override
                                public SdkDeviceInfo getSdkDeviceInfo() {
                                    return null;
                                }
                            };
                        }
                    };

                    return persistenceProtoCore
                            .appStarted(new AppStartedBean("", null))
                            .switchMap(
                                    aVoid -> {
                                    //    securePersistenceManager.persistSecurely(SecurePersistenceManager.SEQUENCE_KEY, "2");
                                        Observable<ProfileMeV1>  result =  persistenceProtoCore.profileMeV1();
                                        result.subscribe(
                                                profileMeV1 ->{
                                                   System.out.println(ProfileMeV1.MobilityType.BRICK_AND_MORTAR.getRawValue().equals(profileMeV1.getModel()));
                                                }
                                        );
                                        return result;
                                    }
                            );
                }).subscribe();

        while (true) {
        }
    }
}
