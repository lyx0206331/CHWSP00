package com.chwishay.chwsp00.btUtil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.clj.fastble.data.BleDevice
import kotlinx.android.synthetic.main.activity_bt_notify.*

class BtNotifyActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun startActivity(context: Context, devices: ArrayList<BleDevice>) {
            val intent = Intent(context, BtNotifyActivity::class.java)
            intent.putParcelableArrayListExtra("devices", devices)
            context.startActivity(intent)
        }
    }

    private val adapter by lazy {
        NotifyAdapter {
            saveData2File(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bt_notify
    }

    override fun initVariables() {
    }

    override fun initViews() {
        rvNotify.adapter = adapter
        adapter.devices = intent.getParcelableArrayListExtra("devices")
    }

    override fun loadData() {
    }

    private fun saveData2File(data: String) {}
}