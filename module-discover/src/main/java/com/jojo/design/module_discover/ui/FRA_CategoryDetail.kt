package com.jojo.design.module_discover.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_base.utils.RecyclerviewHelper
import com.jojo.design.common_ui.lrecyclerview.recyclerview.LRecyclerView
import com.jojo.design.module_discover.mvp.contract.CategoryContract
import com.jojo.design.module_core.mvp.model.CategoryModel
import com.jojo.design.module_core.mvp.presenter.CategoryPresenter
import com.jojo.design.module_discover.R
import com.jojo.design.module_discover.adapter.ADA_Category
import com.jojo.design.module_discover.adapter.ADA_CategoryDetail
import com.jojo.design.module_discover.bean.CategoryBean
import com.jojo.design.module_discover.bean.ItemEntity
import com.jojo.design.module_discover.bean.TabEntity

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/23 5:55 PM
 *    desc   : 分类详情四个Tab页面
 */
class FRA_CategoryDetail : BaseFragment<CategoryPresenter, CategoryModel>(), CategoryContract.View {
    var mAdapter: ADA_CategoryDetail? = null
    lateinit var rv: LRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var type = arguments?.getInt("type")
        //懒加载：第一次进来时请求数据，后续切换tab不再请求
        mPresenter?.getCategorieDetail((activity as ACT_CategoryDetail).categoryId, type!!)
        Log.d("tab", "type=" + type)
//        initTestData(type)
        mAdapter = ADA_CategoryDetail(requireActivity())
        rv = view.findViewById<LRecyclerView>(R.id.rv)
        rv.setPullRefreshEnabled(false)
        RecyclerviewHelper.initLayoutManagerRecyclerView(
            rv,
            mAdapter!!,
            LinearLayoutManager(mContext),
            mContext
        )

//        //请求分类详情
//        mPresenter?.getCategorieDetail((activity as ACT_CategoryDetail).categoryId, type!!)
    }

    /**
     * 模拟数据
     */
    private fun initTestData(type: Int?) {
        rv.layoutManager = LinearLayoutManager(mContext)
        val adapter = ADA_Category(mContext)
        rv.adapter = adapter


        var dataList = ArrayList<CategoryBean>()
        for (i in 0..40) {
            var bean = CategoryBean(
                i.toString(),
                type.toString(),
                "",
                "http://img.kaiyanapp.com/7c46ad04ff913b87915615c78d226a40.jpeg?imageMogr2/quality/60/format/jpg",
                ""
            )
            dataList.add(bean)
        }
        adapter.update(dataList, true)
    }

    override fun getCategoryTabs(dataBean: TabEntity) {
    }

    override fun getCategories(dataList: List<CategoryBean>) {
    }

    override fun getCategoryDetail(dataBean: ItemEntity) {
        mAdapter?.update(dataBean?.itemList, true)
//        for (i in 0 until dataBean.itemList.size) {
//            if (!dataBean?.itemList[i]?.type.equals("video")) {
//                Log.e("TAG", "datatype=" + dataBean?.itemList[i].data.dataType)
//            } else {
//                Log.e("TAG", "title=" + dataBean?.itemList[i].data.title)
//            }
//        }
    }
}