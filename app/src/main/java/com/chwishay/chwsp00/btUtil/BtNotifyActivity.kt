package com.chwishay.chwsp00.btUtil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.model.BleDeviceInfo
import com.chwishay.chwsp00.utils.Observer
import com.chwishay.chwsp00.utils.ObserverManager
import com.chwishay.commonlib.tools.formatHexString
import com.chwishay.commonlib.tools.hexString2Bytes
import com.chwishay.commonlib.tools.showShortToast
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import kotlinx.android.synthetic.main.activity_bt_notify.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class BtNotifyActivity : BaseActivity(), Observer {

    companion object {

        //ble设备服务UUID
        const val SERVICE_UUID = "8653000a-43e6-47b7-9cb0-5fc21d4ae340"

        //ble设备特征值UUID
        const val CHARACTERISTICS_UUID = "8653000c-43e6-47b7-9cb0-5fc21d4ae340"

        //同步数据命令
        const val CMD_SYNC_DATA = "5A"

        //停止采集命令
        const val CMD_STOP_COLLECT = "AA"

        @JvmStatic
        fun startActivity(context: Context, devices: ArrayList<BleDevice>) {
            val intent = Intent(context, BtNotifyActivity::class.java)
            intent.putParcelableArrayListExtra("devices", devices)
            context.startActivity(intent)
        }
    }

    private val adapter by lazy {
        NotifyAdapter(this)
    }

    private var devices: ArrayList<BleDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bt_notify
    }

    override fun initVariables() {
        devices = intent.getParcelableArrayListExtra("devices")
    }

    private fun getDeviceInfos(): ArrayList<BleDeviceInfo> {
        val list = arrayListOf<BleDeviceInfo>()
        devices?.forEach {
            list.add(BleDeviceInfo(it))
        }
        return list
    }

    override fun initViews() {
        btnSyncData.onClick {
            sendCmd(CMD_SYNC_DATA)
        }
        btnStopCollect.onClick {
            sendCmd(CMD_STOP_COLLECT)
        }
        rvNotify.adapter = adapter
        adapter.devices = getDeviceInfos()
        //处理RecyclerView内外滑动冲突问题
        rvNotify.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y)?.apply {
//                    val holder = rv.getChildViewHolder(this) as NotifyAdapter.NotifyViewHolder
                    (this as ViewGroup).requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    private fun sendCmd(cmd: String) {
        devices?.forEach {
            BleManager.getInstance().write(it,
                SERVICE_UUID,
                CHARACTERISTICS_UUID,
                cmd.hexString2Bytes(),
                object : BleWriteCallback() {
                    override fun onWriteSuccess(
                        current: Int,
                        total: Int,
                        justWrite: ByteArray?
                    ) {
                        showShortToast("向${it.name}发送指令${justWrite?.formatHexString(" ")}成功,时间:${System.currentTimeMillis()}")
                    }

                    override fun onWriteFailure(exception: BleException?) {
                        showShortToast("向${it.name}发送指令失败:${exception.toString()}")
                    }
                })
        }
    }

    override fun loadData() {
        ObserverManager.getInstance().addObservable(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        devices?.forEach {
            BleManager.getInstance().clearCharacterCallback(it)
        }
        ObserverManager.getInstance().deleteObserver(this)
    }

    override fun disConnected(bleDevice: BleDevice) {
        devices?.forEach {
            if (bleDevice != null && it.key == bleDevice.key) {
                showShortToast("设备${it.key}已断开")
            }
        }
    }
}