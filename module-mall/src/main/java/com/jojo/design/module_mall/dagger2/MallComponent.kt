package com.jojo.design.module_mall.dagger2

import com.jojo.design.module_mall.ui.ACT_GoodsDetail
import com.jojo.design.module_mall.ui.ACT_GoodsFilter
import com.jojo.design.module_mall.ui.ACT_Search
import com.jojo.design.module_mall.ui.FRA_GoodsComment
import com.jojo.design.common_base.component.ApplicationComponent
import dagger.Component

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 10:19 AM
 *    desc   :
 */
@Component(dependencies = [(ApplicationComponent::class)])
interface MallComponent {
    fun inject(activity: ACT_Search)
    fun inject(activity: ACT_GoodsFilter)
    fun inject(activity: ACT_GoodsDetail)
    fun inject(activity: FRA_GoodsComment)
}