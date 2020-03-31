package com.satispay.protocore.models.transactions;

import java.util.ArrayList;

public class HistoryRequestModel {
    private boolean hasMore;
    private int found;
    private ArrayList<RequestTransaction> list;

    public HistoryRequestModel() {
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

    public ArrayList<RequestTransaction> getList() {
        return list;
    }

    public void setList(ArrayList<RequestTransaction> list) {
        this.list = list;
    }
}
