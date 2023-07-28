package com.jojo.design.common_base.dagger.mvp.databinding

import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseLazyFragment
import javax.inject.Inject

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 3:51 PM
 *    desc   : Dagger2-MVP-BaseFragment（支持DataBinding）
 */
open class BaseDBFragment<P : BaseContract.BasePresenter, M : BaseContract.BaseModel> :
    BaseLazyFragment<P, M>() {

}