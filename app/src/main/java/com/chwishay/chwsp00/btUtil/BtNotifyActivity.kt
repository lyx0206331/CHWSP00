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
import com.chwishay.commonlib.tools.CmdUtil
import com.chwishay.commonlib.tools.logE
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
        val CMD_SYNC_DATA = byteArrayOf(
            0xFA.toByte(), 0xFF.toByte(),
            0xA3.toByte(), 0x01, 0x5A, 0x02
        )

        //停止采集命令
        val CMD_STOP_COLLECT = byteArrayOf(
            0xFA.toByte(), 0xFF.toByte(),
            0xA4.toByte(), 0x01, 0xAA.toByte(), 0xB1.toByte()
        )

        private var timeEquation = 0L

        @JvmStatic
        fun startActivity(context: Context, devices: ArrayList<BleDevice>) {
            val intent = Intent(context, BtNotifyActivity::class.java)
            intent.putParcelableArrayListExtra("devices", devices)
            context.startActivity(intent)
        }
    }

    private val adapter by lazy {
        NotifyAdapter(this) {
            sendCmd(it.bleDevice, CmdUtil.getTimeSyncCmd())
//            if (!it.isNullOrEmpty() && it.size > 1 && it[0].syncTime > 0 && it[1].syncTime > 0) {
//                timeEquation = kotlin.math.abs(it[1].syncTime - it[0].syncTime)
//                btnSyncData.text = "同步数据(时差:$timeEquation)"
////                if (timeEquation >= 30) {
////                    it?.minWith(Comparator { o1, o2 ->
////                        (o1.syncTime - o2.syncTime).toInt()
////                    })?.apply {
////                        "cmd".logE("timeEqShort:${timeEquation.toShort()}, bytes:${timeEquation.toShort().toBytesLE().read2IntLE()}")
////                        val timeEqBytes = timeEquation.toShort().toBytesLE()
////                        val cmdBytes = byteArrayOf(0xA5.toByte(), *timeEqBytes, 0xA5.toByte())
////                        sendCmd(bleDevice,cmdBytes)
////                    }
////                }
////                it[0].syncTime = 0
////                it[1].syncTime = 0
//            }
        }
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
//            adapter.dataIndex = 0
            sendSyncCmd(/*CMD_SYNC_DATA*/CmdUtil.getStartSyncCmd())
        }
        btnCheckTime.onClick {
            checkTime()
        }
        btnStopCollect.onClick {
            sendCollectCmd(/*CMD_STOP_COLLECT*/CmdUtil.getStopSyncCmd())
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

//        checkTime()
    }

    /**
     * 校正时间
     */
    private fun checkTime() {
        adapter.devices?.forEach {
            sendCmd(it.bleDevice, CmdUtil.getTimeSyncCmd())
        }
    }

    private fun sendSyncCmd(cmd: ByteArray) {
        adapter.devices?.get(0)?.let {
            it.sysTime = 0L
            sendCmd(it.bleDevice, cmd)
        }
    }

    private fun sendCollectCmd(cmd: ByteArray) {
        adapter.devices?.forEach {
            it.sysTime = 0L
            sendCmd(it.bleDevice, cmd)
        }
    }

    private fun sendCmd(device: BleDevice, cmd: ByteArray) {
        "CMD".logE("devMac:${device.mac}  cmd:${cmd.contentToString()}")
        BleManager.getInstance().write(
            device,
            SERVICE_UUID,
            CHARACTERISTICS_UUID,
            cmd,
            object : BleWriteCallback() {
                override fun onWriteSuccess(
                    current: Int,
                    total: Int,
                    justWrite: ByteArray?
                ) {
                    showShortToast("向${device.name}发送指令${justWrite?.contentToString()}成功,时间:${System.currentTimeMillis()}")
                }

                override fun onWriteFailure(exception: BleException?) {
                    showShortToast("向${device.name}发送指令失败:${exception.toString()}")
                }
            })
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