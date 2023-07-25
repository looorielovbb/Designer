package com.jojo.design.common_base.dagger.mvp.databinding

import android.support.annotation.Nullable
import androidx.databinding.ViewDataBinding
import com.jojo.design.common_base.BaseApplication
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseLazyFragment
import javax.inject.Inject

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 3:51 PM
 *    desc   : Dagger2-MVP-BaseFragment（支持DataBinding）
 */
abstract class BaseDBFragment<P : BaseContract.BasePresenter, M : BaseContract.BaseModel, DB : ViewDataBinding> : BaseLazyFragment() {
    @Nullable
    @Inject
    @JvmField
    var mPresenter: P? = null
    @Nullable
    @Inject
    @JvmField
    var mModel: M? = null
    protected var viewBinding: DB? = null


    override fun startEvents() {
        viewBinding = viewDataBinding as DB
        initDaggerInject(BaseApplication.mApplicationComponent)
        mPresenter?.attachViewModel(this, mModel!!)
        startFragmentEvents()
    }

    abstract fun startFragmentEvents()
}