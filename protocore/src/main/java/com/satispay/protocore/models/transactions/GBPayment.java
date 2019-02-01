package com.satispay.protocore.models.transactions;

import com.satispay.protocore.models.generic.Consumer;
import com.satispay.protocore.models.generic.PaginatedList;
import com.satispay.protocore.models.generic.Shop;

import java.util.Date;


public class GBPayment {
    public GBPayment() {
    }

    private String id;
    private String type;
    private long amountUnit;
    private String currency;
    private String status;
    private boolean statusOwnership;
    private boolean expired;
    // private Metadata metadata;
    private Sender sender;
    private Receiver receiver;
    private Receiver statusOwner;
    private DailyClosure dailyClosure;
    private Date insertDate;
    private Date expireDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(long amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isStatusOwnership() {
        return statusOwnership;
    }

    public void setStatusOwnership(boolean statusOwnership) {
        this.statusOwnership = statusOwnership;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Receiver getStatusOwner() {
        return statusOwner;
    }

    public void setStatusOwner(Receiver statusOwner) {
        this.statusOwner = statusOwner;
    }

    public DailyClosure getDailyClosure() {
        return dailyClosure;
    }

    public void setDailyClosure(DailyClosure dailyClosure) {
        this.dailyClosure = dailyClosure;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public TransactionProposal toTransactionProposal() {
        Shop shop = new Shop();
        shop.setId(getReceiver().getId());
        shop.setType(getReceiver().getType());

        Consumer consumer = new Consumer();
        consumer.setId(getSender().getId());
        consumer.setName(getSender().getName());
        consumer.setImageUrl(getSender().getProfilePictures().getData().get(0).getUrl());

        TransactionProposal historyTransactionsModel = new TransactionProposal();
        historyTransactionsModel.setTransactionId(getId());
        historyTransactionsModel.setTransactionDate(getInsertDate());
        historyTransactionsModel.setAmount(getAmountUnit());
        historyTransactionsModel.setType(getType());
        historyTransactionsModel.setDailyClosure(getDailyClosure().getId());
        historyTransactionsModel.setDailyClosureDate(getDailyClosure().getDate());
        historyTransactionsModel.setState(getStatus());
        historyTransactionsModel.setStateOwnership(isStatusOwnership());
        historyTransactionsModel.setShop(shop);
        historyTransactionsModel.setConsumer(consumer);
        historyTransactionsModel.setExpired(isExpired());
        return historyTransactionsModel;
    }

    public class DailyClosure {
        public DailyClosure() {
        }

        private String id;
        private Date date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public class Receiver {
        public Receiver() {
        }

        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class Sender {
        public Sender() {
        }

        private String id;
        private String type;
        private String name;
        private String surname;
        private PaginatedList<ProfilePicture> profilePictures;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public PaginatedList<ProfilePicture> getProfilePictures() {
            return profilePictures;
        }

        public void setProfilePictures(PaginatedList<ProfilePicture> profilePictures) {
            this.profilePictures = profilePictures;
        }
    }

    public class ProfilePicture {
        public ProfilePicture() {
        }

        private String id;
        private String url;
        private long width;
        private long height;
        private boolean isOriginal;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getWidth() {
            return width;
        }

        public void setWidth(long width) {
            this.width = width;
        }

        public long getHeight() {
            return height;
        }

        public void setHeight(long height) {
            this.height = height;
        }

        public boolean isOriginal() {
            return isOriginal;
        }

        public void setOriginal(boolean original) {
            isOriginal = original;
        }
    }
}



