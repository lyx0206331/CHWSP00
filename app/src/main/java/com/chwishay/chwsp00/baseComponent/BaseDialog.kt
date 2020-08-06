package com.chwishay.chwsp00.baseComponent

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.chwishay.chwsp00.R

/**
 * date:2018/6/20
 * author：RanQing
 * description：
 */
abstract class BaseDialog @JvmOverloads constructor(context: Context, themeResId: Int = R.style.CustomDialog) : Dialog(context, themeResId) {

//    @JvmOverloads
//    constructor(context: Context, themeResId: Int = R.style.CustomDialog) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResId())
        initViews()
    }

    abstract fun getLayoutResId(): Int
    abstract fun initViews()
}