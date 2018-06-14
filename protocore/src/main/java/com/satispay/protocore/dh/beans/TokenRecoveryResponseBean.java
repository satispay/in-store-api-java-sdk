package com.satispay.protocore.dh.beans;

import java.util.List;

public class TokenRecoveryResponseBean {
    private boolean hasMore;
    private List<StoreDetails> data;

    public TokenRecoveryResponseBean(boolean hasMore, List<StoreDetails> data) {
        this.hasMore = hasMore;
        this.data = data;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<StoreDetails> getData() {
        return data;
    }

    public void setData(List<StoreDetails> data) {
        this.data = data;
    }

    static public class StoreDetails {
        private String id;
        private String name;
        private String address;
        private String phoneNumber;
        private String emailAddress;

        public StoreDetails(String id, String name, String address, String phoneNumber, String emailAddress) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.emailAddress = emailAddress;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }
    }
}
