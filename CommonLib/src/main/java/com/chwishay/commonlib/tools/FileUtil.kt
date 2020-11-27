package com.chwishay.commonlib.tools

import java.io.File

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
 * date:2020/8/19 0019 11:16
 * description:
 */
object FileUtil {

    fun getFile(fileName: String, delOld: Boolean = false): File {
        val file = File(fileName)
        if (delOld && file.isFile) {
            file.deleteOnExit()
        }
        if (!file.exists() || !file.isFile) {
            file.createNewFile()
        }
        return file
    }

    fun getDiretory(dirName: String): File {
        val file = File(dirName)
        if (!file.exists() || !file.isDirectory) {
            file.mkdirs()
        }
        return file
    }
}