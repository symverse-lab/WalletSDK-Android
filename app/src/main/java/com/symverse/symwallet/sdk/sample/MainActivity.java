package com.symverse.symwallet.sdk.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton sendButton;
    private AppCompatButton paidButton;
    private AppCompatButton signInButton;
    private AppCompatButton signDataButton;
    private AppCompatButton verifyingSignButton;
    private AppCompatButton listKeyStoreButton;
    private AppCompatButton exportKeyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.btnSend);
        paidButton = findViewById(R.id.btnPaid);
        signInButton = findViewById(R.id.btnSignIn);
        signDataButton = findViewById(R.id.btnSignData);
        verifyingSignButton = findViewById(R.id.btnVerifyingSign);
        listKeyStoreButton = findViewById(R.id.btnListKeyStore);
        exportKeyButton = findViewById(R.id.btnExportKey);

        sendButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SendActivity.class);
            startActivity(intent);
        });
        paidButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PaidActivity.class);
            startActivity(intent);
        });
        signInButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        });
        signDataButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignDataActivity.class);
            startActivity(intent);
        });
        verifyingSignButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, VerifyingSignActivity.class);
            startActivity(intent);
        });
        listKeyStoreButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListKeyStoreActivity.class);
            startActivity(intent);
        });
        exportKeyButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExportKeyActivity.class);
            startActivity(intent);
        });
    }
}
