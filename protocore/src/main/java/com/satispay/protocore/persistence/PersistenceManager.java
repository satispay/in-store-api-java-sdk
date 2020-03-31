package com.satispay.protocore.persistence;

import com.satispay.protocore.models.transactions.RequestTransaction;
import com.satispay.protocore.models.transactions.TransactionProposal;

import java.util.ArrayList;

public interface PersistenceManager {

    /**
     * This method persists a group of transactions
     *
     * @param transactionProposals the list of transactions to persist
     */
    void persistTransactions(ArrayList<TransactionProposal> transactionProposals);


    void persistRequest(ArrayList<RequestTransaction> requestTransactions);

    /**
     * This method persists the bean returned by closing a transaction which represents part of
     * the closed transactions
     *
     * @param transactionProposal the transaction to persist
     */
    void persistClosedTransaction(TransactionProposal transactionProposal);

    /**
     * This method persists a list of transactions returned by the polling
     *
     * @param transactionProposals the list of pending transactions to persist
     */
    void persistTransactionsPolling(ArrayList<TransactionProposal> transactionProposals);


    void persistRequestPolling(ArrayList<RequestTransaction> requestTransactions);

}
