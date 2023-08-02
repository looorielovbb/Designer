package com.jojo.design.module_mall.ui

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.convenientbanner.ConvenientBanner
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.config.arouter.ARouterConstants
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.StatusBarHelper
import com.jojo.design.common_base.utils.glide.GlideUtils
import com.jojo.design.common_ui.view.MultipleStatusView
import com.jojo.design.module_mall.R
import com.jojo.design.module_mall.bean.CommentBean
import com.jojo.design.module_mall.bean.GoodsContentBean
import com.jojo.design.module_mall.bean.GoodsDesBean
import com.jojo.design.module_mall.bean.RevelentBean
import com.jojo.design.module_mall.databinding.ActGoodsDetailBinding
import com.jojo.design.module_mall.helper.BannerHelper
import com.jojo.design.module_mall.mvp.contract.GoodsContract
import com.jojo.design.module_mall.mvp.model.GoodsModel
import com.jojo.design.module_mall.mvp.presenter.GoodsPresenter
import org.greenrobot.eventbus.EventBus

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/10 5:05 PM
 *    desc   : 商品详情页面
 */
@Route(path = ARouterConfig.ACT_GOODS_DETAIL)
class ACT_GoodsDetail : BaseActivity<GoodsPresenter, GoodsModel>(), GoodsContract.View {
    private var productId: String? = "3115460"
    private var currentFragment: Fragment? = null
    private var fragmentGoodsDes: FRA_GoodsDes? = null
    private var fragmentGoodsCom: FRA_GoodsComment? = null
    private lateinit var binding:ActGoodsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActGoodsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startEvents()
    }

    private fun startEvents() {
        StatusBarHelper.setStatusTextColor(true, this)
        productId = intent.extras?.getString(ARouterConstants.PRODUCT_ID)
        mPresenter?.getGoodsContent(productId!!)
        mPresenter?.getGoodsDescription(productId!!)
        mPresenter?.getGoodsCommentList(productId!!, 0)
//        mPresenter?.getRevelentGoodsList(productId!!)
        val currentTimeMillis = System.currentTimeMillis()
        Log.e("TAG", "currentTimeMillis=" + currentTimeMillis)


        initView()

        initFragments()

        initListener()
    }

    private fun initView() {

        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        binding.layoutDetailTop.banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
        binding.layoutDetailTop.banner.setPageIndicator(intArrayOf(R.drawable.bg_shape_circle_grey, R.drawable.bg_shape_circle_white))
        binding.layoutDetailTop.banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)

        binding.layoutTitleRoot.root.post {
            Log.e("TAG", "binding.layoutTitleRoot.root.height=" + binding.layoutTitleRoot.root.height)
            binding.layoutDetailBottom.susTitle.layoutParams.height = binding.layoutTitleRoot.root.height
        }

    }

    private fun initFragments() {
        binding.layoutDetailBottom.tvProductDes.isSelected = true
        fragmentGoodsDes = FRA_GoodsDes()
        fragmentGoodsCom = FRA_GoodsComment()
        if (!fragmentGoodsDes!!.isAdded) {
            currentFragment = fragmentGoodsDes
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fl_content, fragmentGoodsDes!!)
            transaction.commitAllowingStateLoss()
        }
    }

    /**
     * 选中评价
     */
    private fun checkGoodsCom() {
        binding.layoutDetailBottom.lineBottomTwo.visibility = View.VISIBLE
        binding.layoutDetailBottom.lineBottomOne.visibility = View.INVISIBLE
        binding.layoutDetailBottom.tvComment.isSelected = true
        binding.layoutDetailBottom.tvProductDes.isSelected = false
        switchContent(currentFragment!!, fragmentGoodsCom!!)
    }

    /**
     * 选中商品描述
     */
    private fun checkGoodsDes() {
        binding.layoutDetailBottom.lineBottomOne.visibility = View.VISIBLE
        binding.layoutDetailBottom.lineBottomTwo.visibility = View.INVISIBLE
        binding.layoutDetailBottom.tvProductDes.isSelected = true
        binding.layoutDetailBottom.tvComment.isSelected = false
        switchContent(currentFragment!!, fragmentGoodsDes!!)
    }

    /**
     * 切换Fragment
     *
     * @param from
     * @param to
     */
    private fun switchContent(from: Fragment, to: Fragment) {
        if (currentFragment !== to) {
            currentFragment = to
            val transaction = supportFragmentManager.beginTransaction()
            if (!to.isAdded) { // 先判断是否被add过
                transaction.hide(from).add(R.id.fl_content, to).commitAllowingStateLoss() // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss() // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private fun initListener() {
        binding.layoutDetailBottom.tvProductDes.setOnClickListener { checkGoodsDes() }
        binding.layoutDetailBottom.tvComment.setOnClickListener { checkGoodsCom() }

        binding.layoutDetailTop.tvWatchMore.setOnClickListener {
            binding.layoutDetailTop.tvWatchMore.visibility = View.INVISIBLE
            binding.layoutDetailTop.tvBrandStory.maxLines = Integer.MAX_VALUE
            binding.layoutDetailTop.tvBrandStory.requestLayout()
        }

        //默认设置标题栏透明
        binding.layoutTitleRoot.root.setBackgroundColor(Color.argb(0, 255, 255, 255))
        binding.layoutTitleRoot.viewDivider.visibility = View.INVISIBLE

        binding.layoutDetailTop.scrollView.setOnScrollViewListener { scrollX, scrollY, oldx, oldScrollY ->
            var distanceScrollY = binding.layoutDetailTop.banner.height - binding.layoutTitleRoot.root.height

            if (scrollY <= 0) {//未滑动:设置全透明
                binding.layoutTitleRoot.root.setBackgroundColor(Color.argb(0, 255, 255, 255))
                binding.layoutTitleRoot.ivBack.setImageResource(R.drawable.ic_back_arrow_white)
                binding.layoutTitleRoot.ivCart.setImageResource(R.drawable.ic_cart_white)
                binding.layoutTitleRoot.ivShare.setImageResource(R.drawable.ic_share_white)
                binding.layoutTitleRoot.viewDivider.visibility = View.INVISIBLE

            } else if (scrollY in 1..distanceScrollY) { //滑动过程中 并且在mHeight之内
                val scale = scrollY.toFloat() / distanceScrollY
                val alpha = 255 * scale
                //白色
                binding.layoutTitleRoot.root.setBackgroundColor(Color.argb(alpha.toInt(), 255, 255, 255))
//                view_circle_one.setBackgroundColor(Color.argb(alpha.toInt(), 59, 59, 59))
//                view_circle_two.setBackgroundColor(Color.argb(alpha.toInt(), 59, 59, 59))
//                view_circle_three.setBackgroundColor( Color.argb(alpha.toInt(), 59, 59, 59))
                if (scale >= 0.5) {
                    binding.layoutTitleRoot.ivBack.setImageResource(R.drawable.ic_back_arrow_black)
                    binding.layoutTitleRoot.ivCart.setImageResource(R.drawable.ic_cart_black)
                    binding.layoutTitleRoot.ivShare.setImageResource(R.drawable.ic_share_black)
                } else {
                    binding.layoutTitleRoot.ivBack.setImageResource(R.drawable.ic_back_arrow_white)
                    binding.layoutTitleRoot.ivCart.setImageResource(R.drawable.ic_cart_white)
                    binding.layoutTitleRoot.ivShare.setImageResource(R.drawable.ic_share_white)
                }
                binding.layoutTitleRoot.ivShare.visibility = View.INVISIBLE
            } else {//超过mHeight
                binding.layoutTitleRoot.root.setBackgroundColor(Color.argb(255, 255, 255, 255))
                binding.layoutTitleRoot.ivBack.setImageResource(R.drawable.ic_back_arrow_black)
                binding.layoutTitleRoot.ivCart.setImageResource(R.drawable.ic_cart_black)
                binding.layoutTitleRoot.ivShare.setImageResource(R.drawable.ic_share_black)
                binding.layoutTitleRoot.ivShare.visibility = View.VISIBLE
            }
        }
    }

    override fun getGoodsContent(dataBean: GoodsContentBean) {
        Log.e("TAG", "getGoodsContent=" + dataBean.brand)
        tv_goods_title.text = dataBean.title
        binding.layoutDetailTop.tvBrandStory.text = dataBean.brandStory
        tv_price.text = "￥" + dataBean.price
        tv_productUser.text = "主理人：" + dataBean.productUser
        tv_brand.text = "品牌：" + dataBean.brand
        tv_favNum.text = dataBean.favNum
        tv_like.text = "喜欢 " + dataBean.favNum
        tv_productDiscountTxt.text = dataBean.productDiscountTxt
        tv_desc.text = dataBean.platFormWeixin.desc
        if (!TextUtils.isEmpty(dataBean.postage)) tv_postage.text = "邮费：" + dataBean.postage + "元" else tv_postage.text = "包邮"

        GlideUtils.loadCircleImage(dataBean.avaPath, iv_productUser, 0)
        GlideUtils.loadNormalImage(dataBean.brandIcon, iv_brand, 0)

        dataBean.image.let { (dataBean.imgsUrlList as ArrayList<String>).add(0, it) }
        //初始化商品图片轮播
        BannerHelper.setBanner(binding.layoutDetailTop.banner as ConvenientBanner<String>?, dataBean.imgsUrlList)

        binding.layoutDetailTop.tvBrandStory.viewTreeObserver.addOnPreDrawListener((object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                //这个回调会调用多次，获取完行数记得注销监听
                binding.layoutDetailTop.tvBrandStory.viewTreeObserver.removeOnPreDrawListener(this);
                Log.e("TAG", "TextView 行数：" + binding.layoutDetailTop.tvBrandStory.lineCount)
                if (binding.layoutDetailTop.tvBrandStory.lineCount < 3) binding.layoutDetailTop.tvWatchMore.visibility = View.INVISIBLE else binding.layoutDetailTop.tvWatchMore.visibility = View.VISIBLE
                return false
            }

        }))
    }

    override fun getGoodsDescription(dataList: List<GoodsDesBean>) {

//        GlideUtils.loadNormalImage(dataList[1].content, iv_bottom, 0)
        Log.e("TAG", "getGoodsDescription=" + dataList.size)
        EventBus.getDefault().post(dataList)
    }

    override fun getGoodsCommentList(dataList: List<CommentBean>) {
        Log.e("TAG", "getGoodsCommentList=" + dataList.size)
    }

    override fun getRevelentGoodsList(dataBean: RevelentBean) {
        Log.e("TAG", "getRevelentGoodsList=" + dataBean.revelentList.size)
    }


}