package com.symverse.symwallet.sdk.sample;

import android.content.ClipData;
import android.content.ClipboardManager;
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

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {
    private AppCompatEditText serviceUser;
    private AppCompatButton requestButton;

    private ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        serviceUser = findViewById(R.id.etServiceUser);
        requestButton = findViewById(R.id.btnRequest);

        requestButton.setOnClickListener(this::signIn);
        serviceReceiver = new ServiceReceiver(this, getClass().getSimpleName()) {

            @Override
            public void onReceive(SymSDK.WalletAPI.RequestID requestID, SymSDK.WalletAPI.API api, SymSDK.WalletAPI.SendData sendData, SymSDK.WalletAPI.ReceiveData receiveData) {
                switch (api) {
                    case SIGNIN: {
                        if (receiveData.getStatus().equals("success")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] result : %s", sendData.getApi(), receiveData.getResult()))
                                    .append("\n\n")
                                    .append(String.format("Citizen : %s", receiveData.getCitizen()));

                            new AlertDialog.Builder(SignInActivity.this)
                                    .setTitle(android.R.string.dialog_alert_title)
                                    .setMessage(stringBuilder.toString())
                                    .create().show();

                            try {
                                JSONObject jsonObject = new JSONObject(receiveData.getResult());
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                clipboardManager.setPrimaryClip(ClipData.newPlainText("", jsonObject.optString("signature")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] error : %s", sendData.getApi(), receiveData.getError()));

                            new AlertDialog.Builder(SignInActivity.this)
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

    private void signIn(View view) {
        try {
            SymWalletAPI.signIn(this, serviceReceiver, serviceUser.getText().toString());
        } catch (MissingArgumentException e) {
            e.printStackTrace();
        }
    }
}
