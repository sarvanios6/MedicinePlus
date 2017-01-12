package com.sarvan.medicineplus;

import android.app.Application;

import io.realm.Realm;

/**
 * MedicinePlus Application
 */

public class MedicinePlus extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /** Initialize Realm **/
//        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
//        Realm.setDefaultConfiguration(config);
        Realm.init(this);
    }
}
