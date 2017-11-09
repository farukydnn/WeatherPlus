package com.farukydnn.weatherplus.core.ui;

import com.farukydnn.weatherplus.interfaces.PermissionListener;

import java.util.Map;

/**
 * Created by Faruk Aydın on 17.10.2017.
 **/

public class PermissionFragment extends BaseFragment {

    public void requestAppPermissions(PermissionActivity permissionActivity,
                                      final String[] requestedPermissions, final int requestCode,
                                      final Map<String,
                                              PermissionActivity.PermissionDialogMessage> messages,
                                      PermissionListener responseCallback) {

        permissionActivity.requestAppPermissions(requestedPermissions, requestCode, messages,
                responseCallback);
    }
}
