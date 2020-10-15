package com.satispay.protocore.models.transactions;

import java.util.ArrayList;

public class HistoryTransactionsModel {
    private boolean hasMore;
    private int found;
    private ArrayList<TransactionProposal> list;

    public HistoryTransactionsModel() {
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public ArrayList<TransactionProposal> getList() {
        return list;
    }

    public ArrayList<TransactionProposal> getFilteredList() {
        ArrayList<TransactionProposal> filteredList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            TransactionProposal proposal = list.get(i);
            if (proposal.getConsumer().getId() != null)
                filteredList.add(proposal);
        }

        return filteredList;
    }

    public void setList(ArrayList<TransactionProposal> list) {
        this.list = list;
    }
}
