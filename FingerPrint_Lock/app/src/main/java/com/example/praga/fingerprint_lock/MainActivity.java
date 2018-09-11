package com.example.praga.fingerprint_lock;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import static android.content.Context.FINGERPRINT_SERVICE;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    private FingerprintManager mFingerprintManager;
    private final String KEY_ALIAS = "FINGERPRINT_KEY_PAIR_ALIAS";
    private final String KEY_STORE = "AndroidKeyStore";

    private KeyStore mKeyStore;
    private KeyPairGenerator mKeyPairGenerator;
    private Cipher mCipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSensor();
    }

    private void initSensor() {
        mFingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        if (isFingerPrintRegistered()) {
            FingerprintManager.CryptoObject cryptedFingerprint = getCryptoObject();
            if (cryptedFingerprint != null) {
                mFingerprintManager.authenticate(cryptedFingerprint, new CancellationSignal(), 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        Toast.makeText(getBaseContext(), errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        navigateToApp();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(getBaseContext(), "FingerPrint not Recognised", Toast.LENGTH_SHORT).show();
                    }
                }, null);
                return;
            }
        }
        navigateToApp();
    }

    private boolean isFingerPrintRegistered(){
        if (mFingerprintManager.isHardwareDetected()) {
            KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            return keyguardManager.isKeyguardSecure() && mFingerprintManager.hasEnrolledFingerprints();
        } else return false;
    }

    private FingerprintManager.CryptoObject getCryptoObject(){
        try{
            mKeyStore = KeyStore.getInstance(KEY_STORE);
            mKeyStore.load(null);
            mCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            if(!mKeyStore.containsAlias(KEY_ALIAS)){
                mKeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEY_STORE);
                mKeyPairGenerator.initialize(new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP).setUserAuthenticationRequired(true).build());
                mKeyPairGenerator.generateKeyPair();
            }
            PrivateKey privateKey = (PrivateKey) mKeyStore.getKey(KEY_ALIAS, null);
            mCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new FingerprintManager.CryptoObject(mCipher);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void navigateToApp(){
        startActivity(new Intent(this, Welcome.class));
    }
}
