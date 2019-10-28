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
import java.math.BigInteger;
import java.util.regex.Pattern;

public class PaidActivity extends AppCompatActivity {
    private AppCompatEditText storeIdEdit;
    private AppCompatEditText storeNameEdit;
    private AppCompatEditText itemIdEdit;
    private AppCompatEditText itemIdName;
    private AppCompatEditText walletIdEdit;
    private AppCompatEditText toAddressEdit;
    private AppCompatEditText valueEdit;
    private AppCompatEditText quantitiesEdit;
    private AppCompatEditText discountEdit;
    private AppCompatButton requestButton;

    private ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);

        storeIdEdit = findViewById(R.id.etStoreId);
        storeNameEdit = findViewById(R.id.etStoreName);
        itemIdEdit = findViewById(R.id.etItemId);
        itemIdName = findViewById(R.id.etItemName);
        walletIdEdit = findViewById(R.id.etWalletId);
        toAddressEdit = findViewById(R.id.etToAddress);
        valueEdit = findViewById(R.id.etValue);
        quantitiesEdit = findViewById(R.id.etQuantities);
        discountEdit = findViewById(R.id.etDiscount);
        requestButton = findViewById(R.id.btnRequest);

        requestButton.setOnClickListener(this::paid);

        serviceReceiver = new ServiceReceiver(this, getClass().getSimpleName()) {

            @Override
            public void onReceive(SymSDK.WalletAPI.RequestID requestID, SymSDK.WalletAPI.API api, SymSDK.WalletAPI.SendData sendData, SymSDK.WalletAPI.ReceiveData receiveData) {
                switch (api) {
                    case PAID: {
                        if (receiveData.getStatus().equals("success")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] result : %s", sendData.getApi(), receiveData.getResult()));

                            new AlertDialog.Builder(PaidActivity.this)
                                    .setTitle(android.R.string.dialog_alert_title)
                                    .setMessage(stringBuilder.toString())
                                    .create().show();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] error : %s", sendData.getApi(), receiveData.getError()));

                            new AlertDialog.Builder(PaidActivity.this)
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

    private void paid(View view) {
        try {
            BigDecimal value = textToBigInt(valueEdit.getText(), 0);
            BigInteger quantities = textToBigInt(quantitiesEdit.getText(), 1).toBigInteger();
            BigDecimal discount = textToBigInt(discountEdit.getText(), 0);

            SymWalletAPI.paid(this, serviceReceiver,
                    storeIdEdit.getText().toString(), storeNameEdit.getText().toString(), itemIdEdit.getText().toString(), itemIdName.getText().toString(),
                    walletIdEdit.getText().toString(), toAddressEdit.getText().toString(),
                    value, quantities, discount);
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
