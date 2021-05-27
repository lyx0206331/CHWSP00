package com.chwishay.commonlib.tools

import androidx.annotation.IntDef
import io.netty.buffer.Unpooled
import java.text.SimpleDateFormat
import java.util.*

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
 * date:2021/1/11 0011 11:46
 * description:
 */
object CmdUtil {

    const val TYPE_IMU = 0xA0
    const val TYPE_POWER = 0xA1
    const val TYPE_RECHARGE_STATE = 0xA2
    const val TYPE_SYNC_START = 0xA3
    const val TYPE_SYNC_STOP = 0xA4
    const val TYPE_CONN_STATE = 0xA5
    const val TYPE_SYNC_TIME = 0xA6
    const val TYPE_FIRMWARE_VERSION = 0xA7

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        TYPE_IMU,
        TYPE_POWER,
        TYPE_RECHARGE_STATE,
        TYPE_SYNC_START,
        TYPE_SYNC_STOP,
        TYPE_CONN_STATE,
        TYPE_SYNC_TIME,
        TYPE_FIRMWARE_VERSION
    )
    annotation class CmdType

    private val cmdByteBuf = Unpooled.buffer()

    /**
     * 包头数据
     */
    var headBytes: ByteArray = byteArrayOf(0xFA.toByte(), 0xFF.toByte())

    private fun createCmdData(cmdData: ByteArray): ByteArray {
        cmdByteBuf.clear()
        cmdByteBuf.capacity(headBytes?.size.orDefault() + cmdData.size + 1)
        cmdByteBuf.writeBytes(headBytes)
        cmdByteBuf.writeBytes(cmdData)
        cmdByteBuf.writeByte(cmdData.cmdVerify())
        return cmdByteBuf.array()
    }

    /**
     * 指令校验
     */
    fun ByteArray.cmdVerify() = this.sum().inv() + 1

    /**
     * 判断是IMU数据。由于IMU数据一帧包含三组有效数据，每一组都包含包头，类型，长度，数据及校验，所以单独做判断
     */
    fun ByteArray.isIMUData() =
        this[0] == headBytes[0] && this[1] == headBytes[1] && this[2] == TYPE_IMU.toByte()

    /**
     * 获取开始同步指令
     */
    fun getStartSyncCmd() =
        createCmdData(byteArrayOf(TYPE_SYNC_START.toByte(), 0x01, 0x5A.toByte()))

    /**
     * 获取停止同步指令
     */
    fun getStopSyncCmd() = createCmdData(byteArrayOf(TYPE_SYNC_STOP.toByte(), 0x01, 0xAA.toByte()))

    /**
     * 获取连接状态指令
     */
    fun getStateCmd() = createCmdData(byteArrayOf(TYPE_RECHARGE_STATE.toByte(), 0x01, 0x01))

    /**
     * 获取时间同步指令
     */
    fun getTimeSyncCmd(millis: Long = System.currentTimeMillis()): ByteArray =
        if (millis < 0) {
            throw IllegalArgumentException("输入参数不合法")
        } else {
            SimpleDateFormat("yy:MM:dd:HH:mm:ss", Locale.CHINESE).format(Date(millis)).split(":")
                .let {
                    val time = it[0].toInt().shl(26)
                        .or(it[1].toInt().shl(22))
                        .or(it[2].toInt().shl(17))
                        .or(it[3].toInt().shl(12))
                        .or(it[4].toInt().shl(6))
                        .or(it[5].toInt()).toBytesBE()
                    createCmdData(byteArrayOf(TYPE_SYNC_TIME.toByte(), *time))
                }
        }

    /**
     * 数据解析
     */
    fun parseCmdData(data: ByteArray): DataEntity? =
        if (data.size >= 6) {
            if (data[0] == headBytes[0] && data[1] == headBytes[1] && data.sliceArray(
                    IntRange(
                        2,
                        data.size - 2
                    )
                ).cmdVerify().toByte() == data[data.size - 1]
            ) {
                DataEntity(
                    data[2].toInt(),
                    data[3].toInt(),
                    data.sliceArray(IntRange(4, data.size - 2))
                )
            } else {
                null
            }
        } else {
            null
        }

    fun ByteArray.isValid(): Boolean =
        size == 31 && this[0] == headBytes[0] && this[1] == headBytes[1] && this.sliceArray(
            IntRange(
                2,
                size - 2
            )
        ).cmdVerify().toByte() == this[size - 1]

    fun ByteArray.parseImuData(): List<IMUEntity>? = if (this.size != 93) {
        "IMUValues".logE("数据不完整: ${this.contentToString()}")
        null
    } else {
        val list = arrayListOf<IMUEntity>()
        for (i in 0..2) {
            val start = i * 31
            val frameBytes = this.sliceArray(IntRange(start, start + 30))
//            if (frameBytes.isValid()) {
                val imuEntity = IMUEntity(
                    byteArrayOf(frameBytes[5], frameBytes[6]).read2UShortBE(),
                    byteArrayOf(frameBytes[7], frameBytes[8]).read2UShortBE(),
                    byteArrayOf(frameBytes[9], frameBytes[10]).read2UShortBE(),
                    byteArrayOf(frameBytes[11], frameBytes[12]).read2UShortBE(),
                    byteArrayOf(frameBytes[13], frameBytes[14]).read2UShortBE(),
                    byteArrayOf(frameBytes[15], frameBytes[16]).read2UShortBE(),
                    byteArrayOf(frameBytes[18], frameBytes[19]).read2UShortBE(),
                    byteArrayOf(frameBytes[20], frameBytes[21]).read2UShortBE(),
                    byteArrayOf(frameBytes[22], frameBytes[23]).read2UShortBE(),
                    byteArrayOf(frameBytes[24], frameBytes[25]).read2UShortBE(),
                    byteArrayOf(frameBytes[26], frameBytes[27]).read2UShortBE(),
                    byteArrayOf(frameBytes[28], frameBytes[29]).read2UShortBE()
                )
                "IMU_DATA".logE("IMU:$imuEntity")
                list.add(imuEntity)
//            }
        }
        list
    }
}

/**
 * @param type 报文类型。0xA0:IMU数据; 0xA1:电量; 0xA2:充电状态;0xA5:应答蓝牙状态
 * @param length 报文数据长度
 * @param content 报文内容
 */
data class DataEntity(@CmdUtil.CmdType val type: Int, val length: Int, val content: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataEntity

        if (type != other.type) return false
        if (length != other.length) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + length
        result = 31 * result + content.contentHashCode()
        return result
    }
}

data class IMUEntity(
    val accX1: UShort, val accY1: UShort, val accZ1: UShort,
    val gyrX1: UShort, val gyrY1: UShort, val gyrZ1: UShort,
    val accX2: UShort, val accY2: UShort, val accZ2: UShort,
    val gyrX2: UShort, val gyrY2: UShort, val gyrZ2: UShort
)