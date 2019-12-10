package com.nibou.niboucustomer.application;

import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nibou.niboucustomer.R;

public class SCMApplication extends MultiDexApplication {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setupLibrary();
    }

    private void setupLibrary() {
        new Thread(() -> {
            // for nogut crash
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //firebase setup
            FirebaseApp.initializeApp(SCMApplication.this);
        }).start();
    }

    synchronized public FirebaseAnalytics getFirebaseAnalytics() {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        return mFirebaseAnalytics;
    }
}
