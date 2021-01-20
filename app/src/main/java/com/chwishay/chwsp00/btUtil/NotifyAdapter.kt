package com.chwishay.chwsp00.btUtil

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.activity.ReportActivity
import com.chwishay.chwsp00.model.BleDeviceInfo
import com.chwishay.commonlib.tools.*
import com.chwishay.commonlib.tools.CmdUtil.isIMUData
import com.chwishay.commonlib.tools.CmdUtil.parseImuData
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.exception.BleException
import com.clj.fastble.utils.HexUtil
import com.example.alglibrary.AlgUtil
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import org.jetbrains.anko.find
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File
import kotlin.concurrent.thread

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
 * date:2020/9/8 0008 16:23
 * description:
 */
class NotifyAdapter(val context: Context, val callback: (BleDeviceInfo) -> Unit) :
    RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder>() {

    class NotifyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDevName = itemView.findViewById<TextView>(R.id.tvDeviceName)
        val tvSysTime = itemView.find<TextView>(R.id.tvSysTime)
        val tvReceiveTimeLen = itemView.find<TextView>(R.id.tvReceiveTimeLen)
        val tvReceiveSpeed = itemView.find<TextView>(R.id.tvReceiveSpeed)
        val tvTotalData = itemView.find<TextView>(R.id.tvTotalData)
        val switchNotify = itemView.find<Switch>(R.id.switchNotify)
        val etFileName = itemView.find<EditText>(R.id.etFileName)
        val btnSave = itemView.find<Button>(R.id.btnSave)
        val btnBuildReport = itemView.find<Button>(R.id.btnBuildReport)
        val btnClear = itemView.find<Button>(R.id.btnClear)
        val tvData = itemView.find<TextView>(R.id.tvData)
            .apply { movementMethod = ScrollingMovementMethod.getInstance() }
        val tvPower = itemView.find<TextView>(R.id.tvPower)
        val tvRechargeState = itemView.find<TextView>(R.id.tvRechargeState)
        val chart = itemView.find<LineChart>(R.id.chart).also {
            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {}

                override fun onNothingSelected() {}
            })
            it.description.isEnabled = true
            it.setTouchEnabled(true)
            it.isDragEnabled = true
            it.setScaleEnabled(true)
            it.setDrawGridBackground(false)
            it.setPinchZoom(true)
            it.setBackgroundColor(Color.LTGRAY)
            it.data = LineData().apply { setValueTextColor(Color.WHITE) }
            it.legend.apply {
                form = Legend.LegendForm.LINE
                textColor = Color.WHITE
            }
            it.xAxis.apply {
                textColor = Color.WHITE
                setDrawGridLines(false)
                setAvoidFirstLastClipping(false)
                isEnabled = true
            }
            it.isAutoScaleMinMaxEnabled = true
