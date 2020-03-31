package com.satispay.protocore.models.transactions;

import com.satispay.protocore.models.generic.Consumer;
import com.satispay.protocore.models.generic.Shop;

import java.util.Date;

public class RequestTransaction {

    public static String TYPE_PAYMENT = "REQUEST";

    private String requestId;
    private Date requestTransactionDate;
    private Long amount;
    private String type;
    private String dailyClosure;
    private Date dailyClosureDate;
    private String state;
    private Boolean stateOwnership;
    private Boolean expired;
    private Shop shop;
    private Consumer consumer;
    private String requestTransactionType;
    private String comment;


    public RequestTransaction() {

    }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Date getRequestTransactionDate() {
            return requestTransactionDate;
        }

        public void setRequestTransactionDate(Date requestTransactionDate) {
            this.requestTransactionDate = requestTransactionDate;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDailyClosure() {
            return dailyClosure;
        }

        public void setDailyClosure(String dailyClosure) {
            this.dailyClosure = dailyClosure;
        }

        public Date getDailyClosureDate() {
            return dailyClosureDate;
        }

        public void setDailyClosureDate(Date dailyClosureDate) {
            this.dailyClosureDate = dailyClosureDate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Boolean getStateOwnership() {
            return stateOwnership;
        }

        public void setStateOwnership(Boolean stateOwnership) {
            this.stateOwnership = stateOwnership;
        }

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }

        public Consumer getConsumer() {
            return consumer;
        }

        public void setConsumer(Consumer consumer) {
            this.consumer = consumer;
        }

        public boolean isExpired() {
            return expired;
        }

        public void setExpired(Boolean expired) {
            this.expired = expired;
        }

        public String getRequestTransactionType() { return requestTransactionType; }

        public void setRequestTransactionType(String requestTransactionType) { this.requestTransactionType = requestTransactionType; }

        public String getComment() { return comment; }

        public void setComment(String comment) { this.comment = comment; }

        public enum TransactionState {
            APPROVED("APPROVED"),
            CANCELED("CANCELED"),
            PENDING("PENDING"),
            ACCEPTING("ACCEPTING"),
            REFUSING("REFUSING"),
            FAILED_OPERATION_NOT_ALLOWED("FAILED_OPERATION_NOT_ALLOWED"),
            FAILED_ACCEPTING("FAILED_ACCEPTING"),
            FAILED_REFUSING("FAILED_REFUSING"),
            PENDING_APPROVAL("PENDING_APPROVAL"),
            PENDING_REFUSAL("PENDING_REFUSAL"),
            PENDING_DELETED("PENDING_DELETED");

            String rawValue;

            TransactionState(String value) {
                rawValue = value;
            }

            public String getRawValue() {
                return rawValue;
            }


        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TransactionProposal && ((TransactionProposal) obj).getTransactionId().equals(requestId);
        }

        @Override
        public String toString() {
            return consumer.getName() + " is paying " + amount + " cents";
        }
}

