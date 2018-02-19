package com.example.samhithgb.webviewcheck;

import android.Manifest;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;

public class MyWebChromeClient extends WebChromeClient {

    MainActivity mainActivity;
    boolean isPermissionAsked = true;

    public MyWebChromeClient(MainActivity activity){
         mainActivity = activity;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
        isPermissionAsked= mainActivity.askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, mainActivity, new PermissionInterface() {
           @Override
           public void onPermissionGranted(boolean isGranted) {
               if(isGranted){
                   callback.invoke(origin,true,false);
               } else {
                   callback.invoke(origin,false,false);
               }
           }
       });
        //give website permission if app permission is already available or Android version is below M
        if(!isPermissionAsked){
            callback.invoke(origin,true,false);
        }
    }
}
