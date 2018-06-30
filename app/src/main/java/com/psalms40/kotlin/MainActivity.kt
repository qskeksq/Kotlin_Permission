package com.psalms40.kotlin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 체크할 퍼미션
    private val permissions = arrayOf(Manifest.permission.CAMERA)
    private val REQ_CODE = 999

    private fun postPermissionGranted() {

    }

    fun checkVersion() {
        // 마시멜로우 이전 버전에서는 권한 승인을 넘어간다
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            postPermissionGranted()
        } else {
            // 권한을 이미 받았는지 확인
            checkAlreadyGrantedPermission()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkAlreadyGrantedPermission() {
        // 만약 하나라도 승인이 안 된 권한이 있으면 isAllGranted = false
        val isAllGranted = permissions.none { checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }
        // 모든 권한이 승인됬으면 콜백 코드 실행
        if (isAllGranted) {
            postPermissionGranted()
            // 승인되지 요청이 있는 경우 권한 요청
        } else {
            requestPermissions(permissions, REQ_CODE)
        }
    }

    @Override
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // 사용자가 권한을 체크했는지 확인
        val isAllGranted = grantResults.none { it == PackageManager.PERMISSION_DENIED }
        // 전부 승인했을 경우 코드 실행
        if (isAllGranted) {
            postPermissionGranted()
            // 승인하지 않았을 경우 다시 한 번 왜 권한이 필요한지 대화상자를 띄워준다
        } else {
            showAskAgainDialog()
        }
    }

    private fun showAskAgainDialog() {
        val dialog = AlertDialog.Builder(this).setTitle("권한 설정 필요").setMessage("현재 기능을 사용하기 위해서는 해당 권한 설정이 필요합니다. 설정 페이지로 넘어가시겠습니까?")
                .setPositiveButton("예", {_, _ -> goSettings()})
                .setNegativeButton("아니오", null)
                .create()
        dialog.show()
    }

    private fun goSettings() {
        // 설정창을 패키지 이름으로 실행시킨다
        val intent =  Intent(Settings.ACTION_APPLICATION_SETTINGS, Uri.fromParts("package", packageName, null))
        startActivity(intent)
    }

}
