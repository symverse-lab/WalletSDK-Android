package com.symverse.symwallet.sdk.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import com.symverse.symwallet.sdk.ServiceReceiver;
import com.symverse.symwallet.sdk.SymSDK;
import com.symverse.symwallet.sdk.SymWalletAPI;
import com.symverse.symwallet.sdk.exceptions.MissingArgumentException;

public class ExportKeyActivity extends AppCompatActivity {
    private SwitchCompat exportModeSwitch;
    private AppCompatButton requestButton;

    private ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_key);

        exportModeSwitch = findViewById(R.id.switchExportMode);
        requestButton = findViewById(R.id.btnRequest);

        requestButton.setOnClickListener(this::exportKey);
        serviceReceiver = new ServiceReceiver(this, getClass().getSimpleName()) {

            @Override
            public void onReceive(SymSDK.WalletAPI.RequestID requestID, SymSDK.WalletAPI.API api, SymSDK.WalletAPI.SendData sendData, SymSDK.WalletAPI.ReceiveData receiveData) {
                switch (api) {
                    case EXPORT_KEY: {
                        if (receiveData.getStatus().equals("success")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] result : %s", sendData.getExportMode(), receiveData.getResult()));

                            new AlertDialog.Builder(ExportKeyActivity.this)
                                    .setTitle(android.R.string.dialog_alert_title)
                                    .setMessage(stringBuilder.toString())
                                    .create().show();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.format("ID : %s", requestID.getId())).append("\n").append(requestID.getTimestamp())
                                    .append("\n\n")
                                    .append(String.format("[%s] error : %s", sendData.getExportMode(), receiveData.getError()));

                            new AlertDialog.Builder(ExportKeyActivity.this)
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

    private void exportKey(View view) {
        try {
            SymWalletAPI.exportKey(this, serviceReceiver, exportModeSwitch.isChecked() ? SymSDK.WalletAPI.ExportMode.PATH : SymSDK.WalletAPI.ExportMode.CONTENT);
        } catch (MissingArgumentException e) {
            e.printStackTrace();
        }
    }
}
