package com.symverse.symwallet.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

public class SymWalletResultReceiver extends BroadcastReceiver {
    private Gson gson = new Gson();

    @Override
    public void onReceive(Context context, Intent intent) {
        String requestIdString = intent.getStringExtra("requestId");
        String sendDataString = intent.getStringExtra("sendData");
        String receiveDataString = intent.getStringExtra("receiveData");

        SymSDK.WalletAPI.RequestID requestID = gson.fromJson(requestIdString, SymSDK.WalletAPI.RequestID.class);
        SymSDK.WalletAPI.SendData sendData = gson.fromJson(sendDataString, SymSDK.WalletAPI.SendData.class);
        SymSDK.WalletAPI.ReceiveData receiveData = gson.fromJson(receiveDataString, SymSDK.WalletAPI.ReceiveData.class);

        if (sendData != null && sendData.getCallback() != null) {
            Intent tossIntent = new Intent(sendData.getCallback());
            tossIntent.setData(intent.getData());
            tossIntent.putExtra("requestId", requestID);
            tossIntent.putExtra("sendData", sendData);
            tossIntent.putExtra("receiveData", receiveData);
            context.sendBroadcast(tossIntent);
        }
    }
}
