package com.jojo.design.module_mall.ui

import android.os.Bundle
import android.view.View
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_mall.adapter.ADA_GoodsComment
import com.jojo.design.module_mall.adapter.ADA_GoodsRecommend
import com.jojo.design.module_mall.bean.CommentBean
import com.jojo.design.module_mall.bean.GoodsContentBean
import com.jojo.design.module_mall.bean.GoodsDesBean
import com.jojo.design.module_mall.bean.RevelentBean
import com.jojo.design.module_mall.databinding.FraGoodsCommentBinding
import com.jojo.design.module_mall.mvp.contract.GoodsContract
import com.jojo.design.module_mall.mvp.model.GoodsModel
import com.jojo.design.module_mall.mvp.presenter.GoodsPresenter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/15 4:03 PM
 *    desc   : 商品详情-评价
 */
class FRA_GoodsComment : BaseFragment<GoodsPresenter, GoodsModel>(), GoodsContract.View {
    var mAdapter: ADA_GoodsRecommend? = null
    var mAdapterComment: ADA_GoodsComment? = null
    private  var binding:FraGoodsCommentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FraGoodsCommentBinding.bind(view)
        startFragmentEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun startFragmentEvents() {
        mPresenter?.getGoodsCommentList((activity as ACT_GoodsDetail).productId!!, 0)
        mPresenter?.getRevelentGoodsList((activity as ACT_GoodsDetail).productId!!)

        mAdapter = ADA_GoodsRecommend(mContext)
        binding!!.gvRecomment.adapter = mAdapter

        mAdapterComment = ADA_GoodsComment(mContext)
        binding!!.lvComment.adapter = mAdapterComment
    }


    override fun getGoodsCommentList(dataList: List<CommentBean>) {
        binding!!.tvCommentNum.text = "所有" + dataList.size + "条评论"
        mAdapterComment?.update(dataList, true)
    }

    override fun getRevelentGoodsList(dataBean: RevelentBean) {
        mAdapter?.update(dataBean?.revelentList, true)
    }

    override fun getGoodsContent(dataBean: GoodsContentBean) {
    }

    override fun getGoodsDescription(dataList: List<GoodsDesBean>) {
    }

}