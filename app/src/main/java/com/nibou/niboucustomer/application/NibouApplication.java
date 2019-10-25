package com.nibou.niboucustomer.application;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NibouApplication extends MultiDexApplication implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        setupLibrary();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
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
            FirebaseApp.initializeApp(NibouApplication.this);

            //local database for chat
            Realm.init(NibouApplication.this);
            RealmConfiguration config = new RealmConfiguration.Builder().name("scm.realm").schemaVersion(1).deleteRealmIfMigrationNeeded().build();
            Realm.setDefaultConfiguration(config);
            Stetho.initialize(Stetho.newInitializerBuilder(NibouApplication.this).enableDumpapp(Stetho.defaultDumperPluginsProvider(NibouApplication.this)).enableWebKitInspector(RealmInspectorModulesProvider.builder(NibouApplication.this).build()).build());
        }).start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
    }
}
