package com.symverse.symwallet.sdk;

import com.google.gson.annotations.SerializedName;

import org.spongycastle.jcajce.provider.digest.SHA3;
import org.spongycastle.util.encoders.Hex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SymSDK {

    public static class WalletAPI {
        public static final long API_VERSION = 1;
        public static final long API_MIN_VERSION = 1;

        //enums

        public static enum API {
            SEND,
            PAID,
            SIGNIN,
            SIGN_DATA,
            VERIFYING_SIGN,
            LIST_KEYSTORE,
            EXPORT_KEY
        }

        public static enum ExportMode {
            PATH,
            CONTENT
        }
        //

        // datas

        public static class RequestID implements Serializable {
            @SerializedName("id")
            private final String id;

            @SerializedName("timestamp")
            private final long timestamp;

            public RequestID() {
                id = UUID.randomUUID().toString();
                timestamp = System.currentTimeMillis();
            }

            public String getId() {
                return id;
            }

            public long getTimestamp() {
                return timestamp;
            }
        }

        public static class SendData implements Serializable {
            @SerializedName("package")
            private String aPackage;

            @SerializedName("appId")
            private String appId;

            @SerializedName("callback")
            private String callback;

            @SerializedName("api")
            private String api;

            @SerializedName("version")
            private final long version = API_VERSION;

            @SerializedName("storeId")
            private String storeId;

            @SerializedName("storeName")
            private String storeName;

            @SerializedName("itemId")
            private String itemId;

            @SerializedName("itemName")
            private String itemName;

            @SerializedName("walletId")
            private String walletId;

            @SerializedName("toAddress")
            private String toAddress;

            @SerializedName("value")
            private String value;

            @SerializedName("quantities")
            private String quantities;

            @SerializedName("discount")
            private String discount;

            @SerializedName("user")
            private String user;

            @SerializedName("exportMode")
            private String exportMode;

            @SerializedName("timestamp")
            private final long timestamp;

            public SendData() {
                timestamp = System.currentTimeMillis();
            }

            public SendData(String aPackage, String appId, String callback, API api, String storeId, String storeName,
                            String itemId, String itemName, String walletId, String toAddress,
                            BigDecimal value, BigInteger quantities, BigDecimal discount,
                            String user, ExportMode exportMode) {
                this.aPackage = aPackage;
                this.appId = appId;
                this.callback = callback;
                this.api = api.name();
                this.storeId = storeId;
                this.storeName = storeName;
                this.itemId = itemId;
                this.itemName = itemName;
                this.walletId = walletId;
                this.toAddress = toAddress;
                this.value = value.toString();
                this.quantities = quantities.toString();
                this.discount = discount.toString();
                this.user = user;
                this.exportMode = exportMode.name();
                this.timestamp = System.currentTimeMillis();
            }

            public String getPackage() {
                return aPackage;
            }

            public void setPackage(String aPackage) {
                this.aPackage = aPackage;
            }

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getCallback() {
                return callback;
            }

            public void setCallback(String callback) {
                this.callback = callback;
            }

            public API getApi() {
                return API.valueOf(api);
            }

            public void setApi(API api) {
                this.api = api.name();
            }

            public long getVersion() {
                return version;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public String getWalletId() {
                return walletId;
            }

            public void setWalletId(String walletId) {
                this.walletId = walletId;
            }

            public String getToAddress() {
                return toAddress;
            }

            public void setToAddress(String toAddress) {
                this.toAddress = toAddress;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public BigInteger getQuantities() {
                return new BigInteger(quantities);
            }

            public void setQuantities(BigInteger quantities) {
                this.quantities = quantities.toString();
            }

            public BigDecimal getDiscount() {
                return new BigDecimal(discount);
            }

            public void setDiscount(BigDecimal discount) {
                this.discount = discount.toString();
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }

            public ExportMode getExportMode() {
                return ExportMode.valueOf(exportMode);
            }

            public void setExportMode(ExportMode exportMode) {
                this.exportMode = exportMode.name();
            }

            public long getTimestamp() {
                return timestamp;
            }

            //
            public final String makeHashMessage() {
                String messageHashSource = String.format("%s:%s:%s%d", appId, user, aPackage, System.currentTimeMillis());
                return makeHashMessage(messageHashSource);
            }

            public final String makeHashMessage(String data) {
                SHA3.Digest256 digest256 = new SHA3.Digest256();
                byte[] messageHash = digest256.digest(data.getBytes(StandardCharsets.UTF_8));

                return "0x" + Hex.toHexString(messageHash);
            }
        }

        public static class ReceiveData implements Serializable {
            @SerializedName("version")
            private final long version = API_VERSION;

            @SerializedName("minVer")
            private final long minVer = API_MIN_VERSION;

            @SerializedName("status")
            private String status;

            @SerializedName("result")
            private String result;

            @SerializedName("citizen")
            private String citizen;

            @SerializedName("error")
            private String error;

            @SerializedName("timestamp")
            private final long timestamp;

            public ReceiveData() {
                timestamp = System.currentTimeMillis();
            }

            public ReceiveData(String status, String result, String citizen, String error) {
                this.status = status;
                this.result = result;
                this.citizen = citizen;
                this.error = error;
                this.timestamp = System.currentTimeMillis();
            }

            public long getVersion() {
                return version;
            }

            public long getMinVer() {
                return minVer;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public String getCitizen() {
                return citizen;
            }

            public void setCitizen(String citizen) {
                this.citizen = citizen;
            }

            public String getError() {
                return error;
            }

            public void setError(String error) {
                this.error = error;
            }

            public long getTimestamp() {
                return timestamp;
            }
        }
    }
}
