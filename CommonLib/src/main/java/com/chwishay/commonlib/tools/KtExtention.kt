package com.chwishay.commonlib.tools

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.chwishay.commonlib.BuildConfig
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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
 * Create time:20-8-4 下午1:46
 * Description:Kotlin常用扩展函数
 **/

object DateFormatStr {
    const val FORMAT_YMDHMS_CN = "yyyy年MM月dd日 HH时mm分ss秒"
    const val FORMAT_YMDHMS = "yyyy/MM/dd HH:mm:ss"
    const val FORMAT_YMD_CN = "yyyy年MM月dd日"
    const val FORMAT_YMD = "yyyy/MM/dd"
    const val FORMAT_HMS_CN = "HH时mm分ss秒"
    const val FORMAT_HMS = "HH:mm:ss"
    const val FORMAT_YM_CN = "yyyy年MM月"
    const val FORMAT_YM = "yyyy/MM"
    const val FORMAT_MD_CN = "MM月dd日"
    const val FORMAT_MD = "MM/dd"
    const val FORMAT_HM_CN = "HH时mm分"
    const val FORMAT_HM = "HH:mm"
    const val FORMAT_MS_CN = "mm分ss秒"
    const val FORMAT_MS = "mm:ss"

    const val FORMAT_HMS_SSS = "HH:mm:ss.SSS"
}

fun Short?.orDefault(default: Short = 0): Short = this ?: default

fun Int?.orDefault(default: Int = 0): Int = this ?: default

fun Long?.orDefault(default: Long = 0L): Long = this ?: default

fun Float?.orDefault(default: Float = .0f): Float = this ?: default

fun Double?.orDefault(default: Double = .0): Double = this ?: default

fun Boolean?.orDefault(default: Boolean = false): Boolean = this ?: default

fun String?.orDefault(default: String = ""): String = this ?: default

fun String.logE(msg: String, tr: Throwable? = null) =
    if (BuildConfig.DEBUG) Log.e(this, msg, tr) else 0

fun String.logV(msg: String, tr: Throwable? = null) =
    if (BuildConfig.DEBUG) Log.v(this, msg, tr) else 0

fun String.logD(msg: String, tr: Throwable? = null) =
    if (BuildConfig.DEBUG) Log.d(this, msg, tr) else 0

fun String.logW(msg: String, tr: Throwable? = null) =
    if (BuildConfig.DEBUG) Log.w(this, msg, tr) else 0

fun String.logI(msg: String, tr: Throwable? = null) =
    if (BuildConfig.DEBUG) Log.i(this, msg, tr) else 0

fun Context.showShortToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.showLongToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

fun Context.showShortToast(@StringRes resId: Int) =
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.showLongToast(@StringRes resId: Int) =
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()

/**
 * 格式化日期字符串
 */
fun Long.formatDateString(
    formatStr: String = DateFormatStr.FORMAT_YMD_CN,
    locale: Locale = Locale.CHINESE
): String = SimpleDateFormat(formatStr, locale).format(Date(this))

fun Date.formatDateString(
    formatStr: String = DateFormatStr.FORMAT_YMD_CN,
    locale: Locale = Locale.CHINESE
): String = SimpleDateFormat(formatStr, locale).format(this)

/**
 * 字节数组转换为浮点型(大端模式)
 */
fun ByteArray.read2FloatBE(offset: Int) =
    if (this == null || this.size < offset + 4) throw IllegalArgumentException("传入参数不正确")
    else java.lang.Float.intBitsToFloat(
        0xff000000.and(this[offset].toInt().shl(24).toLong())
            .or(0x00ff0000.and(this[offset + 1].toInt().shl(16)).toLong())
            .or(0x0000ff00.and(this[offset + 2].toInt().shl(8)).toLong())
            .or(0x000000ff.and(this[offset + 3].toInt()).toLong()).toInt()
    )

/**
 * 字节数组转换为浮点型(小端模式)
 */
fun ByteArray.read2FloatLE(offset: Int) =
    if (this == null || this.size < offset + 4) throw IllegalArgumentException("传入参数不正确")
    else java.lang.Float.intBitsToFloat(
        0xff000000.and(this[offset + 3].toInt().shl(24).toLong())
            .or(0x00ff0000.and(this[offset + 2].toInt().shl(16)).toLong())
            .or(0x0000ff00.and(this[offset + 1].toInt().shl(8)).toLong())
            .or(0x000000ff.and(this[offset].toInt()).toLong()).toInt()
    )

/**
 * 字节数组转换为字符串
 * @param seperator 间隔符
 */
fun ByteArray?.formatHexString(seperator: String = ""): String? =
    if (this == null || this.isEmpty()) {
        null
    } else {
        val sb = StringBuilder()
        this.forEach { item ->
            var hex = Integer.toHexString(item.toInt().and(0xff))
            if (hex.length == 1) hex = "0$this"
            sb.append("$hex$seperator")
        }
        sb.toString().trim()
    }

/**
 * 十六进制字符串转字节数组
 */
fun String?.hexString2Bytes(): ByteArray? =
    if (this.isNullOrEmpty() || !this.isHexString()) null
    else {
        val src = this.trim().toUpperCase()
        val len = src.length / 2
        val result = ByteArray(len) { 0 }
        val hexChars = src.toCharArray()
        for (i in 0 until len) {
            val pos = i * 2
            result[i] = (hexChars[pos].char2Byte().toInt().shl(4)
                .or(hexChars[pos + 1].char2Byte().toInt())).toByte()
        }
        result
    }

/**
 * 字符转字节
 */
fun Char.char2Byte() = "0123456789ABCDEF".indexOf(this).toByte()

/**
 * 判断是否十六进制字符串
 */
fun String?.isHexString() =
    if (this.isNullOrEmpty()) false
    else {
        Pattern.compile("^[0-9A-Fa-f]+$").matcher(this).matches()
    }

/**
 * 判断是否邮箱地址
 */
fun String?.isEmailString() =
    if (this.isNullOrEmpty()) false
    else {
        Pattern.compile("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]\$")
            .matcher(this).matches()
    }

/**
 * 判断是否Wi-Fi SSID
 */
fun String?.isSsidString() =
    if (this.isNullOrEmpty()) false
    else {
        Pattern.compile("^[A-Za-z]+[\\w\\-\\:\\.]*\$").matcher(this).matches()
    }