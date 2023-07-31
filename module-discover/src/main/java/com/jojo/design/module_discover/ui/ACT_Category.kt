package com.jojo.design.module_discover.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.adapter.rv.MultiItemTypeAdapter
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.config.arouter.ARouterConstants
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.StatusBarHelper
import com.jojo.design.module_discover.mvp.contract.CategoryContract
import com.jojo.design.module_core.mvp.model.CategoryModel
import com.jojo.design.module_core.mvp.presenter.CategoryPresenter
import com.jojo.design.module_discover.R
import com.jojo.design.module_discover.adapter.ADA_Category
import com.jojo.design.module_discover.bean.CategoryBean
import com.jojo.design.module_discover.bean.ItemEntity
import com.jojo.design.module_discover.bean.TabEntity
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 * 开眼视频分类页面
 */
@Route(path = ARouterConfig.ACT_CATEGORY)
class ACT_Category : BaseActivity<CategoryPresenter, CategoryModel>(), CategoryContract.View {
    var mAdapter: ADA_Category? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_category)
        startEvents()
    }

    fun startEvents() {
        StatusBarHelper.setStatusTextColor(true, this)
        setHeaderTitle("全部分类")
        mPresenter?.getCategories()

        mAdapter = ADA_Category(mContext)
        val rv_category = findViewById<RecyclerView>(R.id.rv_category)
        rv_category.layoutManager = GridLayoutManager(mContext, 2)
        rv_category.adapter = mAdapter

        // //设置item之间的间距
        rv_category.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val itemPosition = parent.getChildAdapterPosition(view)
                if (itemPosition > 1) outRect.top = 5
                if ((itemPosition + 1) % 2 == 0) outRect.left = 5
            }
        })
        OverScrollDecoratorHelper.setUpOverScroll(
            rv_category,
            OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )
        initListener()
    }

    private fun initListener() {
        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val bean = mAdapter!!.dataList[position]
                ARouter.getInstance().build(ARouterConfig.ACT_CATEGORY_DETAIL)
                    .withString(ARouterConstants.CATEGORY_HEAD_IMAGE, bean.headerImage)
                    .withString(ARouterConstants.CATEGORY_ID, bean.id)
                    .withString(ARouterConstants.CATEGORY_NAME, bean.name)
                    .withSerializable(ARouterConstants.CATEGORY_BEAN, bean)
                    .navigation()
            }

            override fun onItemLongClick(
                view: View?,
                holder: RecyclerView.ViewHolder?,
                position: Int
            ): Boolean {
                return false;
            }
        })
    }

    override fun getCategoryTabs(dataBean: TabEntity) {
    }

    override fun getCategories(dataList: List<CategoryBean>) {
        Log.e("TAG", "dataList=" + dataList.size)
        mAdapter?.update(dataList, true)
    }

    override fun getCategoryDetail(dataBean: ItemEntity) {
    }

}
