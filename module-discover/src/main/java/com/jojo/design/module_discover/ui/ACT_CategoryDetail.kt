package com.jojo.design.module_discover.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.appbar.AppBarLayout
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.config.arouter.ARouterConstants
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.StatusBarHelper
import com.jojo.design.common_base.utils.glide.GlideUtils
import com.jojo.design.module_discover.mvp.contract.CategoryContract
import com.jojo.design.module_core.mvp.model.CategoryModel
import com.jojo.design.module_core.mvp.presenter.CategoryPresenter
import com.jojo.design.module_discover.R
import com.jojo.design.module_discover.bean.CategoryBean
import com.jojo.design.module_discover.bean.ItemEntity
import com.jojo.design.module_discover.bean.TabEntity
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlin.math.abs

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/23 4:58 PM
 *    desc   : 开眼视频分类详情页面（5.0新特性CoordinatorLayout +AppBarLayout效果）
 */
@Route(path = ARouterConfig.ACT_CATEGORY_DETAIL)
class ACT_CategoryDetail : BaseActivity<CategoryPresenter, CategoryModel>(), CategoryContract.View {
    var categoryId = ""
    var categoryBean: CategoryBean? = null

    private enum class AppBarState {
        EXPANDED,
        COLLAPSED,
        MIDDLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_category_detail)
        startEvents()
    }

    fun startEvents() {
        val iv_headImg = findViewById<ImageView>(R.id.iv_headImg)
        val tv_name = findViewById<TextView>(R.id.tv_name)
        val tv_des = findViewById<TextView>(R.id.tv_des)
        categoryId = intent.extras?.getString(ARouterConstants.CATEGORY_ID)!!
        categoryBean =
            intent.extras!!.getSerializable(ARouterConstants.CATEGORY_BEAN) as CategoryBean?
        GlideUtils.loadNormalImage(
            intent.extras!!.getString(ARouterConstants.CATEGORY_HEAD_IMAGE)!!,
            iv_headImg,
            0
        )
        tv_name.text = categoryBean?.name
        tv_des.letterSpacing = 10f
        tv_des.setText(categoryBean?.description!!, TextView.BufferType.SPANNABLE)
        mPresenter?.getCategoryTabs(categoryId)
        initToorbar()
//        createFragment(viewpager, tablayout)
        initListener()
    }

    private var mState: AppBarState? = null
    private fun initListener() {
        val tv_title = findViewById<TextView>(R.id.tv_title)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val appbar = findViewById<AppBarLayout>(R.id.appbar)
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (mState != AppBarState.EXPANDED) {
                    mState = AppBarState.EXPANDED//修改状态标记为展开
                    tv_title.visibility = View.GONE
                    toolbar.setNavigationIcon(R.drawable.ic_back_arrow_white)
//                    view_title_divider.visibility = View.GONE
                    StatusBarHelper.setStatusTextColor(false, this)
                }
            } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                if (mState != AppBarState.COLLAPSED) {
                    mState = AppBarState.COLLAPSED//修改状态标记为折叠
                    tv_title.visibility = View.VISIBLE
                    toolbar.setNavigationIcon(R.drawable.ic_back_arrow_black)
//                    view_title_divider.visibility = View.VISIBLE
                    StatusBarHelper.setStatusTextColor(true, this)
                }
            } else {
                if (mState != AppBarState.MIDDLE) {
                    if (mState == AppBarState.COLLAPSED) {
                        tv_title.visibility = View.GONE
                        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_white)
//                        view_title_divider.visibility = View.GONE
                        StatusBarHelper.setStatusTextColor(false, this)
                    }
                    mState = AppBarState.MIDDLE//修改状态标记为中间

                }
            }
        }
    }

    /**
     * 设置标题栏 返回icon和标题
     */
    private fun initToorbar() {
        val tv_title = findViewById<TextView>(R.id.tv_title)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val appbar = findViewById<AppBarLayout>(R.id.appbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_white)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        tv_title.text = intent.extras?.getString(ARouterConstants.CATEGORY_NAME)
    }

    var viewpager: ViewPager? = null
    var tablayout: SmartTabLayout? = null

    override fun getCategoryTabs(dataBean: TabEntity) {
        createFragment(dataBean.tabInfo.tabList, viewpager!!, tablayout!!)
    }

    override fun getCategories(dataList: List<CategoryBean>) {
    }

    override fun getCategoryDetail(dataBean: ItemEntity) {
    }

    /**
     * 创建Fragment,关联ViewPager和Fragment
     */
    private fun createFragment(
        tabList: List<TabEntity.TabInfoEntity.TabBean>,
        viewpager: ViewPager,
        smartTab: SmartTabLayout
    ) {

        val pages = FragmentPagerItems(this)
        (tabList.indices).mapTo(pages) {
            val arguments = Bundle()
            arguments.putInt("type", it)
            FragmentPagerItem.of(tabList[it].name, FRA_CategoryDetail::class.java, arguments)
        }
        //设置预加载Fragment的数量为tabList.size（首次进入时，viewpgaer的每个Fragment都会走startFragmentEvents，然后每次滑动切换都走onUserVisible）
        viewpager.offscreenPageLimit = tabList.size
        val adapter = FragmentPagerItemAdapter(supportFragmentManager, pages)
        viewpager.adapter = adapter
        smartTab.setViewPager(viewpager)
    }
}