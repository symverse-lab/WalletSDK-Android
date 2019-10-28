package com.symverse.symwallet.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.symverse.symwallet.sdk.exceptions.MissingArgumentException;

import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public final class SymWalletAPI {

    private SymWalletAPI() {

    }

    public static void send(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver,
                               @NonNull String walletId, @NonNull String toAddress,
                               @Nullable BigDecimal value) throws MissingArgumentException {
        if (!checkInstall(context)) {
            return;
        }

        if (value == null || value.compareTo(BigDecimal.ZERO) == -1) {
            value = BigDecimal.ZERO;
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.SEND);
        sendData.setCallback(serviceReceiver.getAction());
        sendData.setWalletId(walletId);
        sendData.setToAddress(toAddress);
        sendData.setValue(value.toPlainString());

        sendBroadcast(context, sendData);
    }

    public static void paid(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver,
                               @Nullable String storeId, @Nullable String storeName, @Nullable String itemId, @Nullable String itemName,
                               @NonNull String walletId, @NonNull String toAddress,
                               @Nullable BigDecimal value, @Nullable BigInteger quantities, @Nullable BigDecimal discount) throws MissingArgumentException {
        if (!checkInstall(context)) {
            return;
        }

        if (value == null || value.compareTo(BigDecimal.ZERO) == -1) {
            value = BigDecimal.ZERO;
        }

        if (quantities == null || quantities.compareTo(BigInteger.ONE) == -1) {
            quantities = BigInteger.ONE;
        }

        if (discount == null || discount.compareTo(BigDecimal.ZERO) == -1) {
            discount = BigDecimal.ZERO;
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.PAID);
        sendData.setCallback(serviceReceiver.getAction());
        sendData.setStoreId(storeId);
        sendData.setStoreName(storeName);
        sendData.setItemId(itemId);
        sendData.setItemName(itemName);
        sendData.setWalletId(walletId);
        sendData.setToAddress(toAddress);
        sendData.setValue(value.toPlainString());
        sendData.setQuantities(quantities);
        sendData.setDiscount(discount);

        sendBroadcast(context, sendData);
    }

    public static String signIn(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver, @NonNull String user) throws MissingArgumentException {
        if (!checkInstall(context)) {
            return null;
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.SIGNIN);
        sendData.setCallback(serviceReceiver.getAction());
        sendData.setUser(user);
        sendData.setValue(sendData.makeHashMessage());

        sendBroadcast(context, sendData);

        return sendData.getValue();
    }

    public static String signData(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver, @NonNull String data) throws MissingArgumentException {
        if (!checkInstall(context)) {
            return null;
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.SIGN_DATA);
        sendData.setCallback(serviceReceiver.getAction());
        sendData.setValue(sendData.makeHashMessage(data));

        sendBroadcast(context, sendData);

        return sendData.getValue();
    }

    public static String verifyingSign(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver, @NonNull String signMessage) throws MissingArgumentException, ArrayIndexOutOfBoundsException {
        if (!checkInstall(context)) {
            return null;
        }

        byte[] signMessageBytes = Hex.decode(signMessage.replaceFirst("^0x", ""));
        byte[] symId = Arrays.copyOfRange(signMessageBytes, 0, 10);
        byte[] messageHash = Arrays.copyOfRange(signMessageBytes, 11, 11 + signMessageBytes[10]);
        if (messageHash.length != 32) {
            throw new MissingArgumentException("Malformed signMessage.");
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.VERIFYING_SIGN);
        sendData.setCallback(serviceReceiver.getAction());
        sendData.setValue(signMessage);

        sendBroadcast(context, sendData);

        return "0x" + Hex.toHexString(messageHash);
    }

    public static void listKeyStore(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver) throws MissingArgumentException {
        if (!checkInstall(context)) {
            return;
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.LIST_KEYSTORE);
        sendData.setCallback(serviceReceiver.getAction());

        sendBroadcast(context, sendData);
    }

    public static void exportKey(@NonNull Context context, @NonNull ServiceReceiver serviceReceiver, @NonNull SymSDK.WalletAPI.ExportMode exportMode) throws MissingArgumentException {
        if (!checkInstall(context)) {
            return;
        }

        String appId = null;
        try {
            appId = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.metaData.getString("SymAppID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }
        if (appId == null) {
            throw new MissingArgumentException("SymAppID not found in metadata.");
        }

        SymSDK.WalletAPI.SendData sendData = new SymSDK.WalletAPI.SendData();
        sendData.setAppId(appId);
        sendData.setPackage(context.getPackageName());
        sendData.setApi(SymSDK.WalletAPI.API.EXPORT_KEY);
        sendData.setCallback(serviceReceiver.getAction());
        sendData.setExportMode(exportMode);

        sendBroadcast(context, sendData);
    }

    private static void sendBroadcast(Context context, SymSDK.WalletAPI.SendData sendData) {
        Gson gson = new Gson();
        StringBuilder dataBuilder = new StringBuilder("symverse://sdk?");
        dataBuilder.append("requestId=").append(Uri.encode(gson.toJson(new SymSDK.WalletAPI.ReceiveData())));
        dataBuilder.append("&sendData=").append(Uri.encode(gson.toJson(sendData)));

        Intent intent = new Intent("com.symverse.symwallet.sdk");
        intent.setClassName("com.symverse.wallet", "com.symverse.symwallet.sdk.SymWalletSDKReceiver");
        intent.setData(Uri.parse(dataBuilder.toString()));
        context.sendBroadcast(intent);
    }

    private static boolean checkInstall(@NonNull Context context) {
        try {
            boolean result = false;
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.symverse.wallet", PackageManager.GET_RECEIVERS);
            for (ActivityInfo activityInfo : packageInfo.receivers) {
                if (activityInfo.name.equals("com.symverse.symwallet.sdk.SymWalletSDKReceiver")) {
                    result = true;
                    break;
                }
            }

            if (result) {
                return true;
            } else {
                throw new PackageManager.NameNotFoundException("not found sdk receiver.");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.symverse.wallet"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
    }
}
