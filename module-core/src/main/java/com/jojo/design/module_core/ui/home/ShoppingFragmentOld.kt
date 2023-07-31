package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_ui.lrecyclerview.recyclerview.LRecyclerView
import com.jojo.design.common_ui.lrecyclerview.recyclerview.LRecyclerViewAdapter
import com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle
import com.jojo.design.common_ui.view.MultipleStatusView
import com.jojo.design.module_core.R
import com.jojo.design.module_core.widgets.RefreshView
import com.jojo.design.module_core.adapter.ADA_ShoppingContent
import com.jojo.design.module_core.adapter.ADA_TestFragment
import com.jojo.design.module_core.bean.*
import com.jojo.design.module_core.databinding.FraShoppingBinding
import com.jojo.design.module_core.mvp.contract.ShoppingContract
import com.jojo.design.module_core.mvp.model.ShoppingModel
import com.jojo.design.module_core.mvp.presenter.ShoppingPresenter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/7 11:34 AM
 *    desc   : 逛（shoping）-废弃
 */
class ShoppingFragmentOld : BaseFragment<ShoppingPresenter, ShoppingModel>(), ShoppingContract.View {
    private var mTitle: String? = null
    private lateinit var lrecyclerview:LRecyclerView
    private var binding:FraShoppingBinding? = null
    lateinit var mAdapter: ADA_ShoppingContent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_shopping,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFragmentEvents()
    }



     private fun startFragmentEvents() {
//        var mAdapter = ADA_TestFragment(mContext)
        lrecyclerview = requireView().findViewById(R.id.lrecyclerview)
        mAdapter = ADA_ShoppingContent(requireActivity())
        val mLRecyclerViewAdapter = LRecyclerViewAdapter(mAdapter)
        lrecyclerview.setRefreshHeader(RefreshView(mContext))
        val headerView = LayoutInflater.from(mContext).inflate(R.layout.shoping_head_view, null, false)
        //头部添加Recyclerview
        val recyclerview = headerView.findViewById<RecyclerView>(R.id.recyclerview)
//        mLRecyclerViewAdapter.addHeaderView(headerView)
        //设置外层列表Adapter
        lrecyclerview.adapter = mLRecyclerViewAdapter
        lrecyclerview.setHasFixedSize(true)
        lrecyclerview.layoutManager = LinearLayoutManager(mContext)
        lrecyclerview.setRefreshProgressStyle(ProgressStyle.SysProgress)
        lrecyclerview.setLoadingMoreProgressStyle(ProgressStyle.SysProgress)
        //设置头部文字颜色
        lrecyclerview.setHeaderViewColor(R.color.color_app_yellow, R.color.color_app_yellow, R.color.color_ffffff)
        //设置底部加载颜色-loading动画颜色,文字颜色,footer的背景颜色
        lrecyclerview.setFooterViewColor(R.color.color_app_yellow, R.color.color_app_yellow, R.color.color_ffffff)
        //设置底部加载文字提示
        lrecyclerview.setFooterViewHint(mContext.resources.getString(R.string.list_footer_loading), mContext.resources.getString(R.string.list_footer_end), mContext.resources.getString(R.string.list_footer_network_error))
        //Headview为RecyclerView时的处理
        val data = ArrayList<String>()
        (0..50).mapTo(data) { "Item=" + it }
//        mAdapter.update(data, true)
        val mAdapter2 = ADA_TestFragment(mContext)
        recyclerview.adapter = mAdapter2
        recyclerview.layoutManager = LinearLayoutManager(mContext)
        mAdapter2.update(data.subList(0, 5), true)

        //获取商品分类
        mPresenter?.getCategoryList()
        initListener()
    }

    private fun initListener() {
        lrecyclerview.setOnRefreshListener {
            Handler().postDelayed({
                lrecyclerview.refreshComplete(1)
            }, 1000)
        }
        lrecyclerview.setOnLoadMoreListener {
            Handler().postDelayed({
                lrecyclerview.setNoMore(true)
            }, 2000)
        }
    }

    private var mCategoryList: ArrayList<CategoryEntity> = arrayListOf()
    override fun getCategoryList(dataList: List<CategoryEntity>) {
        mCategoryList.addAll(dataList)
        //获取商品列表
        mPresenter?.getGoodsList()
    }

    override fun getGoodsList(dataList: List<GoodsEntity>) {
        val mData = ArrayList<ContentBean>()
        val categoryBean = ContentBean(1, mCategoryList, dataList)
        val goodsBean = ContentBean(2, mCategoryList, dataList)
//        var viewPagerBean = ContentBean(3, mCategoryList, dataList)
        mData.add(categoryBean)
        mData.add(goodsBean)
//        mData.add(viewPagerBean)
        mAdapter.update(mData, true)

    }

    override fun getHandPickedGoods(bean: RecordsEntity) {
    }

    override fun getPersonLike(dataList: List<AllfaverEntity>) {
    }
}