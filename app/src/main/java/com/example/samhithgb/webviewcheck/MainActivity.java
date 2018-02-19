package com.example.samhithgb.webviewcheck;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    public int PERMISSION_REQUEST_CODE = 1;
    private static DialogInterface.OnClickListener sOnClickListenerForPermissionRationale = null;
    public PermissionInterface mCallback;
    public String mOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.loadUrl("https://www.google.co.in/maps/place/conra/@12.9750473,77.6170616,16z/data=!4m5!3m4!1s0x3bae169b41c32eeb:0xd7be5acf89a61a1d!8m2!3d12.9754049!4d77.6204947");
    }

    public boolean askForPermission(String permission, Activity activity, PermissionInterface callback) {
        mCallback = callback;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if ((activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)) {
            int requestCode = PERMISSION_REQUEST_CODE;
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                String permissionName = "Location Permission";
                String permissionAccessDescription = "This website is asking for location permission";
                DialogInterface.OnClickListener positiveButtonListener = getOnClickListenerForPermissionRationale(activity, permission, requestCode);
                showAlertDialog(activity,permissionName ,permissionAccessDescription ,false, activity.getString(android.R.string.ok), positiveButtonListener);
            } else {
                activity.requestPermissions(new String[]{permission}, requestCode);
            }
            return true;
        }
        return false;
    }

    private DialogInterface.OnClickListener getOnClickListenerForPermissionRationale(final Activity activity, final String permission, final int finalRequestCode) {
        if (sOnClickListenerForPermissionRationale == null) {
            sOnClickListenerForPermissionRationale = new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.requestPermissions(new String[]{permission}, finalRequestCode);
                }
            };
        }
        return sOnClickListenerForPermissionRationale;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, boolean isCancellable, String positiveButtonString, DialogInterface.OnClickListener positiveButtonListener) {
        return (new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isCancellable)
                .setPositiveButton(positiveButtonString, positiveButtonListener)).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCallback.onPermissionGranted(true);
        } else {
            mCallback.onPermissionGranted(false);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
