package com.satispay.instore.client.controller;

import com.satispay.instore.client.data.PersistenceProtoCoreClientImpl;
import com.satispay.protocore.active.PersistenceProtoCore;
import com.satispay.protocore.active.ProtoCoreHttpClientProvider;
import com.satispay.protocore.models.transactions.CloseTransaction;
import com.satispay.protocore.models.transactions.TransactionProposal;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import okhttp3.Request;
import okhttp3.Response;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.satispay.instore.client.SatispayInStoreApplication.SDK_DEVICE_INFO;

public class SecondPageController implements Initializable {

    public TextField profileMeInfo;
    public ImageView profileImage;
    public TableView<TransactionProposal> transactionsTable;
    public Button retrieveInfoButton;
    public Button pendingTransactions;
    public Button acceptButton;
    public Button refuseButton;
    public Button historyTransactions;
    public Button refundButton;
    public Label result;

    private PersistenceProtoCore persistenceProtoCore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initRetrieveInfoButton();
        initPendingTransactionsButton();
        initTransactionHistoryButton();
        initAcceptButton();
        initRefuseButton();
        initRefundButton();

        persistenceProtoCore = PersistenceProtoCoreClientImpl.getInstance();
    }

    private void initTable() {
        TableColumn<TransactionProposal, String> columnName = new TableColumn<>("State");
        columnName.setCellValueFactory(new PropertyValueFactory<>("state"));
        columnName.setMinWidth(100D);

        TableColumn<TransactionProposal, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setMinWidth(100D);

        TableColumn<TransactionProposal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateColumn.setMinWidth(300D);

        TableColumn<TransactionProposal, String> uidColumn = new TableColumn<>("ID");
        uidColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        uidColumn.setMinWidth(100D);

        transactionsTable.getColumns().addAll(Arrays.asList(columnName, amountColumn, dateColumn, uidColumn));
    }

    private void initRetrieveInfoButton() {
        retrieveInfoButton.setOnAction(event -> {

            // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
            // here a re some reference:
            //  - http://reactivex.io
            //  - https://github.com/ReactiveX/RxJava
            persistenceProtoCore
                    .profileMe()
                    .take(1)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(
                            profileMe -> Platform.runLater(() -> {
                                profileMeInfo.setText(
                                        profileMe.getShop().getName() + profileMe.getShop().getPhoneNumber() +
                                                profileMe.getShop().getAddress().getAddress()
                                );

                                try {
                                    Request request = new Request.Builder().url(new URL(profileMe.getShop().getImageUrl())).build();
                                    Response response = ProtoCoreHttpClientProvider.getInstance().getProtocoreClientNoSignatureVerify(persistenceProtoCore.getProtoCoreProvider().getSatispayContext(), MemoryPersistenceManager.getInstance(), SDK_DEVICE_INFO).newCall(request).execute();

                                    profileImage.setImage(new Image(response.body().byteStream(), 70, 70, false, false));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            })
                    );
        });

    }

    private void initAcceptButton() {
        acceptButton.setDisable(true);
        acceptButton.setOnAction(event -> {
            TransactionProposal selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {

                // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
                // here a re some reference:
                //  - http://reactivex.io
                //  - https://github.com/ReactiveX/RxJava
                persistenceProtoCore
                        .closeTransaction(new CloseTransaction(TransactionProposal.TransactionState.APPROVED.getRawValue()), selectedItem.getTransactionId())
                        .take(1)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(
                                transactionProposal -> Platform.runLater(() -> {
                                    transactionProposal.getStateOwnership();

                                    transactionsTable.getItems().clear();
                                    transactionsTable.refresh();

                                    disableAllButtons();
                                    result.setText("OK");
                                }),
                                throwable -> Platform.runLater(() -> result.setText("KO check logs"))
                        );
            }
        });
    }

    private void disableAllButtons() {
        acceptButton.setDisable(true);
        refundButton.setDisable(true);
        refuseButton.setDisable(true);
    }

    private void initRefuseButton() {
        refuseButton.setDisable(true);
        refuseButton.setOnAction(event -> {
            TransactionProposal selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {

                // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
                // here a re some reference:
                //  - http://reactivex.io
                //  - https://github.com/ReactiveX/RxJava
                persistenceProtoCore
                        .closeTransaction(new CloseTransaction(TransactionProposal.TransactionState.CANCELED.getRawValue()), selectedItem.getTransactionId())
                        .take(1)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(
                                transactionProposal -> Platform.runLater(() -> {
                                    transactionsTable.getItems().clear();
                                    transactionsTable.refresh();

                                    disableAllButtons();
                                    result.setText("OK");
                                }),
                                throwable -> Platform.runLater(() -> result.setText("KO check logs"))
                        );
            }
        });
    }

    private void initPendingTransactionsButton() {
        pendingTransactions.setOnAction(event -> {
            clearTable();
            result.setText("");

            // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
            // here a re some reference:
            //  - http://reactivex.io
            //  - https://github.com/ReactiveX/RxJava
            persistenceProtoCore
                    .getTransactionHistory(20, null, "proposed")
                    .subscribeOn(Schedulers.newThread())
                    .take(1)
                    .subscribe(historyTransactionsModel ->
                            Platform.runLater(() -> {
                                        acceptButton.setDisable(false);
                                        refuseButton.setDisable(false);
                                        refundButton.setDisable(true);

                                        transactionsTable.setItems(
                                                FXCollections.observableList(historyTransactionsModel.getList())
                                        );
                                        transactionsTable.refresh();
                                    }
                            )
                    );
        });
    }

    private void clearTable() {
        Platform.runLater(() -> {
            transactionsTable.getItems().clear();
            transactionsTable.refresh();
        });
    }

    private void initTransactionHistoryButton() {
        historyTransactions.setOnAction(event -> {
            clearTable();
            result.setText("");

            // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
            // here a re some reference:
            //  - http://reactivex.io
            //  - https://github.com/ReactiveX/RxJava
            persistenceProtoCore
                    .getTransactionHistory(20, null, null)
                    .subscribeOn(Schedulers.newThread())
                    .take(1)
                    .subscribe(historyTransactionsModel ->
                            Platform.runLater(() -> {
                                        acceptButton.setDisable(true);
                                        refuseButton.setDisable(true);
                                        refundButton.setDisable(false);

                                        transactionsTable.setItems(
                                                FXCollections.observableList(historyTransactionsModel.getList())
                                        );
                                        transactionsTable.refresh();
                                    }
                            )
                    );
        });
    }

    private void initRefundButton() {
        refundButton.setDisable(true);
        refundButton.setOnAction(event -> {
            TransactionProposal selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {

                // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
                // here a re some reference:
                //  - http://reactivex.io
                //  - https://github.com/ReactiveX/RxJava
                persistenceProtoCore
                        .refundTransaction(selectedItem.getTransactionId())
                        .take(1)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(
                                transactionProposal -> Platform.runLater(() -> {
                                    transactionsTable.getItems().clear();
                                    transactionsTable.refresh();

                                    disableAllButtons();
                                    result.setText("OK");
                                }),
                                throwable -> Platform.runLater(() -> result.setText("KO check logs"))
                        );
            }
        });
    }

}
