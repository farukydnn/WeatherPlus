package com.farukydnn.weatherplus.interfaces;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Faruk Aydın on 17.10.2017.
 **/

public interface PermissionListener {

    void onPermissionsGranted(int requestCode);

    void onPermissionsDenied(int requestCode, int errorType,
                             @Nullable List<String> permissionsThisTimeDenied,
                             @Nullable List<String> permissionsAlwaysDenied);
}
