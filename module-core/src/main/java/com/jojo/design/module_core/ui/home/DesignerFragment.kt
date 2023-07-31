package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_base.utils.StatusBarHelper
import com.jojo.design.common_base.utils.glide.GlideUtils
import com.jojo.design.common_ui.view.CustomViewPager
import com.jojo.design.common_ui.view.DropZoomScrollView
import com.jojo.design.module_core.R
import com.jojo.design.module_core.bean.DesignerEntity
import com.jojo.design.module_core.bean.TagCategoryEntity
import com.jojo.design.module_core.mvp.contract.DesignerContract
import com.jojo.design.module_core.mvp.model.DesignerModel
import com.jojo.design.module_core.mvp.presenter.DesignerPresenter
import com.ogaclejapan.smarttablayout.SmartTabLayout
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_designer, container, false)
    }

    companion object {
        fun getInstance(title: String): DesignerFragment {
            val fragment = DesignerFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    lateinit var tablayout: SmartTabLayout
    lateinit var susTablayout: SmartTabLayout
    lateinit var viewpager: CustomViewPager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.getRecommendDesigner()
        mPresenter?.getDesignerTypeList()
        tv_user_nike = view.findViewById(R.id.tv_user_nike)
        tv_op_tag = view.findViewById(R.id.tv_op_tag)
        tv_shop_name = view.findViewById(R.id.tv_shop_name)
        tv_tags = view.findViewById(R.id.tv_tags)
        iv_head_cover = view.findViewById(R.id.iv_head_cover)
        val dropScrollView = view.findViewById<DropZoomScrollView>(R.id.dropScrollView)
        val fade_status_bar = view.findViewById<View>(R.id.fade_status_bar)
        val tab = view.findViewById<View>(R.id.tab)
        val rl_head = view.findViewById<View>(R.id.rl_head)
        val rl_sus_tab = view.findViewById<View>(R.id.rl_sus_tab)
        val sus_tab = view.findViewById<View>(R.id.sus_tab)
        tablayout = view.findViewById(R.id.tablayout)
        susTablayout = view.findViewById(R.id.susTablayout)
        viewpager = view.findViewById(R.id.viewpager)
        dropScrollView.setOnScrollViewListener { scrollX, scrollY, oldx, oldY ->
            val lp = fade_status_bar.layoutParams
            lp.height = StatusBarHelper.getStatusBarHeight(mContext)
            fade_status_bar.layoutParams = lp
            val distanceScrollY =
                (rl_head.height + tab.height) - (rl_sus_tab.height + fade_status_bar.height)
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
        (dataList.indices).mapTo(pages) {
            FragmentPagerItem.of(
                dataList[it].name, FRA_DesignerTypeList::class.java
            )
        }
        val adapter = FragmentPagerItemAdapter(
            activity?.supportFragmentManager, pages
        )
        viewpager.adapter = adapter
        tablayout.setViewPager(viewpager)
        susTablayout.setViewPager(viewpager)
    }

    lateinit var tv_user_nike: TextView
    lateinit var tv_op_tag: TextView
    lateinit var tv_shop_name: TextView
    lateinit var tv_tags: TextView
    lateinit var iv_head_cover: ImageView


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
        val child = viewpager.getChildAt(position)
        if (child != null) {
            val params = viewpager.layoutParams
            params.height = viewpager.map[position]!!
            viewpager.layoutParams = params
        }
    }
}