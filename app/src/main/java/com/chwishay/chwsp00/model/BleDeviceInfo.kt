package com.chwishay.chwsp00.model

import com.clj.fastble.data.BleDevice
import java.io.FileOutputStream
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
        get() = total - lastSecondTotal
    var total: Int = 0
    var lastSecondTotal = 0

    fun write2File(fileName: String, data: ByteArray) {
        thread {
            FileOutputStream(fileName, true).apply {
                write(data)
                close()
            }
        }

    }
}