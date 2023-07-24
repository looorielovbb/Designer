package com.will.weiyuekotlin.module

import android.content.Context
import com.jojo.design.common_base.BaseApplication

import dagger.Module
import dagger.Provides

/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
@Module
class ApplicationModule(private val mContext: Context) {

    @Provides
    internal fun provideApplication(): BaseApplication {
        return mContext.applicationContext as BaseApplication
    }

    @Provides
    internal fun provideContext(): Context {
        return mContext
    }
}
