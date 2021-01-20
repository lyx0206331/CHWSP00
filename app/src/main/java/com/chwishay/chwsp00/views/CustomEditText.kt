package com.chwishay.chwsp00.views

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

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
 * date:2020/12/24 0024 17:16
 * description:
 */
class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var inputContent = ""

    private var selectionPos = 0

    var maxLength = 30

    /**
     * 是否只包含字母数字及汉字
     */
    private fun String.isNumOrCharOrChinese() = this.matches(Regex("^[a-z0-9A-Z\\u4e00-\\u9fa5]+$"))

    init {
        addTextChangedListener({ text, start, count, after ->
            text.toString().apply {
//                logE("before::text: $this, start: $start, count: $count, after: $after")
                inputContent = this
            }
        }, { text, start, count, after ->
//            text.toString().also {
//                it.logE("on:: text: $it, start: $start, count: $count, after: $after")
//            }
        }, { text ->
            text.toString().also {
//                it.logE("after:: text: $it, size: ${it.toByteArray().size}")
                if ((!it.isNullOrEmpty() && !it.isNumOrCharOrChinese()) || it.toByteArray().size > maxLength) {
                    this.text = Editable.Factory.getInstance().newEditable(inputContent)
                    selectionPos = inputContent.length - 1
                    setSelectionPos()
                }
            }
        })
    }

    private fun setSelectionPos() {
        if (selectionPos < 0) {
            return
        }
        try {
            this.setSelection(selectionPos)
        } catch (e: Exception) {
            e.printStackTrace()
            --selectionPos
            setSelectionPos()
        }
    }
}