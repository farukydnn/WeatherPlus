package com.farukydnn.weatherplus.core.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.interfaces.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Faruk Aydın on 16.10.2017.
 **/

public class PermissionActivity extends BaseActivity {

    public static class PermissionDialogMessage implements Parcelable {
        private final String title, message;

        public PermissionDialogMessage(String title, String message) {
            this.title = title;
            this.message = message;
        }

        String getTitle() {
            return title;
        }

        String getMessage() {
            return message;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.message);
        }

        protected PermissionDialogMessage(Parcel in) {
            this.title = in.readString();
            this.message = in.readString();
        }

        public static final Parcelable.Creator<PermissionDialogMessage> CREATOR
                = new Parcelable.Creator<PermissionDialogMessage>() {
            @Override
            public PermissionDialogMessage createFromParcel(Parcel source) {
                return new PermissionDialogMessage(source);
            }

            @Override
            public PermissionDialogMessage[] newArray(int size) {
                return new PermissionDialogMessage[size];
            }
        };
    }


    private final static String TAG = PermissionActivity.class.getSimpleName();
    private final static String PrefTag = PermissionActivity.class.toString();

    private final static String KEY_SHOWING_DIALOG_COUNT = "showing-dialog-count";
    private final static String KEY_DIALOG_MESSAGES = "dialog-message";
    private final static String KEY_REQUEST_QUEUE = "request-queue";
    private final static String KEY_REQUEST_CODE = "request-code";

    protected final static int PERMISSION_DENIED = 0;
    protected final static int PERMISSION_CANCELLED = 1;

    private SharedPreferences sharedPref;
    private int showingDialogCount;
    private PermissionListener mPermissionCallback;

    private List<PermissionDialogMessage> dialogMessages;
    private List<AlertDialog> infoDialogs;
    private List<String> requestQueue;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedState);

        sharedPref = getSharedPreferences(PrefTag, Context.MODE_PRIVATE);
        infoDialogs = new ArrayList<>();

        if (savedState == null) {
            dialogMessages = new ArrayList<>();
            requestQueue = new ArrayList<>();

        } else {
            showingDialogCount = savedState.getInt(KEY_SHOWING_DIALOG_COUNT);
            dialogMessages = savedState.getParcelableArrayList(KEY_DIALOG_MESSAGES);
            requestQueue = savedState.getStringArrayList(KEY_REQUEST_QUEUE);
            requestCode = savedState.getInt(KEY_REQUEST_CODE);

            for (PermissionDialogMessage message : dialogMessages) {
                showRequestInfoDialog(message);
            }
        }
    }

    public void requestAppPermissions(String[] requestedPermissions, int requestCode,
                                      @Nullable Map<String, PermissionDialogMessage> messages,
                                      PermissionListener responseCallback) {

        Log.d(TAG, "requestAppPermissions");

        if (responseCallback == null) {
            throw new NullPointerException("responseCallback can't be null!");
        }

        mPermissionCallback = responseCallback;
        this.requestCode = requestCode;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            showingDialogCount = 0;

            List<String> deniedQueue = new ArrayList<>();

            int permissionCheck = PackageManager.PERMISSION_GRANTED;

            for (int position = requestedPermissions.length - 1; position > -1; position--) {
                String permissionName = requestedPermissions[position];
                int currPermission = ContextCompat.checkSelfPermission(this, permissionName);

                permissionCheck += currPermission;

                if (currPermission == PackageManager.PERMISSION_DENIED) {
                    boolean shouldRequestPermission =
                            ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName);

                    if (shouldRequestPermission || !sharedPref.getBoolean(permissionName, false)) {

                        requestQueue.add(0, permissionName);

                        if (messages != null && messages.size() > 0) {
                            PermissionDialogMessage messageForCurrPermission
                                    = messages.get(permissionName);

                            if (messageForCurrPermission != null)
                                dialogMessages.add(messageForCurrPermission);
                        }
                    } else {
                        deniedQueue.add(0, permissionName);
                    }
                }
            }

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                mPermissionCallback.onPermissionsGranted(requestCode);

            } else if (!deniedQueue.isEmpty()) {
                requestQueue.clear();
                dialogMessages.clear();

                mPermissionCallback.onPermissionsDenied(requestCode, PERMISSION_DENIED, null,
                        deniedQueue);

            } else if (!dialogMessages.isEmpty()) {
                showingDialogCount = dialogMessages.size();

                for (PermissionDialogMessage message : dialogMessages) {
                    showRequestInfoDialog(message);
                }

            } else if (!requestQueue.isEmpty()) {
                askForPermissions();
            }

        } else {
            mPermissionCallback.onPermissionsGranted(requestCode);
        }
    }

    void showRequestInfoDialog(PermissionDialogMessage message) {
        Log.d(TAG, "showRequestInfoDialog");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(message.getTitle())
                .setMessage(message.getMessage())
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        infoDialogs.remove(infoDialogs.size() - 1);
                        dialogMessages.remove(dialogMessages.size() - 1);

                        if (--showingDialogCount == 0) {
                            askForPermissions();
                        }
                    }
                })
                .setCancelable(false)
                .create();

        infoDialogs.add(dialog);

        dialog.show();
    }

    private void askForPermissions() {
        Log.d(TAG, "askForPermissions");

        String permissions[] = new String[requestQueue.size()];
        for (int i = 0; i < requestQueue.size(); i++) {
            permissions[i] = requestQueue.get(i);
        }

        requestQueue.clear();

        // Check the result in onRequestPermissionsResult()
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        List<String> deniedForThisTime = null, deniedForAlways = null;

        for (int position = 0; position < grantResults.length; position++) {

            int currResult = grantResults[position];
            String permissionName = permissions[position];

            boolean isNeverShowAgainNotChecked =
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName);

            if (currResult == PackageManager.PERMISSION_DENIED) {

                if (isNeverShowAgainNotChecked) {
                    if (deniedForThisTime == null)
                        deniedForThisTime = new ArrayList<>();

                    deniedForThisTime.add(permissionName);
                } else {
                    if (deniedForAlways == null)
                        deniedForAlways = new ArrayList<>();

                    deniedForAlways.add(permissionName);
                    sharedPref.edit().putBoolean(permissionName, true).apply();
                }
            }

            permissionCheck += currResult;
        }

        if (mPermissionCallback != null) {
            if (grantResults.length > 0) {

                if (permissionCheck == PackageManager.PERMISSION_GRANTED)
                    mPermissionCallback.onPermissionsGranted(requestCode);

                else
                    mPermissionCallback.onPermissionsDenied(requestCode, PERMISSION_DENIED,
                            deniedForThisTime, deniedForAlways);

            } else {
                mPermissionCallback.onPermissionsDenied(requestCode, PERMISSION_CANCELLED,
                        deniedForThisTime, deniedForAlways);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: Save dialog state");
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SHOWING_DIALOG_COUNT, showingDialogCount);

        outState.putParcelableArrayList(KEY_DIALOG_MESSAGES,
                (ArrayList<? extends Parcelable>) dialogMessages);

        outState.putStringArrayList(KEY_REQUEST_QUEUE, (ArrayList<String>) requestQueue);

        outState.putInt(KEY_REQUEST_CODE, requestCode);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Destroy all dialogs");
        super.onDestroy();

        for (AlertDialog dialog : infoDialogs) {
            dialog.dismiss();
        }

        mPermissionCallback = null;
    }
}