package com.jojo.design.common_base

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.component.ApplicationComponent
import kotlin.properties.Delegates

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/11/29 3:25 PM
 *    desc   :
 */
open class BaseApplication : Application() {
    ////用companion object包裹，实现java中static的效果,包裹的方法或者变量都是static的
    companion object {
        lateinit var application: Application
        var mApplicationComponent: ApplicationComponent by Delegates.notNull()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initARouter()
        initDagger()
    }

    private fun initDagger() {
        /*mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .httpModule(HttpModule())
                .build()*/
        Log.e("TAG", "mApplicationComponent=$mApplicationComponent")
    }

    /**
     * 初始化路由
     */
    private fun initARouter() {
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }

}