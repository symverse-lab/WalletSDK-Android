package com.symverse.symwallet.sdk.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.symverse.symwallet.sdk.ServiceReceiver;
import com.symverse.symwallet.sdk.SymSDK;
import com.symverse.symwallet.sdk.SymWalletAPI;
import com.symverse.symwallet.sdk.exceptions.MissingArgumentException;

public class VerifyingSignActivity extends AppCompatActivity {
    private AppCompatEditText verifyingSign;
    private AppCompatButton requestButton;

    private String messageHash;
    private ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying_sign);

        verifyingSign = findViewById(R.id.etVerifyingSign);
        requestButton = findViewById(R.id.btnRequest);

        requestButton.setOnClickListener(this::verifyingSign);
        serviceReceiver = new ServiceReceiver(this, getClass().getSimpleName()) {

            @Override
            public void onReceive(SymSDK.WalletAPI.RequestID requestID, SymSDK.WalletAPI.API api, SymSDK.WalletAPI.SendData sendData, SymSDK.WalletAPI.ReceiveData receiveData) {
                switch (api) {
                    case VERIFYING_SIGN: {
                        if (receiveData.getStatus().equals("success")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("messageHash : %s", messageHash))
                                    .append("\n\n")
                                    .append(String.format("result : %s", receiveData.getResult()));

                            new AlertDialog.Builder(VerifyingSignActivity.this)
                                    .setTitle(android.R.string.dialog_alert_title)
                                    .setMessage(stringBuilder.toString())
                                    .create().show();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("messageHash : %s", messageHash))
                                    .append("\n\n")
                                    .append(String.format("error : %s", receiveData.getError()));

                            new AlertDialog.Builder(VerifyingSignActivity.this)
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

    private void verifyingSign(View view) {
        try {
            messageHash = SymWalletAPI.verifyingSign(this, serviceReceiver, verifyingSign.getText().toString());
        } catch (MissingArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
