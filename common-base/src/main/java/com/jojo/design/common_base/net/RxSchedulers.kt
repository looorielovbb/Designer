package com.jojo.design.common_base.net

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * 线程调度
 * Created by yangyan
 * on 2018/4/24.
 */

object RxSchedulers {
    fun <T:Any> io_main(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}
