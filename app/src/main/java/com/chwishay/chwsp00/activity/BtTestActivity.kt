package com.chwishay.chwsp00.activity

import android.content.Context
import android.content.Intent
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity

class BtTestActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BtTestActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bt_test
    }

    override fun initVariables() {
    }

    override fun initViews() {
    }

    override fun loadData() {
    }
}