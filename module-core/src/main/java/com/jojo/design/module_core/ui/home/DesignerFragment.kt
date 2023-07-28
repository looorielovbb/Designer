package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_base.utils.glide.GlideUtils
import com.jojo.design.common_base.utils.StatusBarHelper
import com.jojo.design.common_ui.view.MultipleStatusView
import com.jojo.design.module_core.R
import com.jojo.design.module_core.bean.DesignerEntity
import com.jojo.design.module_core.bean.TagCategoryEntity
import com.jojo.design.module_core.mvp.contract.DesignerContract
import com.jojo.design.module_core.mvp.model.DesignerModel
import com.jojo.design.module_core.mvp.presenter.DesignerPresenter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import org.greenrobot.eventbus.EventBus

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/7 11:34 AM
 *    desc   : 设计师
 */
class DesignerFragment : BaseFragment<DesignerPresenter, DesignerModel>(), DesignerContract.View {
    private var mTitle: String? = null
    override fun getContentViewLayoutId(): Int = R.layout.fra_designer

    companion object {
        fun getInstance(title: String): DesignerFragment {
            val fragment = DesignerFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
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
//        DaggerCoreComponent.builder().applicationComponent(BaseApplication.mApplicationComponent).build().inject(this)
    }

    override fun startFragmentEvents() {
        mPresenter?.getRecommendDesigner()
        mPresenter?.getDesignerTypeList()
        initListener()
    }

    private fun initListener() {
        dropScrollView.setOnScrollViewListener { scrollX, scrollY, oldx, oldY ->
            var lp = fade_status_bar.layoutParams
            lp.height = StatusBarHelper.getStatusBarHeight(mContext)
            fade_status_bar.layoutParams = lp

            var distanceScrollY = (rl_head.height + tab.height) - (rl_sus_tab.height + fade_status_bar.height)
            if (scrollY >= distanceScrollY) {
                StatusBarHelper.setStatusTextColor(true, requireActivity())
                tab.visibility = View.INVISIBLE
                sus_tab.visibility = View.VISIBLE
            } else {
                StatusBarHelper.setStatusTextColor(false, requireActivity())
                tab.visibility = View.VISIBLE
                sus_tab.visibility = View.INVISIBLE
            }
        }
    }

    override fun getDesinerList(dataList: List<DesignerEntity>) {
    }

    /**
     * 获取设计师类型
     */
    override fun getDesignerTypeList(dataList: List<TagCategoryEntity>) {
        Log.e("TAG", "getDesignerTypeList")
        createFragmentByTags(dataList)
        EventBus.getDefault().post(dataList)
    }
    /**
     * 根据标签数量动态创建Fragment
     */
    private fun createFragmentByTags(dataList: List<TagCategoryEntity>) {
        val pages = FragmentPagerItems(mContext)
//        for (i in 0..dataList.size-1){
//            pages.add(FragmentPagerItem.of(dataList[i].name, FRA_DesignerTypeList::class.java!!))
//        }
        (0 until dataList.size).mapTo(pages) { FragmentPagerItem.of(dataList[it].name,
            FRA_DesignerTypeList::class.java
        ) }
        val adapter = FragmentPagerItemAdapter(activity?.supportFragmentManager,
                pages)
        viewpager.adapter = adapter
        tablayout.setViewPager(viewpager)
        susTablayout.setViewPager(viewpager)
    }

    /**
     * 获取顶部推荐设计师
     */
    override fun getRecommendDesigner(topDesigner: DesignerEntity) {
        tv_user_nike.text = topDesigner.userNick
        tv_op_tag.text = topDesigner.opTag
        tv_shop_name.text = topDesigner.shopName

        if (topDesigner.tags.size == 1) {
            tv_tags.text = topDesigner.tags[0].name + "   "
        } else if (topDesigner.tags.size == 2) {
            tv_tags.text = topDesigner.tags[0].name + "   " + topDesigner.tags[1].name
        }
        GlideUtils.loadImage(topDesigner.banner, iv_head_cover, 0)

    }

    /**
     * 重新设置viewPager高度
     *
     * @param position
     */
    fun resetViewPagerHeight(position: Int) {
        var child = viewpager.getChildAt(position)
        if (child != null) {
            var params = viewpager.layoutParams
            params.height = viewpager.map[position]!!
            viewpager.layoutParams = params
        }
    }
}