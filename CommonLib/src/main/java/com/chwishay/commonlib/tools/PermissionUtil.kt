package com.chwishay.commonlib.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//   ┃　　　┃   神兽保佑
//   ┃　　　┃   代码无BUG！
//   ┃　　　┗━━━┓
//   ┃　　　　　　　┣┓
//   ┃　　　　　　　┏┛
//   ┗┓┓┏━┳┓┏┛
//     ┃┫┫　┃┫┫
//     ┗┻┛　┗┻┛
/**
 * Author:RanQing
 * Create time:20-8-4 上午11:12
 * Description:权限请求设置工具类
 **/
class PermissionUtil(private val activity: Activity, private val permissions: Array<String>) {

    companion object {

        const val PERMISSION_REQ_CODE = 1
//        @Volatile
//        private var instance: PermissionUtil? = null
//        fun getInstance(activity: Activity, permissions: Array<String>) = instance ?: synchronized(this) { instance ?: PermissionUtil(activity, permissions).also  { instance = it }}
    }

    /**
     * 请求权限
     */
    fun requestPermission() {
        if (!permissions.isNullOrEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && lacksPermissions()) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQ_CODE)
        }
    }

    /**
     * 检测是否缺少权限
     */
    private fun lacksPermissions(): Boolean {
        permissions?.forEach {
            if (ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    /**
     * 此方法在Activity中重写的onRequestPermissionResult(...)方法中调用
     */
    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<out String>,
                                   grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_REQ_CODE -> {
                if (grantResults.size == this.permissions?.size) {
                    grantResults.forEachIndexed { index, i ->
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            CommUtil.toastShort(activity, "请求${permissions[index]}授权被拒绝")
//                            goToSetting()
                        } else {
                            CommUtil.toastShort(activity, "${permissions[index]}已授权")
                        }
                    }
                }
            }
        }
    }

    /**
     * 跳转到权限详情设置页
     */
    fun goToSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }
}