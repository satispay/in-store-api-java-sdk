package com.satispay.protocore.dh;

import retrofit2.Retrofit;

public abstract class AbstractDHService {
    protected Retrofit retrofit;
    protected DH dh;

    public DH getDh() {
        return dh;
    }
}
