package com.example.dotamarketplace;

import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderLogger;


public class RudderstackClient {
    private static RudderClient rudderClient;

    public static RudderClient getRudderClient(){
        if(rudderClient==null){
            rudderClient = RudderClient.getInstance(
                    RudderClient.getApplication(),
                    "39dce006-33e4-4678-8054-0765319c0141",
                    new RudderConfig.Builder()
                            .withDataPlaneUrl("https://backend-cdxp360.digipop.ai/61ff8018-21ba-41d3-9f60-bc3a9d5f79aa/df526042-d013-42e1-b099-c39e19db0a3d")
                            .withControlPlaneUrl("https://backend-cdxp360.digipop.ai/61ff8018-21ba-41d3-9f60-bc3a9d5f79aa/df526042-d013-42e1-b099-c39e19db0a3d")
                            .withTrackLifecycleEvents(true)
                            .withRecordScreenViews(true)
                            .withLogLevel(RudderLogger.RudderLogLevel.VERBOSE) // for logging, disable in production
                            .build()
            );
        }
        return rudderClient;
    }

}
