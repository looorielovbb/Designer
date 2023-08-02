package com.jojo.design.module_mall.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_mall.adapter.ADA_GoodsDes
import com.jojo.design.module_mall.bean.GoodsDesBean
import com.jojo.design.module_mall.databinding.FraGoodsDesBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/15 4:03 PM
 *    desc   : 商品详情-商品描述
 */
class FRA_GoodsDes : BaseFragment<BaseContract.BasePresenter, BaseContract.BaseModel>() {
    private var mAdapter: ADA_GoodsDes? = null
    private var binding: FraGoodsDesBinding? = null
    override fun isBindEventBus(isBind: Boolean): Boolean {
        return super.isBindEventBus(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FraGoodsDesBinding.bind(view)
        startFragmentEvents()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveDataList(dataList: List<GoodsDesBean>) {
        Log.e("TAG", "onReceiveDataList=" + dataList.size)
        mAdapter?.update(dataList, true)
    }


    private fun startFragmentEvents() {
        mAdapter = ADA_GoodsDes(mContext)
        binding!!.lvContent.layoutManager = LinearLayoutManager(mContext)
        binding!!.lvContent.adapter = mAdapter

    }
}