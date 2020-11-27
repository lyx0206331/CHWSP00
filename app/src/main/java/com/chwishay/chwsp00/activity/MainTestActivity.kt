package com.chwishay.chwsp00.activity

import android.Manifest
import android.os.Environment
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.btUtil.BtTestActivity
import com.chwishay.chwsp00.views.LoadingDialog
import com.chwishay.commonlib.tools.*
import kotlinx.android.synthetic.main.activity_main_test.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainTestActivity : BaseActivity() {

    private val permissions1 = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val permissionUtil: PermissionUtil by lazy { PermissionUtil(this) }
    private val permissions2 = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main_test
    }

    override fun initVariables() {
    }

    override fun initViews() {
        btnShowPD.setOnClickListener { LoadingDialog(this).show() }
        btnTestChart.setOnClickListener { /*permissionUtil.requestPermission()*/ChartActivity.startActivity(
            this
        )
        }
        btnSave2Excel.setOnClickListener {
            val data = arrayListOf<TestModel>().apply {
                for (i in 0..99) {
                    add(TestModel("name$i", i, if (i % 2 == 0) "男" else "女"))
                }
            }
            val dir =
                FileUtil.getDiretory("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/Test").absolutePath
            ExcelUtil.instance.initExcel("$dir/test.xls", "testSheet", arrayOf("姓名", "年龄", "性别"))
                .export2Excel(data)
        }
        btnTestBt.onClick {
            permissionUtil.requestPermission(
                permissions2,
                object : PermissionUtil.IPermissionCallback {
                    override fun allowedPermissions() {
                        CommUtil.logE("req permission", "permission2")
                        BtTestActivity.startActivity(this@MainTestActivity)
                    }

                    override fun deniedPermissions() {
                        permissionUtil.showTips("需要定位权限，是否手动设置?")
                    }
                })
        }
    }

    override fun loadData() {
        permissionUtil.requestPermission(permissions1, object : PermissionUtil.IPermissionCallback {
            override fun allowedPermissions() {
                CommUtil.logE("req permission", "permissions1")
            }

            override fun deniedPermissions() {
                permissionUtil.showTips()
            }
        })
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