package com.nibou.niboucustomer.application;

import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class SCMApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
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
}
