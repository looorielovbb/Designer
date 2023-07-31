package com.jojo.design.module_core.ui.designer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.adapter.rv.MultiItemTypeAdapter
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.config.arouter.ARouterConstants
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.module_core.bean.DesignerEntity
import com.jojo.design.module_core.bean.TagCategoryEntity
import com.jojo.design.module_core.mvp.contract.DesignerContract
import com.jojo.design.module_core.mvp.model.DesignerModel
import com.jojo.design.module_core.mvp.presenter.DesignerPresenter
import com.jojo.design.common_base.utils.RecyclerviewHelper
import com.jojo.design.common_ui.lrecyclerview.recyclerview.LRecyclerView
import com.jojo.design.module_core.R
import com.jojo.design.module_core.adapter.ADA_DesignerList

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/24 3:21 PM
 *    desc   : 设计师列表
 */
@Route(path = ARouterConfig.ACT_DESIGNER_LIST)
class ACT_DesignerList : BaseActivity<DesignerPresenter, DesignerModel>(), DesignerContract.View {
    var mAdapter: ADA_DesignerList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_designer_list)
        val lrecyclerview = findViewById<LRecyclerView>(R.id.lrecyclerview)
        val extras = intent.extras
        setHeaderTitle(extras?.getString(ARouterConstants.TAG_NAME)!!)
        mPresenter?.getDesinerList(
            extras?.getString(ARouterConstants.TAG_CATEGORY_ID)!!,
            extras?.getString(ARouterConstants.TAG_ID)!!
        )
        mAdapter = ADA_DesignerList(mContext)
        RecyclerviewHelper.initRecyclerView(lrecyclerview, mAdapter!!, mContext)
        initListener()
    }

    fun startEvents() {

    }

    private fun initListener() {
        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val realPos = position - 1
                val bean = mAdapter!!.dataList[realPos]
                ARouter.getInstance().build(ARouterConfig.ACT_WEB_VIEW)
                    .withString(
                        ARouterConstants.WEB_URL,
                        "http://www.xiangqu.com/designer2/index?id=" + bean.id
                    )
                    .withString(ARouterConstants.WEB_TITLE, bean.userNick)
                    .navigation()
            }

            override fun onItemLongClick(
                view: View?,
                holder: RecyclerView.ViewHolder?,
                position: Int
            ): Boolean {
                return false
            }
        })
    }

    override fun getDesignerTypeList(dataList: List<TagCategoryEntity>) {
    }

    override fun getRecommendDesigner(topDesigner: DesignerEntity) {
    }

    override fun getDesinerList(dataList: List<DesignerEntity>) {
        Log.e("TAG", "getDesinerList=" + dataList.size)
        mAdapter?.update(dataList, true)
    }
}