package com.jojo.design.common_base.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Subscriber

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/1/8 17:24 PM
 *    desc   : RxJava2Helper（Room）
 */
object RxJava2Helper {
    @JvmStatic
    fun <T : Any> getFlowable(method: () -> T): Flowable<T> {
        return object : Flowable<T>() {
            override fun subscribeActual(s: Subscriber<in T>) {
                try {
                    s.onNext(method.invoke())
                } catch (e: Exception) {
                    e.printStackTrace()
                    s.onError(e)
                }
                s.onComplete()
            }
        }.compose(io2main<T>())
    }

    @JvmStatic
    fun <T : Any> io2main(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}