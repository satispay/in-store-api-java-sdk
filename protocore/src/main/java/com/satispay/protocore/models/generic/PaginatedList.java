package com.satispay.protocore.models.generic;

import java.util.ArrayList;

public class PaginatedList <T> {
    private boolean hasMore;
    private ArrayList<T> data;

    public PaginatedList() {
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
