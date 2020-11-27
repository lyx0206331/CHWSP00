package com.chwishay.chwsp00.baseComponent

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.chwishay.chwsp00.R

/**
 * date:2018/6/20
 * author：RanQing
 * description：
 */
abstract class BaseDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : Dialog(context, themeResId), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

//    @JvmOverloads
//    constructor(context: Context, themeResId: Int = R.style.CustomDialog) : super(context, themeResId)

    override fun onSaveInstanceState(): Bundle {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        return super.onSaveInstanceState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        setContentView(getLayoutResId())
        initViews()
    }

    override fun onStop() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onStop()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    abstract fun getLayoutResId(): Int
    abstract fun initViews()
}