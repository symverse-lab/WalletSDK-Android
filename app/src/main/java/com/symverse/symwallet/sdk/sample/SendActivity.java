package com.symverse.symwallet.sdk.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.symverse.symwallet.sdk.ServiceReceiver;
import com.symverse.symwallet.sdk.SymSDK;
import com.symverse.symwallet.sdk.SymWalletAPI;
import com.symverse.symwallet.sdk.exceptions.MissingArgumentException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class SendActivity extends AppCompatActivity {
    private AppCompatEditText walletIdEdit;
    private AppCompatEditText toAddressEdit;
    private AppCompatEditText valueEdit;
    private AppCompatButton requestButton;

    private ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        walletIdEdit = findViewById(R.id.etWalletId);
        toAddressEdit = findViewById(R.id.etToAddress);
        valueEdit = findViewById(R.id.etValue);
        requestButton = findViewById(R.id.btnRequest);

        requestButton.setOnClickListener(this::send);

        serviceReceiver = new ServiceReceiver(this, getClass().getSimpleName()) {

            @Override
            public void onReceive(SymSDK.WalletAPI.RequestID requestID, SymSDK.WalletAPI.API api, SymSDK.WalletAPI.SendData sendData, SymSDK.WalletAPI.ReceiveData receiveData) {
                switch (api) {
                    case SEND: {
                        if (receiveData.getStatus().equals("success")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] result : %s", sendData.getApi(), receiveData.getResult()));

                            new AlertDialog.Builder(SendActivity.this)
                                    .setTitle(android.R.string.dialog_alert_title)
                                    .setMessage(stringBuilder.toString())
                                    .create().show();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] error : %s", sendData.getApi(), receiveData.getError()));

                            new AlertDialog.Builder(SendActivity.this)
                                    .setTitle(android.R.string.dialog_alert_title)
                                    .setMessage(stringBuilder.toString())
                                    .create().show();
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        };
        serviceReceiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceReceiver.unregister();
    }

    private void send(View view) {
        try {
            SymWalletAPI.send(this, serviceReceiver, walletIdEdit.getText().toString(), toAddressEdit.getText().toString(), textToBigInt(valueEdit.getText(), 0));
        } catch (MissingArgumentException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal textToBigInt(CharSequence text, int minValue) {
        if (TextUtils.isEmpty(text) || !Pattern.compile("[0-9.]+").matcher(text).matches()) {
            text = String.valueOf(minValue);
        }

        BigDecimal value = new BigDecimal(text.toString());
        BigDecimal min = BigDecimal.valueOf(minValue);
        if (value.compareTo(min) == -1) {
            value = min;
        }

        return value;
    }
}
