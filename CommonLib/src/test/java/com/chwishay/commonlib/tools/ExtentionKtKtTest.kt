package com.chwishay.commonlib.tools

import org.junit.Assert.assertEquals
import org.junit.Test

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
 * date:2021/1/19 0019 16:17
 * description:
 */
class ExtentionKtKtTest {

    @Test
    fun formatDateString() {
    }

    @Test
    fun testFormatDateString() {
    }

    @Test
    fun read2FloatBE() {
    }

    @Test
    fun read2FloatLE() {
    }

    @Test
    fun toBytesLE() {
    }

    @Test
    fun toBytesBE() {
    }

    @Test
    fun testToBytesLE() {
    }

    @Test
    fun testToBytesBE() {
    }

    @Test
    fun read2ShortBE() {
        assertEquals(byteArrayOf(0x80.toByte(), 0x2a.toByte()).read2UShortBE(), 32810.toUShort())
    }

    @Test
    fun read2ShortLE() {
    }

    @Test
    fun read2IntBE() {
    }

    @Test
    fun read2IntLE() {
    }

    @Test
    fun read2LongBE() {
    }

    @Test
    fun read2LongLE() {
    }

    @Test
    fun formatHexString() {
    }

    @Test
    fun hexString2Bytes() {
    }

    @Test
    fun char2Byte() {
    }

    @Test
    fun str2BinStr() {
    }

    @Test
    fun isHexString() {
    }

    @Test
    fun isEmailString() {
    }

    @Test
    fun isSsidString() {
    }

    @Test
    fun getCHINESE_UNICODE() {
    }

    @Test
    fun containtChinese() {
    }

    @Test
    fun isAllChinese() {
    }

    @Test
    fun isNumOrCharOrChinese() {
    }

    @Test
    fun createFile() {
    }

    @Test
    fun getFilesByTimeRange() {
    }

    @Test
    fun hasAvailableSpace() {
    }
}