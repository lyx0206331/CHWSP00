package com.chwishay.chwsp00.btUtil

import android.bluetooth.BluetoothGatt
import android.text.TextUtils
import androidx.annotation.IntDef
import androidx.lifecycle.MutableLiveData
import com.chwishay.chwsp00.utils.ObserverManager
import com.chwishay.commonlib.baseComp.BaseAct
import com.chwishay.commonlib.tools.logE
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.clj.fastble.scan.BleScanRuleConfig
import java.util.*
import kotlin.collections.ArrayList

//                       _ooOoo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                       O\ = /O
//                   ____/`---'\____
//                 .   ' \\| |// `.
//                  / \\||| : |||// \
//                / _||||| -:- |||||- \
//                  | | \\\ - /// | |
//                | \_| ''\---/'' | |
//                 \ .-\__ `-` ___/-. /
//              ______`. .' /--.--\ `. . __
//           ."" '< `.___\_<|>_/___.' >'"".
//          | | : `- \`.;`\ _ /`;.`/ - ` : | |
//            \ \ `-. \_ __\ /__ _/ .-` / /
//    ======`-.____`-.___\_____/___.-`____.-'======
//                       `=---='
//
//    .............................................
//             佛祖保佑             永无BUG
/**
 * author:RanQing
 * date:2021/6/4 0004 14:02
 * description:
 */
object BleUtil {

    const val SCAN_STATE_START = 0
    const val SCAN_STATE_SCANNING = 1
    const val SCAN_STATE_FINISH = 2

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SCAN_STATE_START, SCAN_STATE_SCANNING, SCAN_STATE_FINISH)
    annotation class ScanState

    const val CONN_STATE_START = 0
    const val CONN_STATE_SUCCESS = 1
    const val CONN_STATE_FAIL = 2
    const val CONN_STATE_DISCONNECT = 3

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(CONN_STATE_START, CONN_STATE_SUCCESS, CONN_STATE_FAIL, CONN_STATE_DISCONNECT)
    annotation class ConnState

    val scanStateLiveData = MutableLiveData<@ScanState Int>()
    val connectStateLiveData = MutableLiveData<@ConnState Int>()
    val scannedDevsLiveData = MutableLiveData<List<BleDevice>>()
    val connectedDevsLiveData = MutableLiveData<List<BleDevice>>()

    private val scannedList = ArrayList<BleDevice>()
    private val connectedList = ArrayList<BleDevice>()

    fun startScan(nameFilter: String? = "GAIT_DEVICE_CHWS") {
        BleManager.getInstance().initScanRule(
            BleScanRuleConfig.Builder()
                .setServiceUuids(arrayOf(UUID.fromString(BtNotifyActivity.SERVICE_UUID)))
                .setDeviceName(true, nameFilter)
                .setDeviceMac("").setAutoConnect(false).setScanTimeOut(10000).build()
        )
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                BaseAct.TAG.logE("onScanStarted")
                scannedList.clear()
                scannedDevsLiveData.postValue(scannedList.toMutableList())
                scanStateLiveData.postValue(SCAN_STATE_START)
            }

            override fun onScanning(bleDevice: BleDevice?) {
                BaseAct.TAG.logE("${bleDevice.toString()}")
                scanStateLiveData.postValue(SCAN_STATE_SCANNING)
                bleDevice?.let {
                    if (!TextUtils.isEmpty(it.name)/* && it.name.startsWith(nameFilter)*/) {
                        scannedList.add(bleDevice)
                        scannedDevsLiveData.postValue(scannedList.toMutableList())
                    }
                }
            }

            override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                BaseAct.TAG.logE("onScanFinished")
                scanStateLiveData.postValue(SCAN_STATE_FINISH)
            }
        })
    }

    fun stopScan() {
        BleManager.getInstance().cancelScan()
    }

    fun connect(bleDevice: BleDevice?) {
        BleManager.getInstance().connect(bleDevice, object : BleGattCallback() {
            override fun onStartConnect() {
                connectStateLiveData.postValue(CONN_STATE_START)
            }

            override fun onConnectFail(bleDevice: BleDevice?, exception: BleException?) {
                connectStateLiveData.postValue(CONN_STATE_FAIL)
            }

            override fun onConnectSuccess(
                bleDevice: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int
            ) {
                connectStateLiveData.postValue(CONN_STATE_SUCCESS)
                bleDevice?.let {
                    scannedList.removeDev(it)
                    scannedList.add(it)
                    scannedDevsLiveData.postValue(scannedList.toMutableList())
                    connectedList.removeDev(it)
                    connectedList.add(bleDevice)
                    connectedDevsLiveData.postValue(connectedList.toMutableList())
                }
//                BleManager.getInstance().setMtu(bleDevice, 37, object : BleMtuChangedCallback() {
//                    override fun onSetMTUFailure(exception: BleException?) {
//                        showShortToast("MTU修改失败")
//                    }
//
//                    override fun onMtuChanged(mtu: Int) {
//                        TAG.logE("MTU修改为$mtu")
//                    }
//                })
            }

            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                device: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int
            ) {
                connectStateLiveData.postValue(CONN_STATE_DISCONNECT)
                scannedList.removeDev(device)
                scannedDevsLiveData.postValue(scannedList.toMutableList())
                connectedList.removeDev(device)
                connectedDevsLiveData.postValue(connectedList.toMutableList())
                if (isActiveDisConnected) {
                } else {
                    bleDevice?.apply { ObserverManager.getInstance().notifyObserver(this) }
                }
            }
        })
    }

    private fun ArrayList<BleDevice>.removeDev(device: BleDevice?) {
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val i = iterator.next()
            if (i.key == device?.key) {
                iterator.remove()
            }
        }
    }
}