package com.satispay.instore.client.controller;

import com.google.gson.Gson;
import com.satispay.instore.client.data.DHFlowClientImpl;
import com.satispay.instore.client.data.PersistenceProtoCoreClientImpl;
import com.satispay.protocore.active.PersistenceProtoCore;
import com.satispay.protocore.active.ProtoCoreHttpClientProvider;
import com.satispay.protocore.crypto.Base64Utils;
import com.satispay.protocore.dh.DHFlow;
import com.satispay.protocore.dh.DHValues;
import com.satispay.protocore.errors.ProtoCoreError;
import com.satispay.protocore.errors.ProtoCoreErrorType;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.utility.NetworkUtilities;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;

import static com.satispay.instore.client.SatispayInStoreApplication.SDK_DEVICE_INFO;

public class FirstPageController implements Initializable {
    private final String FILE_NAME = "./dh_keys.json";
    public TextField insertTokenField;
    public Button confirmButton;
    public Label sharedSecret;
    public Label nonce;
    public Label tokenResult;
    public Button testSignatureButton;
    public TextArea signatureResponse;
    public Button saveDHKeysButton;
    public Button loadDHKeysButton;
    public Label userKeyId;
    public Label kMaster;
    public Label sequence;
    public Label storingResult;

    private Gson gson = NetworkUtilities.getGson();

    private DHFlow dhFlow;
    private PersistenceProtoCore persistenceProtoCore;

    public void initialize(URL location, ResourceBundle resources) {
        // ==> here the two most important objects are created and retrieved.
        dhFlow = DHFlowClientImpl.getInstance();
        persistenceProtoCore = PersistenceProtoCoreClientImpl.getInstance();

        signatureResponse.setWrapText(true);

        initTokenTextField();
        initConfirmButton();
        initTestSignatureButton();
        initSaveDHKeysButton();
        initLoadDHKeysButton();
    }

    private void initLoadDHKeysButton() {
        loadDHKeysButton.setDisable(false);
        loadDHKeysButton.setOnAction(event -> {
            try {
                DHValues dhValues = gson.fromJson(new String(Files.readAllBytes(Paths.get(FILE_NAME))), DHValues.class);
                loadDHValues(dhValues);
                storingResult.setText("DH VALUES LOADED FROM FILE " + FILE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                storingResult.setText("ERROR LOADING DH VALUES FROM FILE " + FILE_NAME);
            }
        });
    }

    private void initTestSignatureButton() {
        testSignatureButton.setOnAction(event -> {
            signatureResponse.clear();

            // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
            // here a re some reference:
            //  - http://reactivex.io
            //  - https://github.com/ReactiveX/RxJava
            Observable
                    .create(subscriber -> {
                        Request request = new Request.Builder().url("https://staging.authservices.satispay.com/wally-services/protocol/tests/signature").build();
                        try {
                            Response response = ProtoCoreHttpClientProvider.getInstance().getProtocoreClientNoSignatureVerify(dhFlow.getSatispayContext(), MemoryPersistenceManager.getInstance(), SDK_DEVICE_INFO).newCall(request).execute();
                            subscriber.onNext(response.body().string());
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .take(1)
                    .subscribe(string -> signatureResponse.setText(string + ""), throwable -> signatureResponse.setText(Arrays.toString(throwable.getStackTrace())));
        });
    }

    private void initConfirmButton() {
        confirmButton.setOnMouseClicked(event -> {
            String token = insertTokenField.getText();
            if (token != null && !token.isEmpty()) {

                // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
                // here a re some reference:
                //  - http://reactivex.io
                //  - https://github.com/ReactiveX/RxJava
                dhFlow
                        .performExchange()
                        .take(1)
                        .subscribeOn(Schedulers.newThread())
                        .switchMap(responseBean -> dhFlow.performChallenge())
                        .switchMap(dhEncryptedResponseBean -> dhFlow.performTokenVerification(token))
                        .subscribe(
                                dhEncryptedResponseBean -> Platform.runLater(() -> {
                                    confirmButton.setVisible(true);
                                    insertTokenField.setEditable(true);

                                    sharedSecret.setText(Base64Utils.encode(DHValues.getInstance().getkAuth()));
                                    nonce.setText(DHValues.getInstance().getNonce());

                                    Platform.runLater(() -> tokenResult.setText("VERIFIED"));
                                    saveDHKeysButton.setDisable(false);
                                    loadDHKeysButton.setDisable(true);

                                    fillGraphicalLabelsWithValues();
                                }),
                                throwable -> {
                                    if (throwable instanceof ProtoCoreError) {
                                        ProtoCoreError protoCoreError = (ProtoCoreError) throwable;
                                        if (protoCoreError.getProtoCoreErrorType().equals(ProtoCoreErrorType.INVALID_ACTIVATION_CODE)) {
                                            Platform.runLater(() -> tokenResult.setText("FAILED"));
                                        }
                                    }
                                }
                        );
            }
        });
    }

    private void initSaveDHKeysButton() {
        saveDHKeysButton.setDisable(true);
        saveDHKeysButton.setOnAction(event -> {
            File file = new File(FILE_NAME);
            String dhValuesJson = gson.toJson(DHValues.getInstance());
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(dhValuesJson);
                fileWriter.flush();
                fileWriter.close();
                storingResult.setText("DH VALUES SAVED IN FILE " + FILE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                storingResult.setText("ERROR SAVING DH VALUES IN FILE " + FILE_NAME);
            }
        });
    }

    private void initTokenTextField() {
        insertTokenField.textProperty().addListener((observable, oldValue, newValue) -> {
            newValue = newValue.replace(" ", "");
            if (newValue.length() > 6) {
                newValue = newValue.substring(0, 6);
            }
            insertTokenField.setText(newValue.toUpperCase());
        });
    }

    private void loadDHValues(DHValues dhValues) {
        DHValues.getInstance().setkMaster(dhValues.getkMaster());
        DHValues.getInstance().setSequence(dhValues.getSequence());
        DHValues.getInstance().setUserKeyId(dhValues.getUserKeyId());
        MemoryPersistenceManager.getInstance().persistSecurely(SecurePersistenceManager.KMASTER_KEY, Base64Utils.encode(DHValues.getInstance().getkMaster()));
        MemoryPersistenceManager.getInstance().persistSecurely(SecurePersistenceManager.SEQUENCE_KEY, "" + DHValues.getInstance().getSequence());
        MemoryPersistenceManager.getInstance().persistSecurely(SecurePersistenceManager.USER_KEY_ID_KEY, "" + DHValues.getInstance().getUserKeyId());

        loadDHKeysButton.setDisable(true);
        saveDHKeysButton.setDisable(false);
        confirmButton.setDisable(true);
        insertTokenField.setEditable(false);

        fillGraphicalLabelsWithValues();
    }

    private void fillGraphicalLabelsWithValues() {
        userKeyId.setText(MemoryPersistenceManager.getInstance().getPersistedData(SecurePersistenceManager.USER_KEY_ID_KEY));
        kMaster.setText(MemoryPersistenceManager.getInstance().getPersistedData(SecurePersistenceManager.KMASTER_KEY));
        sequence.setText(MemoryPersistenceManager.getInstance().getPersistedData(SecurePersistenceManager.SEQUENCE_KEY));
    }

}
