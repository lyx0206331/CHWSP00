package com.chwishay.chwsp00.activity

import android.Manifest
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.views.LoadingDialog
import com.chwishay.commonlib.tools.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
    private val permissionUtil : PermissionUtil by lazy { PermissionUtil(this, permissions) }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initVariables() {
    }

    override fun initViews() {
        btnShowPD.setOnClickListener { LoadingDialog(this).show() }
        btnHidePD.setOnClickListener { /*permissionUtil.requestPermission()*/ChartActivity.startActivity(this) }
    }

    override fun loadData() {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}