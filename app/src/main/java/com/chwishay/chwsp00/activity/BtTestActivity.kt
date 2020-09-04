package com.chwishay.chwsp00.activity

import android.app.ProgressDialog
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.Intent
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.chwishay.chwsp00.BuildConfig
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.adapter.DeviceAdapter
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.utils.ObserverManager
import com.chwishay.commonlib.tools.showShortToast
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.clj.fastble.scan.BleScanRuleConfig
import kotlinx.android.synthetic.main.activity_bt_test.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textResource

class BtTestActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BtTestActivity::class.java))
        }
    }

    private val operatingAnim by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate)
            .apply { interpolator = LinearInterpolator() }
    }
    private val deviceAdapter by lazy {
        DeviceAdapter(this).apply {
            setOnDeviceClickListener(object : DeviceAdapter.OnDeviceClickListener {
                override fun onConnect(bleDevice: BleDevice?) {
                    if (!BleManager.getInstance().isConnected(bleDevice)) {
                        BleManager.getInstance().cancelScan()
                        connect(bleDevice)
                    }
                }

                override fun onDisConnect(bleDevice: BleDevice?) {
                    TODO("Not yet implemented")
                }

                override fun onDetail(bleDevice: BleDevice?) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
    private val progressDialog by lazy { ProgressDialog(this) }

    override fun getLayoutId(): Int {
        return R.layout.activity_bt_test
    }

    override fun initVariables() {
    }

    override fun initViews() {
        btn_scan.textResource = R.string.start_scan
        btn_scan.onClick {
            if (btn_scan.text == getString(R.string.start_scan)) {
                startScan()
            } else {
                BleManager.getInstance().cancelScan()
            }
        }
    }

    override fun loadData() {
        BleManager.getInstance().init(application)
        BleManager.getInstance().enableLog(BuildConfig.DEBUG).setReConnectCount(1, 5000)
            .setConnectOverTime(20000).setOperateTimeout(5000).initScanRule(
                BleScanRuleConfig.Builder().setServiceUuids(null).setDeviceName(true, null)
                    .setDeviceMac(null).setAutoConnect(false).setScanTimeOut(10000).build()
            )
    }

    override fun onResume() {
        super.onResume()
        showConnectedDevice()
    }

    override fun onDestroy() {
        super.onDestroy()
        BleManager.getInstance().disconnectAllDevice()
        BleManager.getInstance().destroy()
    }

    private fun showConnectedDevice() {
        deviceAdapter.clearConnectedDevice()
        BleManager.getInstance().allConnectedDevice.forEach {
            deviceAdapter.addDevice(it)
        }
        deviceAdapter.notifyDataSetChanged()
    }

    private fun startScan() {
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                deviceAdapter.clearScanDevice()
                deviceAdapter.notifyDataSetChanged()
                img_loading.startAnimation(operatingAnim)
                img_loading.isVisible = true
                btn_scan.textResource = R.string.stop_scan
            }

            override fun onScanning(bleDevice: BleDevice?) {
                deviceAdapter.addDevice(bleDevice)
                deviceAdapter.notifyDataSetChanged()
            }

            override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                img_loading.clearAnimation()
                img_loading.isInvisible = true
                btn_scan.textResource = R.string.start_scan
            }
        })
    }

    private fun connect(bleDevice: BleDevice?) {
        BleManager.getInstance().connect(bleDevice, object : BleGattCallback() {
            override fun onStartConnect() {
            }

            override fun onConnectFail(bleDevice: BleDevice?, exception: BleException?) {
                img_loading.clearAnimation()
                img_loading.isInvisible = true
                btn_scan.textResource = R.string.start_scan
                progressDialog.dismiss()
                showShortToast(R.string.connect_fail)
            }

            override fun onConnectSuccess(
                bleDevice: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int
            ) {
                progressDialog.dismiss()
                deviceAdapter.addDevice(bleDevice)
                deviceAdapter.notifyDataSetChanged()
            }

            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                device: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int
            ) {
                progressDialog.dismiss()
                deviceAdapter.removeDevice(bleDevice)
                deviceAdapter.notifyDataSetChanged()
                if (isActiveDisConnected) {
                    showShortToast(R.string.active_disconnected)
                } else {
                    showShortToast(R.string.disconnected)
                    bleDevice?.apply { ObserverManager.getInstance().notifyObserver(this) }
                }
            }
        })
    }
}