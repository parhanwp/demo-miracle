package com.example.dotamarketplace;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderLogger;

public class MyApplication extends Application {
    private static MyApplication myApp;
    static RudderClient rudderClient = null;
    private static String fcm_token;

    public static MyApplication getInstance(){
        return myApp;
    }

    public RudderClient getRudderClient(){
        return rudderClient;
    }

    public String getFCMtoken(){
        return fcm_token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        rudderClient = RudderClient.getInstance(
                this,
                "39dce006-33e4-4678-8054-0765319c0141",
                new RudderConfig.Builder()
                        .withDataPlaneUrl("https://backend-cdxp360.digipop.ai/61ff8018-21ba-41d3-9f60-bc3a9d5f79aa/df526042-d013-42e1-b099-c39e19db0a3d")
                        .withControlPlaneUrl("https://backend-cdxp360.digipop.ai/61ff8018-21ba-41d3-9f60-bc3a9d5f79aa/df526042-d013-42e1-b099-c39e19db0a3d")
                        .withTrackLifecycleEvents(true)
                        .withRecordScreenViews(true)
                        .withLogLevel(RudderLogger.RudderLogLevel.VERBOSE) // for logging, disable in production
                        .build()
        );

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("log_fetch_fcm", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        fcm_token = token;
                        Log.d("log_fetch_fcm", token);
                    }
                });
    }
}
