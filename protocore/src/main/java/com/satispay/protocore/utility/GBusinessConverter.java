package com.satispay.protocore.utility;

import com.satispay.protocore.models.generic.PaginatedList;
import com.satispay.protocore.models.payment.Payment;
import com.satispay.protocore.models.transactions.HistoryTransactionsModel;
import com.satispay.protocore.models.transactions.TransactionProposal;

import java.util.ArrayList;

public class GBusinessConverter {

    static public HistoryTransactionsModel toHistoryTransactionsModel(PaginatedList<Payment> paymentPaginatedList) {
        HistoryTransactionsModel historyTransactionsModel = new HistoryTransactionsModel();
        historyTransactionsModel.setHasMore(paymentPaginatedList.isHasMore());
        historyTransactionsModel.setFound(paymentPaginatedList.getData().size());
        ArrayList<TransactionProposal> list = new ArrayList<>();
        historyTransactionsModel.setList(list);
        for (Payment payment : paymentPaginatedList.getData()) {
                list.add(payment.toTransactionProposal());
        }
        return historyTransactionsModel;
    }
}

