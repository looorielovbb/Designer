package com.jojo.design.common_base.dagger.mvp

import androidx.databinding.ViewDataBinding

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 3:51 PM
 *    desc   : Dagger2-MVP-BaseFragment
 */
open class BaseFragment<P : BaseContract.BasePresenter, M : BaseContract.BaseModel> : BaseLazyFragment<P,M>() {

}