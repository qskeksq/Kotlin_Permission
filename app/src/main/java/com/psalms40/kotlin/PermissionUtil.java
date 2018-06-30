package com.psalms40.kotlin;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by mac on 2018. 2. 4..
 */

public class PermissionUtil {

    // 체크할 퍼미션
    private static String[] permissions = {Manifest.permission.CAMERA};
    private static final int REQ_CODE = 999;
    private static CameraPermissionCallBack sCallBack;


    public static void checkVersion(Activity activity, CameraPermissionCallBack callback) {
        sCallBack = callback;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.postCameraPermission();
        } else {
            checkAlreadyGrantedPermission(activity);
        }
    }

    /**
     * 이미 체크된 퍼미션이 있는지 확인하고, 체크되지 않았다면 시스템에 onRequestPermission()으로 권한을 요청한다.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void checkAlreadyGrantedPermission(Activity activity) {
        boolean isAllGranted = true;
        for (String perm : permissions) {
            if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
            }
        }
        if (isAllGranted) {
            sCallBack.postCameraPermission();
        } else {
            activity.requestPermissions(permissions, REQ_CODE);
        }
    }


    /**
     * 시스템 권한체크가 끝난 후 호출
     */
    public static  void onResult(int[] grantResults, Activity activity) {
        boolean isAllGranted = true;
        for (int granted : grantResults) {
            if (granted != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
            }
        }
        if (isAllGranted) {
            sCallBack.postCameraPermission();
        }
    }

    /**
     * MainActivity가 스스로를 넘겨주면, 이곳에서 MainActivity 를 대신해 메소드를 호출해주는 콜백 메서드
     */
    public interface CameraPermissionCallBack {
        void postCameraPermission();
    }

}
