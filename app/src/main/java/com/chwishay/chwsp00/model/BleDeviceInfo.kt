package com.chwishay.chwsp00.model

import android.os.Environment
import android.util.Log
import com.chwishay.commonlib.tools.formatDateString
import com.chwishay.commonlib.tools.orDefault
import com.chwishay.commonlib.tools.read2FloatBE
import com.clj.fastble.data.BleDevice
import com.clj.fastble.utils.HexUtil
import java.io.*
import java.util.*
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
 * date:2020/9/9 0009 17:55
 * description:
 */
class BleDeviceInfo(val bleDevice: BleDevice) {

    var serviceUUID: UUID? = null
    var notifyUUID: UUID? = null

    var lastCalTime = 0L
    var speed: Int = 0
    var totalSize: Int = 0

    //上一秒总大小
    var lastSecondTotalSize = 0

    //最后一次包大小
    var lastDataSize = 0
        set(value) {
            field = value
            val timeLen = System.currentTimeMillis() - lastCalTime
            if (lastCalTime == 0L) {
                lastCalTime = System.currentTimeMillis()
                lastSecondTotalSize = 0
                speed = 0
            } else if (timeLen >= 1000) {
                lastCalTime = System.currentTimeMillis()
                speed = ((totalSize - lastSecondTotalSize) / (timeLen / 1000f)).toInt()
                lastSecondTotalSize = totalSize
            }
            totalSize += field
        }

    var syncTime = 0L
    var sysTime = 0L

    //最后一次传输数据
    var lastData: ByteArray? = null
        set(value) {
            field = value
            lastDataSize = field?.size.orDefault()
//            if (lastDataSize == 7 && field!![0] == 0xFB.toByte() && field!![1] == 0xFF.toByte()) {
//                field!!.copyOfRange(2, 6).apply {
//                    sysTime = this.read2LongLE()
//                    syncTime = sysTime
//                }
//            }
            if (needSave && field != null) {
                thread {
                    field?.also { data ->
//                        if (data.size == 93 && data.sliceArray(IntRange(0, 30)).isValid() && data.sliceArray(
//                                IntRange(31, 61)
//                            ).isValid() && data.sliceArray(IntRange(62, 92)).isValid()
//                        ) {
                        writeStr2File(fileName, data, true)
////                            writeStr2File(fileName, field, false)
//                        }
                    }
                }
            }
        }

    //是否需要保存记录
    var needSave = false
        set(value) {
            field = value
            if (field) {
                startSaveTime = System.currentTimeMillis()
                totalReceiveTime = 0L
            } else {
                stopSaveTime = System.currentTimeMillis()
            }
        }
    private var startSaveTime = 0L
    private var stopSaveTime = 0L
    var fileName: String = "${System.currentTimeMillis().formatDateString("yyyy-MM-dd-HH_mm_ss")}"
        set(value) {
            field = "${value}_${startSaveTime.formatDateString("yyyy-MM-dd-HH_mm_ss")}"
        }
    var filePath: String? = null

    //数据总接收时长，数据保存记录状态下，只累计接收数据时段时长，但每次重新保存记录会清零(ms)
    var totalReceiveTime = 0L

    //开始接收数据时间(ms)
    private var startReceiveTime = 0L

    //停止接收数据时间(ms)
    private var stopReceiveTime = 0L

    /**
     * 开始接收数据
     */
    fun startReceive() {
        startReceiveTime = System.currentTimeMillis()
    }

    /**
     * 停止接收数据
     */
    fun stopReceive() {
        stopReceiveTime = System.currentTimeMillis()
        if (startReceiveTime <= 0L) {
            throw IllegalStateException("请先调用startReceive()方法开始接收数据")
        } else {
            totalReceiveTime += stopReceiveTime - startReceiveTime
        }
    }

    /**
     * 保存原始文件
     */
    private fun writeSrc2File(fileName: String, data: ByteArray?) {
        if (data == null) return
        thread {
            val file = checkFileExists(fileName)
            FileOutputStream(file, true).apply {
                write(data)
                close()
            }
        }

    }

    /**
     * 保存十六进制字符串文件
     */
    private fun writeStr2File(fileName: String, data: ByteArray?, isSrc: Boolean = true) {
        if (data == null/* || (data[0] != 0xfa.toByte() && data[1] != 0xff.toByte())*/) return
        val content = if (isSrc) "${HexUtil.formatHexString(data, true)}\n" else {
            if (data.size >= 31) {
                val frameCount = data.size / 31
                val sb = StringBuilder()
                for (i in 0 until frameCount) {
                    val result = FloatArray(18) { 0f }
                    val baseOffset = i * 31
                    result[2] = data.read2FloatBE(baseOffset + 7)
                    result[3] = data.read2FloatBE(baseOffset + 11)
                    result[4] = data.read2FloatBE(baseOffset + 15)
                    result[8] = data.read2FloatBE(baseOffset + 22)
                    result[9] = data.read2FloatBE(baseOffset + 26)
                    result[10] = data.read2FloatBE(baseOffset + 30)
                    val str = "${result.contentToString().replace(",", "")}"
                    sb.append("${str.subSequence(1, str.lastIndex)}\n")
                }
                "$sb"
            } else {
                "${HexUtil.formatHexString(data, true)}\n"
            }
        }
//            "DATA".logE("$content")
        val file = checkFileExists(if (isSrc) "${fileName}_src" else "${fileName}_rst")
        if (!isSrc) filePath = file.absolutePath
        BufferedWriter(OutputStreamWriter(FileOutputStream(file, true))).use {
            it.write(content)
        }
    }

    /**
     * 检测目标文件是否存在，不存在则自动创建
     */
    private fun checkFileExists(fileName: String): File {
        val dir = File("${Environment.getExternalStorageDirectory().absolutePath}/chws/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "$fileName.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    fun readFromFile(filePath: String): FloatArray {
        val file = File(filePath)
        if (!file.exists()) {
            throw FileNotFoundException("文件不存在")
        }

        val sf = StringBuilder()
        file.inputStream().use {
            val isr = InputStreamReader(it)
            val br = BufferedReader(isr)
            var line: String? = br.readLine()
            while (line != null) {
                sf.append(line)
                line = br.readLine()
            }
            isr.close()
            br.close()
        }
        Log.e("SRC_DATA", sf.toString())
        val list = sf.split(" ", "\n")
        val result = FloatArray(list.size)
        list?.forEachIndexed { index, s ->
            result[index] = s.toFloat()
        }
        return result
    }
}