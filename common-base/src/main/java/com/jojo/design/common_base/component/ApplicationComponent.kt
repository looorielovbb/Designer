package com.jojo.design.common_base.component

import com.jojo.design.common_base.module.ApplicationModule
import com.will.weiyuekotlin.module.HttpModule

import dagger.Component

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(modules = [(ApplicationModule::class), (HttpModule::class)])
interface ApplicationComponent {
//
//    val application: BaseAppliction
//
//    val context: Context
//
//    fun getNetEaseApi(): NewsApi
//
//    fun getJanDanApi(): JanDanApi
}
