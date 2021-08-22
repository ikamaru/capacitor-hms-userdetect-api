package com.ikamaru.capacitor.hms.userdetect.api;

import android.app.Activity;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.safetydetect.UserDetectResponse;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectClient;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

public class UserDetect {
    private Activity activity;
    UserDetect(Activity activity){
        this.activity=activity;
    }
    private static String TAG="UserDetectImpl";

    private void initUserDetect(PluginCall call) {
        SafetyDetectClient client = SafetyDetect.getClient(activity);
        client.initUserDetect().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                // Indicates that communication with the service was successful.
                Log.i(TAG, "initUserDetect:onSuccess");
                userDetection(call);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // An error occurred during communication with the service.
                Log.i(TAG, "initUserDetect:onFailure");
                call.reject("userDetect Failed : initUserDetect Failed",e);
            }
        });
    }
    private void userDetection(PluginCall call){
        SafetyDetectClient client = SafetyDetect.getClient(activity);
        String appId = call.getString("appId");
        client.userDetection(appId)
                .addOnSuccessListener(new OnSuccessListener<UserDetectResponse>() {
                    @Override
                    public void onSuccess(UserDetectResponse userDetectResponse) {
                        // Indicates that communication with the service was successful.
                        String responseToken = userDetectResponse.getResponseToken();
                        if (!responseToken.isEmpty()) {
                            Log.i(TAG, "onSuccess: "+responseToken);
                            // Send the response token to your app server, and call the cloud API of HMS Core on your server to obtain the fake user detection result.
                            JSObject res = new JSObject();
                            res.put("token", responseToken);
                            call.resolve(res);
                        }else{
                            Log.i(TAG, "onSuccess: empty..");
                            call.reject("userDetect Failed : userDetection empty token");
                        }
                        shutdownUserDetect();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // An error occurred during communication with the service.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            // An error with the HMS API contains some additional details.
                            // You can use the apiException.getStatusCode() method to get the status code.
                            ApiException apiException = (ApiException) e;
                            errorMsg = SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": "
                                    + apiException.getMessage();
                        } else {
                            // Unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        call.reject("userDetect Failed : userDetection "+errorMsg);
                        Log.i(TAG, "User detection fail. Error info: " + errorMsg);
                        shutdownUserDetect();
                    }
                });
    }
    private void shutdownUserDetect() {
        // Replace with your activity or context as a parameter.
        SafetyDetectClient client = SafetyDetect.getClient(this.activity);
        client.shutdownUserDetect().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                // Indicates that communication with the service was successful.
                Log.i(TAG, "onSuccess: shutdownUserDetect");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // An error occurred during communication with the service.
                Log.i(TAG, "onFailure: shutdownUserDetect "+e.getMessage());
            }
        });
    }

    public void detectUser(PluginCall call){
        if(!call.getData().has("appId") || call.getString("appId")==null || call.getString("appId").isEmpty()){
            call.reject("userDetect Failed : Empty appId");
        }else {
            if(activity==null){
                call.reject("userDetect Failed : Empty activity");
            }else{
                initUserDetect(call);
            }
        }
    }
}
