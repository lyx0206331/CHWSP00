package com.chwishay.commonlib.tools

import com.chwishay.commonlib.tools.CmdUtil.cmdVerify
import org.junit.Assert.assertArrayEquals
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
 * date:2021/1/12 0012 15:19
 * description:
 */
class CmdUtilTest {

    @Test
    fun verify() {
        assertEquals(byteArrayOf(0xA5.toByte(), 0x01, 0x01.toByte()).cmdVerify(), 0x59)
    }

    @Test
    fun getStartSyncCmd() {
        assertArrayEquals(
            CmdUtil.getStartSyncCmd(),
            byteArrayOf(0xFA.toByte(), 0xFF.toByte(), 0xA3.toByte(), 0x01, 0x5A.toByte(), 0x02)
        )
    }

    @Test
    fun getStopSyncCmd() {
        assertArrayEquals(
            CmdUtil.getStopSyncCmd(),
            byteArrayOf(
                0xFA.toByte(),
                0xFF.toByte(),
                0xA4.toByte(),
                0x01,
                0xAA.toByte(),
                0xB1.toByte()
            )
        )
    }

    @Test
    fun getStateCmd() {
        assertArrayEquals(
            CmdUtil.getStateCmd(),
            byteArrayOf(0xFA.toByte(), 0xFF.toByte(), 0xA5.toByte(), 0x01, 0x01, 0x59)
        )
    }

    @Test
    fun getTimeSyncCmd() {
        assertArrayEquals(
            CmdUtil.getTimeSyncCmd(1610444032479),
            byteArrayOf(0xFA.toByte(), 0xFF.toByte(), 0xA6.toByte(), 0x54, 0x59, 0x18, 0x74, 0x21)
        )
    }
}