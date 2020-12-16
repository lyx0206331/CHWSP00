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
import org.jetbrains.anko.toast
import java.io.File
import kotlin.concurrent.thread

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
        //region +哈哈
        btnSave2Excel.setOnClickListener {

            //region +折叠
            thread {
                //行优先测试
//                    val data = arrayListOf<List<String>>().also {
//                        for (i in 0..999) {
//                            it.add(arrayListOf("name$i", "$i", if (i % 2 == 0) "男" else "女"))
//                        }
//                    }
                //列优先测试
                val data = arrayListOf<List<String>>().also { sheets ->
                    sheets.add(arrayListOf<String>().also {
                        for (i in 0..999) {
                            it.add("name$i")
                        }
                    })
                    sheets.add(arrayListOf<String>().also {
                        for (i in 0..999) {
                            it.add("$i 岁")
                        }
                    })
                    sheets.add(arrayListOf<String>().also {
                        for (i in 0..999) {
                            it.add(if (i % 2 == 0) "男" else "女")
                        }
                    })
                }
                val fileName =
                    File("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/B88/export/")
                        .also { it.mkdirs() }
                        .let { "${it.absolutePath}/${System.currentTimeMillis()}.xls" }
                ExcelUtil.instance.setFileInfo(
                    fileName, arrayOf("testSheet0", "testSheet1", "testSheet2"), arrayOf(
                        arrayOf("姓名", "年龄", "性别"), null, arrayOf("abc", "def")
                    )
                )
                    .exportColFirst(arrayListOf(data, data, data))
//                    .insertImageFile(0, 5, 0, File("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/B88/export/10018_zhaoyun_1608083881214.png"))
                runOnUiThread {
                    toast("已保存")
                }
            }
            //endregion
        }
        //endregion
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