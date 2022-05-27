package com.satispay.protocore.models.payment;

import com.satispay.protocore.models.generic.Consumer;
import com.satispay.protocore.models.generic.PaginatedList;
import com.satispay.protocore.models.generic.Shop;
import com.satispay.protocore.models.transactions.TransactionProposal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.satispay.protocore.models.transactions.TransactionProposal.TYPE_PAYMENT;
import static com.satispay.protocore.models.transactions.TransactionProposal.TYPE_REQUEST;


public class Payment {
    public Payment() {
    }

    public static String FLOW_CHARGE = "CHARGE";

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
    private String flow;
    private String description;
    private String comment;

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

    public String getFlow() { return flow; }

    public void setFlow(String flow) { this.flow = flow; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public TransactionProposal toTransactionProposal() {
        DailyClosure dailyClosure = getDailyClosure();
        Shop shop = new Shop();
        Consumer consumer = new Consumer();
        Sender sender = getSender();
        Receiver receiver = getReceiver();
        if (receiver != null) {
            if(Objects.equals(receiver.getType(), "SHOP")) {
                shop.setId(receiver.getId());
                shop.setType(receiver.getType());
            } else if (Objects.equals(receiver.getType(), "CONSUMER")) {
                consumer.setId(receiver.getId());
                consumer.setName(receiver.getName());
                try {
                    String imageUrl = receiver.getProfilePictures().getData().get(0).getUrl();
                    consumer.setImageUrl(imageUrl);
                } catch (Exception e) {
                    // ignore missing url
                }
            }
        }
        if (sender != null) {
            if(Objects.equals(sender.getType(), "SHOP")) {
                shop.setId(sender.getId());
                shop.setType(sender.getType());
            } else if (Objects.equals(sender.getType(), "CONSUMER")) {
                consumer.setId(sender.getId());
                consumer.setName(sender.getName());
                try {
                    String imageUrl = sender.getProfilePictures().getData().get(0).getUrl();
                    consumer.setImageUrl(imageUrl);
                } catch (Exception e) {
                    // ignore missing url
                }
            }
        }

        TransactionProposal historyTransactionsModel = new TransactionProposal();
        historyTransactionsModel.setTransactionId(getId());
        historyTransactionsModel.setTransactionDate(getInsertDate());
        historyTransactionsModel.setAmount(getAmountUnit());
        historyTransactionsModel.setType(getType());

        if (dailyClosure != null) {
            historyTransactionsModel.setDailyClosure(dailyClosure.getId());
            historyTransactionsModel.setDailyClosureDate(dailyClosure.getDate());
        }
        else{

            historyTransactionsModel.setDailyClosure(new SimpleDateFormat("yyyyMMdd", Locale.ITALY).format( getInsertDate()));
            historyTransactionsModel.setDailyClosureDate(getInsertDate());
        }

        String state;
        String status = getStatus();
        switch (status) {
            case "ACCEPTED":
                state = "APPROVED";
                break;
            case "CANCELED":
                state = "CANCELED";
                break;
            case "PENDING":
                state = "PENDING";
                break;
            default:
                state = status;
                break;
        }
        historyTransactionsModel.setState(state);
        historyTransactionsModel.setStateOwnership(isStatusOwnership());
        historyTransactionsModel.setShop(shop);
        historyTransactionsModel.setConsumer(consumer);
        historyTransactionsModel.setExpired(isExpired());
        historyTransactionsModel.setTransactionType((getFlow() == null ) ? TYPE_PAYMENT : TYPE_REQUEST);
        historyTransactionsModel.setComment(getComment());
        historyTransactionsModel.setDescription(getDescription());
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
        public PaginatedList<ProfilePicture> getProfilePictures() {
            return profilePictures;
        }
        public void setProfilePictures(PaginatedList<ProfilePicture> profilePictures) {
            this.profilePictures = profilePictures;
        }
        public String getSurname() {
            return surname;
        }
        public void setSurname(String surname) {
            this.surname = surname;
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



