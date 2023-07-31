package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.view.View
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_core.adapter.ADA_TopicPager
import com.jojo.design.module_core.bean.TopicBean
import com.jojo.design.module_core.bean.TopicDetailEntity
import com.jojo.design.module_core.databinding.FraTopicBinding
import com.jojo.design.module_core.mvp.contract.TopicContract
import com.jojo.design.module_core.mvp.model.TopicModel
import com.jojo.design.module_core.mvp.presenter.TopicPresenter
import com.jojo.design.module_core.widgets.cardview.ShadowTransformer

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/7 11:34 AM
 *    desc   : 专题
 */
class TopicFragment : BaseFragment<TopicPresenter, TopicModel>(), TopicContract.View {
    private var mTitle: String? = null
    private val mDatas = ArrayList<TopicBean>()
    private var mCardAdapter: ADA_TopicPager? = null
    private var mCardShadowTransformer: ShadowTransformer? = null
    private var binding: FraTopicBinding? = null

    companion object {
        fun getInstance(title: String): TopicFragment {
            val fragment = TopicFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FraTopicBinding.bind(view)
        startFragmentEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun startFragmentEvents() {
        mPresenter?.getTopics("131")
        mPresenter?.getTopicDetail("5192")
        mCardAdapter = ADA_TopicPager(mContext, mDatas)
        mCardShadowTransformer = ShadowTransformer(binding?.vpCard!!, mCardAdapter!!)
        mCardShadowTransformer?.enableScaling(true)
        binding?.vpCard?.adapter = mCardAdapter
        binding?.vpCard?.setPageTransformer(false, mCardShadowTransformer)
        binding?.vpCard?.offscreenPageLimit = 3
    }

    override fun getTopics(dataList: List<TopicBean>) {
        mCardAdapter?.notifyChanged(dataList)
        //需重新设置，卡片缩放效果才能起作用
        mCardShadowTransformer?.enableScaling(true)
    }

    override fun getTopicDetail(dataBean: TopicDetailEntity) {
    }
}