//            it.axisLeft.apply {
//                textColor = Color.WHITE
//                axisMaximum = 40f
//                axisMinimum = -40f
//                setDrawGridLines(true)
//            }
            it.axisRight.isEnabled = false
            it.description.isEnabled = false
        }
    }

    fun LineChart.addEntry(imuData: ByteArray) = this.data?.let { d ->
        fun getAccValue(acc: UShort) = (acc.toInt() - 32768) * 9.81f / 2048
        fun getGyrValue(gyr: UShort) = (gyr.toInt() - 32768) * 3.14f / 16.4f / 180
        fun getDataSet(index: Int, @ColorInt color: Int, name: String) =
            d.getDataSetByIndex(index) ?: LineDataSet(null, name).also { lds ->
                lds.axisDependency = YAxis.AxisDependency.LEFT
                lds.color = color
                lds.setCircleColor(Color.WHITE)
                lds.lineWidth = 1f
                lds.circleRadius = 2f
                lds.fillAlpha = 65
                lds.fillColor = color
                lds.highLightColor = Color.rgb(244, 177, 177)
                lds.valueTextColor = Color.WHITE
                lds.valueTextSize = 9f
                lds.setDrawCircles(false)
                d.addDataSet(lds)
            }
        imuData.parseImuData()!![0].let { entity ->
            val accX1Set = getDataSet(0, ColorTemplate.getHoloBlue(), "accX1")
            val gyrX1Set = getDataSet(1, context.getColor1(R.color.green01FD01), "gyrX1")
            val accY1Set = getDataSet(2, context.getColor1(R.color.yellowFFFF00), "accY1")
            val gyrY1Set = getDataSet(3, context.getColor1(R.color.purple7E2E8D), "gyrY1")
            val accZ1Set = getDataSet(4, context.getColor1(R.color.colorPrimary), "accZ1")
            val gyrZ1Set = getDataSet(5, context.getColor1(R.color.redFE0000), "gyrZ1")
            val accX1 = getAccValue(entity.accX1)
            val gyrX1 = getGyrValue(entity.gyrX1)
            val accY1 = getAccValue(entity.accY1)
            val gyrY1 = getGyrValue(entity.gyrY1)
            val accZ1 = getAccValue(entity.accZ1)
            val gyrZ1 = getGyrValue(entity.gyrZ1)
            "IMU_Value".logE("accX1:$accX1, gyrX1:$gyrX1")
            d.addEntry(Entry(accX1Set.entryCount.toFloat(), accX1), 0)
            d.addEntry(Entry(gyrX1Set.entryCount.toFloat(), gyrX1), 1)
            d.addEntry(Entry(accY1Set.entryCount.toFloat(), accY1), 2)
            d.addEntry(Entry(gyrY1Set.entryCount.toFloat(), gyrY1), 3)
            d.addEntry(Entry(accZ1Set.entryCount.toFloat(), accZ1), 4)
            d.addEntry(Entry(gyrZ1Set.entryCount.toFloat(), gyrZ1), 5)
            d.notifyDataChanged()

            this.notifyDataSetChanged()

            this.setVisibleXRangeMaximum(1000f)

            this.moveViewToX(d.entryCount.toFloat())

        }
    }

    var devices: ArrayList<BleDeviceInfo>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        return NotifyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bt_notify, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        val bleDeviceInfo = devices?.get(position)
        bleDeviceInfo?.bleDevice?.apply {
            holder.tvDevName.text = "$name($mac)"
            BleManager.getInstance().getBluetoothGatt(this)?.services?.let {
                it.forEachIndexed { index, bluetoothGattService ->
                    if (index == it.size - 1) {
                        bluetoothGattService.characteristics.forEach { gattCharacteristic ->
                            if (gattCharacteristic.properties.and(BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                holder.switchNotify.onCheckedChange { buttonView, isChecked ->
                                    holder.etFileName.isEnabled = !isChecked
                                    if (isChecked) {
                                        bleDeviceInfo.startReceive()
                                        BleManager.getInstance().notify(
                                            this@apply,
                                            gattCharacteristic.service.uuid.toString(),
                                            gattCharacteristic.uuid.toString(),
                                            object : BleNotifyCallback() {
                                                override fun onNotifySuccess() {
                                                    appendData(holder.tvData, "notify success")
                                                    callback.invoke(bleDeviceInfo)
                                                }

                                                override fun onNotifyFailure(exception: BleException?) {
                                                    appendData(holder.tvData, exception.toString())
                                                }

                                                override fun onCharacteristicChanged(data: ByteArray?) {
                                                    data?.let { d ->
                                                        if (!d.isIMUData()) {
                                                            CmdUtil.parseCmdData(d)
                                                                ?.also { dataEntity ->
                                                                    when (dataEntity.type.toByte()) {
                                                                        CmdUtil.TYPE_POWER.toByte() -> {
                                                                            holder.tvPower.text =
                                                                                "电量:${dataEntity.content[0]}%"
                                                                        }
//                                                                        CmdUtil.TYPE_IMU.toByte() -> {
//                                                    val data = gattCharacteristic.value
//                                                                    bleDeviceInfo.lastData = d
//                                                                    holder.tvSysTime.text =
//                                                                        "系统时间:${bleDeviceInfo.sysTime}ms"
//                                                                    holder.tvReceiveSpeed.text =
//                                                                        "${bleDeviceInfo.speed}byte/s"
//                                                                    holder.tvTotalData.text =
//                                                                        "总接收数据:${bleDeviceInfo.totalSize}byte"
//                                                                        }
                                                                        CmdUtil.TYPE_CONN_STATE.toByte() -> {
                                                                        }
                                                                        else -> {
                                                                        }
                                                                    }

                                                                }
                                                        } else {
                                                            bleDeviceInfo.lastData = d
                                                            holder.tvSysTime.text =
                                                                "系统时间:${bleDeviceInfo.sysTime}ms"
                                                            holder.tvReceiveSpeed.text =
                                                                "${bleDeviceInfo.speed}byte/s"
                                                            holder.tvTotalData.text =
                                                                "总接收数据:${bleDeviceInfo.totalSize}byte"
                                                            holder.chart.addEntry(d)
                                                        }
                                                        appendData(
                                                            holder.tvData,
                                                            HexUtil.formatHexString(data, true)
                                                        )
                                                    }
                                                }
                                            })
                                    } else {
                                        bleDeviceInfo.stopReceive()
                                        holder.tvReceiveTimeLen.text =
                                            "总接收时长:${bleDeviceInfo.totalReceiveTime}ms"
                                        BleManager.getInstance().stopNotify(
                                            this@apply,
                                            gattCharacteristic.service.uuid.toString(),
                                            gattCharacteristic.uuid.toString()
                                        )
                                    }
                                }
                                holder.btnSave.onClick {
//                                    if (!bleDeviceInfo.needSave && holder.switchNotify.isChecked) {
//                                        context.showShortToast("请先关闭接收通知")
//                                        return@onClick
//                                    }
                                    val fileName = holder.etFileName.text
                                    if (fileName.isNullOrEmpty() || fileName.trim()
                                            .isNullOrEmpty()
                                    ) {
                                        holder.btnSave.context.showShortToast("请输入文件名")
                                    } else {
                                        bleDeviceInfo.needSave = !bleDeviceInfo.needSave
                                        if (bleDeviceInfo.needSave) {
                                            bleDeviceInfo.fileName = fileName.toString()
                                            context.showShortToast("开始保存数据")
                                            holder.btnSave.text = "停止保存"
                                        } else {
                                            context.showShortToast("停止保存数据")
                                            holder.btnSave.text = "开始保存"
                                            holder.tvReceiveTimeLen.text =
                                                "总接收时长:${bleDeviceInfo.totalReceiveTime}ms"
                                        }
                                    }
                                }
                                holder.btnBuildReport.onClick {
                                    if (bleDeviceInfo.needSave || !File(bleDeviceInfo.filePath).exists()) {
                                        context.showShortToast("请先保存数据")
                                        return@onClick
                                    } else {
                                        thread {
                                            val algUtil = AlgUtil()
                                            val data = algUtil.readFromTxt(bleDeviceInfo.filePath!!)
                                            val result = algUtil.getTestData(data, 18)
                                            context.runOnUiThread {
                                                ReportActivity.startActivity(
                                                    context,
                                                    bleDeviceInfo.totalReceiveTime,
                                                    result.distance,
                                                    result.stepArray,
                                                    result.stepCount
                                                )
                                            }
                                        }
                                    }
                                }
                                holder.btnClear.onClick {
                                    bleDeviceInfo.totalSize = 0
                                    holder.tvData.text = ""
                                    holder.tvTotalData.text = "总接收数据:${bleDeviceInfo.totalSize}byte"
                                    holder.tvReceiveSpeed.text = "0byte/s"
                                }

                                holder.switchNotify.isChecked = false
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 追加数据并滚动到最后
     */
    private fun appendData(tv: TextView, data: String) {
        context.runOnUiThread {
            tv.apply {
                if (text.length >= 4000) {
                    text = ""
                }
                append(data)
                append("\n")
                val offset = lineCount * lineHeight
                if (offset > height) {
                    scrollTo(0, offset - height)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return devices?.size.orDefault()
    }
}