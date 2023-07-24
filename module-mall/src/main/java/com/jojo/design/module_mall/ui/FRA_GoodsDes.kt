package com.jojo.design.module_mall.ui

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_ui.view.MultipleStatusView
import com.jojo.design.module_mall.R
import com.jojo.design.module_mall.adapter.ADA_GoodsDes
import com.jojo.design.module_mall.bean.GoodsDesBean
import com.will.weiyuekotlin.component.ApplicationComponent

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/15 4:03 PM
 *    desc   : 商品详情-商品描述
 */
class FRA_GoodsDes : BaseFragment<BaseContract.BasePresenter, BaseContract.BaseModel>() {
    var mAdapter: ADA_GoodsDes? = null
    override fun getContentViewLayoutId(): Int = R.layout.fra_goods_des
    override fun isBindEventBus(isBind: Boolean): Boolean {
        return super.isBindEventBus(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveDataList(dataList: List<GoodsDesBean>) {
        Log.e("TAG", "onReceiveDataList=" + dataList.size)
        mAdapter?.update(dataList, true)
    }

    override fun onFirstUserVisible() {
    }

    override fun onFirstUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun getLoadingMultipleStatusView(): MultipleStatusView? = null

    override fun initDaggerInject(mApplicationComponent: ApplicationComponent) {
    }

    override fun startFragmentEvents() {
        mAdapter = ADA_GoodsDes(mContext)
        lv_content.layoutManager = LinearLayoutManager(mContext)
        lv_content.adapter = mAdapter

    }
}