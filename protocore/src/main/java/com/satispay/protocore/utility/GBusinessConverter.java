package com.satispay.protocore.utility;

import com.satispay.protocore.models.generic.PaginatedList;
import com.satispay.protocore.models.payment.Payment;
import com.satispay.protocore.models.transactions.HistoryRequestModel;
import com.satispay.protocore.models.transactions.HistoryTransactionsModel;
import com.satispay.protocore.models.transactions.RequestTransaction;
import com.satispay.protocore.models.transactions.TransactionProposal;

import java.util.ArrayList;

import static com.satispay.protocore.models.payment.Payment.FLOW_CHARGE;

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

    static public HistoryRequestModel toHistoryRequestsModel(PaginatedList<Payment> paymentPaginatedList) {
        HistoryRequestModel historyRequestModel = new HistoryRequestModel();
        historyRequestModel.setHasMore(paymentPaginatedList.isHasMore());
        historyRequestModel.setFound(paymentPaginatedList.getData().size());
        ArrayList<RequestTransaction> list = new ArrayList<>();
        historyRequestModel.setList(list);
        for (Payment payment : paymentPaginatedList.getData()) {
            if(payment.getFlow().equals(FLOW_CHARGE))
                list.add(payment.toRequestTransaction());
        }
        return historyRequestModel;
    }
}
