package com.chwishay.chwsp00.views

import android.content.Context
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseDialog
import kotlinx.android.synthetic.main.dialog_loading.*

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
 * Create time:20-8-3 下午4:00
 * Description:
 **/
class LoadingDialog(context: Context, title: String? = null, private val content: String? = "加载中..."): BaseDialog(context) {
    override fun getLayoutResId(): Int {
        return R.layout.dialog_loading
    }

    override fun initViews() {
        setCanceledOnTouchOutside(false)
        tvContent.text = content
    }
}