package com.chwishay.btlib

import android.annotation.TargetApi
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.chwishay.commonlib.tools.orDefault

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
 * Create time:20-8-5 上午11:19
 * Description:
 **/
@RequiresApi(Build.VERSION_CODES.ECLAIR)
class BTConnectUtil(private val activity: Activity) {

    private var btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var btManager: BluetoothManager? = if (isBleSupported()) activity.getSystemService(
        Context.BLUETOOTH_SERVICE) as BluetoothManager else null

    companion object {
        const val REQUEST_ENABLE_BT = 999
//        val instance: BTConnectUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { BTConnectUtil() }
//        @Volatile
//        private var instance: BTConnectUtil? = null
//        fun getInstance(activity: Activity) = instance ?: synchronized(this) { instance ?: BTConnectUtil(activity).also  { instance = it }}
    }

    /**
     * 是否支持蓝牙
     */
    private fun isBluetoothSupported() = btAdapter != null

    /**
     * 是否支持BLE
     */
    private fun isBleSupported() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    /**
     * 是否已打开蓝牙
     */
    private fun isBluetoothEnabled() = btAdapter?.state == BluetoothAdapter.STATE_ON

    /**
     * 蓝牙是否可用
     */
    fun isEnable(): Boolean {
        return if (!isBluetoothSupported()) {
            throw IllegalStateException("设备不支持蓝牙")
        } else {
            btAdapter?.isEnabled.orDefault()
        }
    }

    /**
     * 打开蓝牙
     */
    fun openBluetooth() = btAdapter?.enable().orDefault()

    /**
     * 打开蓝牙
     */
    fun openBluetooth2() {
        if (!isEnable()) {
            activity.startActivityForResult(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                REQUEST_ENABLE_BT
            )
        }
    }

    /**
     * 关闭蓝牙
     */
    fun closeBluetooth() = btAdapter?.disable().orDefault()

    fun getRemoteDevice(mac: String): BluetoothDevice? =
        if (!TextUtils.isEmpty(mac)) btAdapter?.getRemoteDevice(mac) else null

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getConnectedBleDevices(): List<BluetoothDevice> {
        val devices = arrayListOf<BluetoothDevice>()
        btManager?.getConnectedDevices(BluetoothProfile.GATT)?.let { devices.addAll(it) }
        return devices
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getConnectStatus(mac: String): Int {
        val device = getRemoteDevice(mac)
        return btManager?.getConnectionState(device, BluetoothProfile.GATT).orDefault(-1)
    }

    fun getBondState(mac: String): Int =
        getRemoteDevice(mac)?.bondState.orDefault(BluetoothDevice.BOND_NONE)

    fun getBondedClassicBtDevices(): List<BluetoothDevice>? = btAdapter?.bondedDevices?.toList()

    fun isConnected(mac: String): Boolean =
        getConnectStatus(mac) == BluetoothProfile.STATE_CONNECTED
}