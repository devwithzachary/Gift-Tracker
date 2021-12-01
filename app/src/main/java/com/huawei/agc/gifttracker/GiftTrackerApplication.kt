package com.huawei.agc.gifttracker

import android.app.Application
import com.huawei.agc.gifttracker.data.AppContainer
import com.huawei.agc.gifttracker.data.AppContainerImpl

class GiftTrackerApplication : Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}