package com.chwishay.chwsp00.activity

import android.Manifest
import android.os.Environment
import androidx.lifecycle.lifecycleScope
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.btUtil.BtTestActivity
import com.chwishay.chwsp00.views.LoadingDialog
import com.chwishay.commonlib.tools.*
import kotlinx.android.synthetic.main.activity_main_test.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

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
            lifecycleScope.launch {
                //行优先测试
//                    val data = arrayListOf<List<String>>().also {
//                        for (i in 0..999) {
//                            it.add(arrayListOf("name$i", "$i", if (i % 2 == 0) "男" else "女"))
//                        }
//                    }
                //列优先测试
//                val data = arrayListOf<List<String>>().also { sheets ->
//                    sheets.add(arrayListOf<String>().also {
//                        for (i in 0..999) {
//                            it.add("name$i")
//                        }
//                    })
//                    sheets.add(arrayListOf<String>().also {
//                        for (i in 0..999) {
//                            it.add("$i 岁")
//                        }
//                    })
//                    sheets.add(arrayListOf<String>().also {
//                        for (i in 0..999) {
//                            it.add(if (i % 2 == 0) "男" else "女")
//                        }
//                    })
//                }
                val fileName =
                    File("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/B88/export/")
                        .also { it.mkdirs() }
                        .let { "${it.absolutePath}/${System.currentTimeMillis()}.xls" }
//                ExcelUtil.instance.setFileInfo(
//                    fileName, arrayOf("testSheet0", "testSheet1", "testSheet2"), arrayOf(
//                        arrayOf("姓名", "年龄", "性别"), null, arrayOf("abc", "def")
//                    )
//                )
//                    .exportColFirst(arrayListOf(data, data, data))
                //单元素列表测试
                val data = arrayListOf<ExcelUtil.ExcelItem>().also {
                    for (row in 0..99) {
                        for (col in 0..99) {
                            it.add(ExcelUtil.ExcelItem(row, col, "$row * $col = ${row * col}"))
                        }
                    }
                }

                ExcelUtil.instance.setFileInfo(
                    fileName,
                    arrayOf("testSheet0", "testSheet1", "testSheet2"),
                    arrayOf(arrayOf("姓名", "年龄", "性别"), null, arrayOf("abc", "def"))
                )
                    .exportDataList(arrayListOf(data, data))

//                    .insertImageFile(0, 5, 0, File("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/B88/export/10018_zhaoyun_1608083881214.png"))
//                runOnUiThread {
                toast("已保存")
//                }
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

        btnCopyFile.onClick {
//            lifecycleScope.launch {
            File("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/B88/").let { dir ->
                dir.listFiles()
                    .filter { file -> file.isFile && file.name.endsWith(".txt") }[0].copyTo(
                    File("${Environment.getExternalStorageDirectory().absolutePath}/CHWS/copy/copy_file.txt")
                )
            }
            toast("拷贝成功")
//            }
        }

        btnMemory.onClick {
            Environment.getExternalStorageDirectory().apply {
//            tvMemory.text = "totalMemory:${Formatter.formatFileSize(this@MainTestActivity, totalSpace)}  availabelMemory:${Formatter.formatFileSize(this@MainTestActivity, usableSpace)}"
                tvContent.text =
                    "totalMemory:${totalSpace / 1000f / 1000 / 1000}G  availabelMemory:${usableSpace / 1000f / 1000 / 1000}G"
//                Calendar.getInstance(Locale.CHINESE).also {
//                    it.set(2020, 11, 23,0, 0 , 0)
//                    val from = it.timeInMillis
//                    it.set(2020, 11, 23,23, 59 , 59)
//                    val to = it.timeInMillis
//                    tvContent.text = File("${Environment.getExternalStorageDirectory().absolutePath}/chws/B88").getFilesByTimeRange(from, to)?.contentDeepToString()
//                }
            }
        }

        btnCmd.onClick {
//            tvContent.text = CmdUtil.getStartSyncCmd()?.contentToString()
            tvContent.text = CmdUtil.getTimeSyncCmd()?.contentToString()
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