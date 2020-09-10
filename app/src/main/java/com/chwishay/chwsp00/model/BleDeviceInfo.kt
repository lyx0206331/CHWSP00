package com.chwishay.chwsp00.model

import android.os.Environment
import com.chwishay.commonlib.tools.orDefault
import com.clj.fastble.data.BleDevice
import com.clj.fastble.utils.HexUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
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
    var startTime = 0L
    var speed: Int = 0
    var totalSize: Int = 0

    //上一秒总大小
    var lastSecondTotalSize = 0

    //最后一次包大小
    var lastDataSize = 0
        set(value) {
            field = value
            val timeLen = System.currentTimeMillis() - startTime
            if (startTime == 0L) {
                startTime = System.currentTimeMillis()
                lastSecondTotalSize = 0
                speed = 0
            } else if (timeLen >= 1000) {
                startTime = System.currentTimeMillis()
                speed = ((totalSize - lastSecondTotalSize) / (timeLen / 1000f)).toInt()
                lastSecondTotalSize = totalSize
            }
            totalSize += field
        }
    var lastData: ByteArray? = null
        set(value) {
            field = value
            lastDataSize = field?.size.orDefault()
            if (needSave && !fileName.isNullOrEmpty()) {
                writeStr2File(fileName!!, field)
            }
        }
    var needSave = false
    var fileName: String? = null

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

    private fun writeStr2File(fileName: String, data: ByteArray?) {
        if (data == null) return
        thread {
            val file = checkFileExists(fileName)
            BufferedWriter(OutputStreamWriter(FileOutputStream(file, true))).apply {
                write("${HexUtil.formatHexString(data, true)} ")
                close()
            }
        }
    }

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
}