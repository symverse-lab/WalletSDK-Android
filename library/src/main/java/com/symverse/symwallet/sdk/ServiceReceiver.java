package com.symverse.symwallet.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import java.util.Locale;

public abstract class ServiceReceiver {
    private Context context;
    private String postFixId;
    private String action;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            SymSDK.WalletAPI.RequestID requestID = (SymSDK.WalletAPI.RequestID) intent.getSerializableExtra("requestId");
            SymSDK.WalletAPI.SendData sendData = (SymSDK.WalletAPI.SendData) intent.getSerializableExtra("sendData");
            SymSDK.WalletAPI.ReceiveData receiveData = (SymSDK.WalletAPI.ReceiveData) intent.getSerializableExtra("receiveData");

            ServiceReceiver.this.onReceive(requestID, sendData.getApi(), sendData, receiveData);
        }
    };

    public ServiceReceiver(@NonNull Context context, @NonNull String postFixId) {
        this.context = context;
        this.postFixId = postFixId;
        this.action = String.format(Locale.getDefault(), "%s_%s_%d", context.getPackageName(), postFixId, System.currentTimeMillis());
    }

    public final String getAction() {
        return action;
    }

    public void register() {
        context.registerReceiver(broadcastReceiver, new IntentFilter(action));
    }

    public void unregister() {
        context.unregisterReceiver(broadcastReceiver);
    }

    public abstract void onReceive(SymSDK.WalletAPI.RequestID requestID, SymSDK.WalletAPI.API api, SymSDK.WalletAPI.SendData sendData, SymSDK.WalletAPI.ReceiveData receiveData);
}
