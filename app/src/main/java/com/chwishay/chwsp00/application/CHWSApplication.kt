package com.chwishay.chwsp00.application

import com.chwishay.chwsp00.BuildConfig
import com.chwshay.commonlib.baseApp.BaseApp
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

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
 * Create time:20-8-5 上午10:30
 * Description:
 **/
class CHWSApplication: BaseApp() {

    override fun onCreate() {
        super.onCreate()
        UMConfigure.init(this, "5f2a2898b4b08b653e914640", "UMENG_CHANNEL", UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        if (BuildConfig.DEBUG) {
            UMConfigure.setLogEnabled(true)
        }
    }
}