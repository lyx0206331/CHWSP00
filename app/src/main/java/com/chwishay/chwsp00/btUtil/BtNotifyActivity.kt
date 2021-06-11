package com.chwishay.chwsp00.btUtil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.chwishay.chwsp00.NotifyViewModel
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.model.BleDeviceInfo
import com.chwishay.chwsp00.utils.Observer
import com.chwishay.chwsp00.utils.ObserverManager
import com.chwishay.commonlib.tools.CmdUtil
import com.chwishay.commonlib.tools.logE
import com.chwishay.commonlib.tools.orDefault
import com.chwishay.commonlib.tools.showShortToast
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import kotlinx.android.synthetic.main.activity_bt_notify.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

class BtNotifyActivity : BaseActivity(), Observer {

    companion object {

        //ble设备服务UUID
        const val SERVICE_UUID = "8653000a-43e6-47b7-9cb0-5fc21d4ae340"

        //ble设备特征值UUID
        const val CHARACTERISTICS_WRITE_UUID = "8653000c-43e6-47b7-9cb0-5fc21d4ae340"

        @JvmStatic
        fun startActivity(context: Context, devices: ArrayList<BleDevice>) {
            val intent = Intent(context, BtNotifyActivity::class.java)
            intent.putParcelableArrayListExtra("devices", devices)
            context.startActivity(intent)
        }
    }

    private val vm: NotifyViewModel by viewModels()

    private val adapter by lazy {
        NotifyAdapter(this) {
            sendCmd(it.bleDevice, CmdUtil.getTimeSyncCmd())
        }
    }

    private var devices: ArrayList<BleDeviceInfo>? = null

    private val startCmd by lazy { CmdUtil.getStartSyncCmd() }
    private val stopCmd by lazy { CmdUtil.getStopSyncCmd() }

    private var clickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bt_notify
    }

    override fun initVariables() {
        val devs: ArrayList<BleDevice>? = intent.getParcelableArrayListExtra("devices")
        devices = getDeviceInfos(devs)
    }

    private fun getDeviceInfos(devs: ArrayList<BleDevice>?): ArrayList<BleDeviceInfo> {
        val list = arrayListOf<BleDeviceInfo>()
        devs?.forEach {
            list.add(BleDeviceInfo(it))
        }
        return list
    }

    override fun initViews() {

//        btnSyncData.onClick {
////            adapter.dataIndex = 0
//            sendSyncCmd(CmdUtil.getStartSyncCmd())
//        }
//        btnCheckTime.onClick {
//            checkTime()
//        }
//        btnStopCollect.onClick {
//            sendStopCmd(/*CMD_STOP_COLLECT*/CmdUtil.getStopSyncCmd())
//        }
        btnStart.onClick {
            if (etUserName.text?.trim().isNullOrEmpty() || etUserId.text?.trim().isNullOrEmpty()) {
                showShortToast("请输入有效用户姓名和病历号")
            } else {
                if (!vm.isStartLiveData.value.orDefault()) {
                    clickTime = System.currentTimeMillis()
                    sendSyncCmd()
//                vm.isStartLiveData.value = true
                } else if (vm.isStartLiveData.value.orDefault()) {
                    if (System.currentTimeMillis() < clickTime + 10000) {
                        showShortToast("请开始10秒后再停止!")
                    } else {
                        sendStopCmd(/*CMD_STOP_COLLECT*/)
                    }
//                vm.isStartLiveData.value = false
                }
            }
        }
        rvNotify.adapter = adapter
        adapter.devices = devices
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

        vm.isStartLiveData.observe(this) {
            btnStart.text = if (it) "停止采集" else "开始采集"
            etUserName.isEnabled = !it
            etUserId.isEnabled = !it
            if (it) {
                adapter.fileName = "${etUserName.text}_${etUserId.text}"
                devices?.forEach { dev ->
                    dev.totalSize = 0
                    dev.needSave = true
                }
            } else {
                devices?.forEach { dev ->
                    dev.needSave = false
                }
            }
        }
    }

    /**
     * 校正时间
     */
    private fun checkTime() {
        adapter.devices?.forEach {
            sendCmd(it.bleDevice, CmdUtil.getTimeSyncCmd())
        }
    }

    private fun sendSyncCmd() {
        adapter.devices?.get(0)?.let {
            it.sysTime = 0L
            adapter.resetFirstFrameMarks()
            sendCmd(it.bleDevice, startCmd)
        }
//        adapter.devices?.forEach {
//            it.sysTime = 0L
//            sendCmd(it.bleDevice, cmd)
//        }
    }

    private fun sendStopCmd() {
        adapter.devices?.forEach {
            it.sysTime = 0L
            sendCmd(it.bleDevice, stopCmd)
        }
    }

    private fun sendCmd(device: BleDevice, cmd: ByteArray) {
        "CMD".logE("devMac:${device.mac}  cmd:${cmd.contentToString()}")
        BleManager.getInstance().write(
            device,
            SERVICE_UUID,
            CHARACTERISTICS_WRITE_UUID,
            cmd,
            object : BleWriteCallback() {
                override fun onWriteSuccess(
                    current: Int,
                    total: Int,
                    justWrite: ByteArray?
                ) {

                    when {
                        justWrite?.contentEquals(startCmd) == true -> {
//                            "CMD".logE("Start:${startCmd?.contentToString()} == ${justWrite?.contentToString()}")
                            vm.isStartLiveData.value = true
                        }
                        justWrite?.contentEquals(stopCmd) == true -> {
//                            "CMD".logE("Stop:${stopCmd?.contentToString()} == ${justWrite?.contentToString()}")
                            vm.isStartLiveData.value = false
                        }
                        else -> {
                            showShortToast("向${device.name}发送指令${justWrite?.contentToString()}成功,时间:${System.currentTimeMillis()}")
                        }
                    }
                }

                override fun onWriteFailure(exception: BleException?) {
                    showShortToast("向${device.name}发送指令失败:${exception.toString()}")
                }
            })
    }

    override fun loadData() {
        ObserverManager.getInstance().addObservable(this)
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            if (vm.isStartLiveData.value.orDefault()) {
                sendStopCmd()
                delay(200)
            }
            adapter.devices?.forEach {
                it.stopReceive()
                BleManager.getInstance()
                    .stopNotify(it.bleDevice, it.serviceUUID.toString(), it.notifyUUID.toString())
            }
            delay(200)
            showShortToast("返回")
            super.onBackPressed()
        }
    }

    override fun onDestroy() {

        super.onDestroy()
        rvNotify.adapter = null
        devices?.forEach {
            BleManager.getInstance().clearCharacterCallback(it.bleDevice)
        }
        ObserverManager.getInstance().deleteObserver(this)
    }

    override fun disConnected(bleDevice: BleDevice) {
        devices?.forEach {
            if (bleDevice.device != null && it.bleDevice.key == bleDevice.key) {
                showShortToast("设备${it.bleDevice.key}已断开")
            }
        }
    }
}