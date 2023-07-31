package com.jojo.design.module_core.ui.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_base.utils.ScreenUtil
import com.jojo.design.module_core.adapter.ADA_ShoppingContent
import com.jojo.design.module_core.bean.*
import com.jojo.design.module_core.databinding.FraShoppingNewBinding
import com.jojo.design.module_core.mvp.contract.ShoppingContract
import com.jojo.design.module_core.mvp.model.ShoppingModel
import com.jojo.design.module_core.mvp.presenter.ShoppingPresenter
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/7 11:34 AM
 *    desc   : 逛（shoping）-采用NestedScrollView嵌套TabLayout+ViewPager+Fragment+RecyclerView 复杂嵌套滑动冲突解决
 */
class ShoppingFragment : BaseFragment<ShoppingPresenter, ShoppingModel>(), ShoppingContract.View {

    private var mTitle: String? = null
    open lateinit var mHeaderAdapter: ADA_ShoppingContent
    private var mCategoryList: ArrayList<CategoryEntity> = arrayListOf()
    private var binding:FraShoppingNewBinding? = null

    companion object {
        fun getInstance(title: String): ShoppingFragment {
            var fragment = ShoppingFragment()
            var bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FraShoppingNewBinding.bind(view)
        startFragmentEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun startFragmentEvents() {
        //获取商品分类
        mPresenter?.getCategoryList()

        mHeaderAdapter = ADA_ShoppingContent(requireActivity())
        binding?.recyclerview?.layoutManager = object : LinearLayoutManager(mContext) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding?.recyclerview?.adapter = mHeaderAdapter


        binding?.llTitle?.root?.post{
            dealWithViewPager()
        }

//        createFragment(vp_shoping, tablayout)
        initListener()
    }

    var toolBarPositionY = 0
    private fun initListener() {
        binding!!.scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {

            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                var scrollY = scrollY
                val location = IntArray(2)
                binding!!.rlTablayout.getLocationOnScreen(location)
                val yPosition = location[1] //rl_tablayout顶部距离屏幕顶部的距离（Y方向的坐标）
                Log.e("scrollView", "滑动的TabLayout的位置：yPosition=" + yPosition + "固定顶部标题栏toolBarPositionY=" + toolBarPositionY)
                //直接通过yPosition < toolBarPositionY判断是否拦截子View，下滑时会顿一下，滑动距离有偏差
                if (yPosition < toolBarPositionY) {
                    binding!!.rlSusTab.visibility = View.VISIBLE
//                    scrollView.setNeedScroll(false) //NetsedScrollView不拦截子View，让子View消费事件，处理滑动
                } else {
                    binding!!.rlSusTab.visibility = View.GONE
//                    scrollView.setNeedScroll(true)
                }
                //解决（原因暂未明）：当上滑yPosition <toolBarPositionY时，设置scrollView.setNeedScroll(false)没有即时生效，NetsedScrollView还继续滑动了一段距离（从toolBarPositionY变到了73,子View才响应滑动事件）
                if (yPosition + (toolBarPositionY - 73) <= toolBarPositionY) {
                    binding!!.scrollView.setNeedScroll(false)
                } else {
                    binding!!.scrollView.setNeedScroll(true)
                }
            }
        })

        binding!!.llTitle.etSearch.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.ACT_SEARCH).navigation()
        }
    }

    private fun dealWithViewPager() {
        toolBarPositionY = binding!!.llTitle.root.height
        val params = binding!!.vpShoping.layoutParams
        params.height = ScreenUtil.getScreenHeight(activity as Activity) - toolBarPositionY - binding!!.rlTablayout.height
        binding!!.vpShoping.layoutParams = params
    }

    /**
     * 创建Fragment和ViewPager
     */
    private fun createFragment(viewpager: ViewPager, tablayout: SmartTabLayout) {
        var dataList = ArrayList<String>()
        dataList.add("精选")
        dataList.add("大家喜欢")
        val pages = FragmentPagerItems(activity)
        for (i in 0 until dataList.size) {
            if (i == 0) {
                pages.add(FragmentPagerItem.of(dataList[i], HandpickedFragment::class.java))
            } else {
                pages.add(FragmentPagerItem.of(dataList[i], AllFavorFragment::class.java))
            }

        }
        val adapter = FragmentPagerItemAdapter((activity as FragmentActivity)?.supportFragmentManager,
                pages)
        viewpager.adapter = adapter!!
        tablayout.setViewPager(viewpager)
        binding!!.susTablayout.setViewPager(viewpager)
    }


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
        mHeaderAdapter.update(mData, true)

        //创建精选+大家喜欢的Tab+ViewPager
        createFragment(binding!!.vpShoping, binding!!.tablayout)
    }

    override fun getHandPickedGoods(bean: RecordsEntity) {
    }

    override fun getPersonLike(dataList: List<AllfaverEntity>) {
    }

}