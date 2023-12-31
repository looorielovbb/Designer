package com.jojo.design.module_mall.ui

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.BaseApplication
import com.jojo.design.common_base.adapter.rv.MultiItemTypeAdapter
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.config.arouter.ARouterConstants
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.RecyclerviewHelper
import com.jojo.design.common_base.utils.ToastUtils
import com.jojo.design.common_ui.lrecyclerview.recyclerview.LRecyclerView
import com.jojo.design.common_ui.view.MyPopupWindow
import com.jojo.design.common_ui.view.NoScrollGridView
import com.jojo.design.module_mall.R
import com.jojo.design.module_mall.adapter.ADA_ChooseCategory
import com.jojo.design.module_mall.adapter.ADA_FilterPrice
import com.jojo.design.module_mall.adapter.ADA_FilterService
import com.jojo.design.module_mall.adapter.ADA_SearchGoods
import com.jojo.design.module_mall.bean.CategoryBean
import com.jojo.design.module_mall.bean.FilterBean
import com.jojo.design.module_mall.bean.RecordsEntity
import com.jojo.design.module_mall.databinding.ActGoodsFilterBinding
import com.jojo.design.module_mall.dialog.DIA_Filter
import com.jojo.design.module_mall.helper.PopupFilter
import com.jojo.design.module_mall.mvp.contract.SearchContract
import com.jojo.design.module_mall.mvp.model.SearchModel
import com.jojo.design.module_mall.mvp.presenter.SearchPresenter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2019/1/2 6:10 PM
 *    desc   : 商品搜索、筛选结果页面
 */
@Route(path = ARouterConfig.ACT_GOODS_FILTER)
class ACT_GoodsFilter : BaseActivity<SearchPresenter, SearchModel>(), SearchContract.View {
    private var mAdapter: ADA_SearchGoods? = null
    private var mAdapterCategory: ADA_ChooseCategory? = null
    private var mAdapterRecommend: ADA_ChooseCategory? = null
    private var mRecommendList = ArrayList<CategoryBean>()
    private var mCategoryPupWindow: MyPopupWindow? = null
    private var mRecommendPupWindow: MyPopupWindow? = null
    private var mDiaFilter: DIA_Filter? = null
    private var gvDiscount: NoScrollGridView? = null
    private var gvPrice: NoScrollGridView? = null
    private var tvConfirmFilter: TextView? = null
    private var mAdapterfilterService: ADA_FilterService? = null
    private var mAdapterfilterPrice: ADA_FilterPrice? = null
    private var selectFilterPrice: String? = null
    //    @BindView(R.id.iv_search) lateinit var ivSearch: ImageView //单模块下开发OK，组件化开发会报编译错误
    private val ivSearch:ImageView? = null
    private var outCategoryId: String? = null
    private var keyword: String? = null
    private var sort: Int = 0 //最新、最热、推荐

    private var isClick = false
    private var preBean: CategoryBean? = null
    private  var preBeanRec: CategoryBean? = null
    private lateinit var binding:ActGoodsFilterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_goods_filter)
        startEvents()
    }

    private fun startEvents() {
        ivSearch?.visibility = View.VISIBLE
        outCategoryId = intent.extras?.getString(ARouterConstants.TAG_CATEGORY_ID)
        keyword = intent.extras?.getString(ARouterConstants.SEARCH_KEYWORDS)
        setHeaderTitle(keyword!!)
//        setSupportActionBar(binding.llTitle.toolbar)
        initGoodsRecyclerview()
        mAdapterCategory = ADA_ChooseCategory(mContext)
        mAdapterRecommend = ADA_ChooseCategory(mContext)
        //选择分类弹窗
        mCategoryPupWindow = PopupFilter.initPopupWindow(this, mAdapterCategory!!, true)
        //推荐弹窗
        mRecommendPupWindow = PopupFilter.initPopupWindow(this, mAdapterRecommend!!, true)
        //推荐弹窗数据
        mRecommendList.add(CategoryBean(1, "最新", false)) //id对应着筛选时的sort
        mRecommendList.add(CategoryBean(2, "最热", false))
        mRecommendList.add(CategoryBean(0, "推荐", true))
        preBeanRec = mRecommendList[2]
        mAdapterRecommend?.update(mRecommendList, true)
        //筛选弹窗
        mDiaFilter = DIA_Filter(this)
        gvDiscount = mDiaFilter?.mContentView?.findViewById<NoScrollGridView>(R.id.gv_discount)
        gvPrice = mDiaFilter?.mContentView?.findViewById<NoScrollGridView>(R.id.gv_price)
        tvConfirmFilter = mDiaFilter?.mContentView?.findViewById<TextView>(R.id.tv_confirm)
        mAdapterfilterService = ADA_FilterService(mContext)
        gvDiscount?.adapter = mAdapterfilterService
        mAdapterfilterPrice = ADA_FilterPrice(mContext)
        gvPrice?.adapter = mAdapterfilterPrice
        requestNet()
        initListener()
    }

    /**
     * 网络请求
     */
    private fun requestNet() {
        //传了分类ID，就不传关键字匹配
        val paramsMap = HashMap<String, String>()
        requestGoodList(paramsMap)
        //请求选择分类和筛选弹窗的数据
        //传了分类ID，就不传关键字匹配
        if (!TextUtils.isEmpty(outCategoryId)) mPresenter?.getCategoryList(outCategoryId!!, "") else mPresenter?.getCategoryList(outCategoryId!!, keyword!!)
        mPresenter?.getFilterData(outCategoryId!!)
    }

    /**
     * 初始化商品列表
     */
    private fun initGoodsRecyclerview() {
        val lrecyclerview = findViewById<LRecyclerView>(R.id.lrecyclerview)
        mAdapter = ADA_SearchGoods(mContext)
        RecyclerviewHelper.initLayoutManagerRecyclerView(lrecyclerview, mAdapter!!, GridLayoutManager(mContext, 2), mContext)
        lrecyclerview.setPullRefreshEnabled(false)
        // //设置item之间的间距
        lrecyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                //设计图item之间的间距为40 (header占了一个位置，故从位置1开始显示实际的item)
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = 40
                }
                if ((parent.getChildAdapterPosition(view) - 1) % 2 == 0) {
                    outRect.left = 40
                } else {
                    outRect.left = 20
                }
            }
        })
        lrecyclerview.setOnRefreshListener {
            Handler(Looper.myLooper()!!).postDelayed({ lrecyclerview.refreshComplete(1) }, 2000)
        }
    }

    private fun initListener() {
        //推荐
        binding.llFilter.rbRecommend.setOnClickListener {
            binding.llFilter.rbCategory.isSelected = false
            mCategoryPupWindow?.dismiss()

            if (isClick) hideRecommendPopup() else showRecommendPopup()

        }
        //选择分类
        binding.llFilter.rbCategory.setOnClickListener {
            binding.llFilter.rbRecommend.isSelected = false
            mRecommendPupWindow?.dismiss()

            if (isClick) hideCategoryPopup() else showCategoryPopup()

        }
        //筛选
        binding.llFilter.rbFilter.setOnClickListener {
            mDiaFilter?.show()
        }
        //选择分类筛选操作
        mAdapterCategory?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val bean = mAdapterCategory!!.dataList[position]

                if (bean == preBean) {
                    bean.isCheck = bean.isCheck
                } else {
                    if (preBean == null) {
                        bean.isCheck = true
                    } else {
                        preBean?.isCheck = false
                        bean.isCheck = true
                    }
                }
                preBean = bean
                mAdapterCategory?.notifyDataSetChanged()

                //隐藏选择分类弹窗
                hideCategoryPopup()

                val paramsMap = HashMap<String, String>()
                //传了分类ID，就不传关键字匹配
                outCategoryId = bean.id.toString()
                if (bean.id == 0) outCategoryId = intent.extras?.getString(ARouterConstants.TAG_CATEGORY_ID)
                requestGoodList(paramsMap)

                binding.llFilter.rbCategory.text = bean.name
            }

            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

        })
        //推荐筛选操作
        mAdapterRecommend?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val bean = mAdapterRecommend!!.dataList[position]
                if (bean == preBeanRec) {
                    bean.isCheck = bean.isCheck
                } else {
                    if (preBeanRec == null) {
                        bean.isCheck = true
                    } else {
                        preBeanRec?.isCheck = false
                        bean.isCheck = true
                    }
                }
                preBeanRec = bean
                mAdapterRecommend?.notifyDataSetChanged()
                hideRecommendPopup()

                val paramsMap = HashMap<String, String>()
                sort = bean.id
                requestGoodList(paramsMap)

                binding.llFilter.rbRecommend.text = bean.name
            }

            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

        })
        //筛选弹窗内折扣选择(多选)
        gvDiscount?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val bean = mAdapterfilterService!!.dataList[position]
            bean.isCheck = !bean.isCheck
            mAdapterfilterService?.notifyDataSetChanged()
        }
        //筛选弹窗内价格筛选选择(单选)
        gvPrice?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectFilterPrice = mAdapterfilterPrice!!.dataList[position]
            mAdapterfilterPrice?.mSelectPos = position
            mAdapterfilterPrice?.notifyDataSetChanged()
        }
        //筛选操作
        tvConfirmFilter?.setOnClickListener {
            mDiaFilter?.dismiss()
            val paramsMap = HashMap<String, String>()
            for (i in 0 until mAdapterfilterService?.dataList?.size!!) {
                val promotionTagBean = mAdapterfilterService!!.dataList[i]
                if (promotionTagBean.isCheck) {
                    paramsMap["promotionTags[$i]"] = promotionTagBean.key
                }
            }
            if (!TextUtils.isEmpty(selectFilterPrice)) {
                val splitArray = selectFilterPrice?.split("-")
                paramsMap["minPrice"] = splitArray!![0]
                paramsMap["maxPrice"] = splitArray[1]
            }

            requestGoodList(paramsMap)
        }
        mAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val realPps = position - 1
                val recordsBean = mAdapter!!.dataList[realPps]
                //跳转到新页面进行搜索结果展示
                ARouter.getInstance().build(ARouterConfig.ACT_GOODS_DETAIL)
                        .withString(ARouterConstants.PRODUCT_ID, recordsBean.productId)
                        .navigation()
            }

            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

        })
    }

    /**
     * 请求商品列表：传了分类ID，就不传关键字匹配
     */
    private fun requestGoodList(paramsMap: HashMap<String, String>) {
        if (!TextUtils.isEmpty(outCategoryId)) mPresenter?.getSearchGoods(outCategoryId!!, "", 0, sort, paramsMap)
        else mPresenter?.getSearchGoods(outCategoryId!!, keyword!!, 0, sort, paramsMap)
    }

    /**
     * 隐藏选择分类弹窗
     */
    fun hideCategoryPopup() {
        binding.llFilter.rbCategory.isSelected = false
        isClick = false
        binding.bgPopup.visibility = View.GONE
        mCategoryPupWindow?.dismiss()
    }

    /**
     * 展示选择分类弹窗
     */
    private fun showCategoryPopup() {
        binding.llFilter.rbCategory.isSelected = true
        isClick = true
        binding.bgPopup.visibility = View.VISIBLE
        mCategoryPupWindow?.showAsDropDown(binding.llFilter.root)
    }

    /**
     * 隐藏推荐弹窗
     */
    fun hideRecommendPopup() {
        binding.llFilter.rbRecommend.isSelected = false
        isClick = false
        binding.bgPopup.visibility = View.GONE
        mRecommendPupWindow?.dismiss()
    }

    /**
     * 展示推荐弹窗
     */
    private fun showRecommendPopup() {
        binding.llFilter.rbRecommend.isSelected = true
        isClick = true
        binding.bgPopup.visibility = View.VISIBLE
        mRecommendPupWindow?.showAsDropDown(binding.llFilter.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        //防止内存泄漏
        mRecommendPupWindow?.dismiss()
        mCategoryPupWindow?.dismiss()
    }

    override fun getHotList(dataList: List<String>) {
    }


    override fun getSearchGoods(dataBean: RecordsEntity) {
        if (dataBean.records.isEmpty()) {
            ToastUtils.makeShortToast(BaseApplication.application.getString(R.string.content_search_content_not_empty))
            mAdapter?.update(ArrayList<RecordsEntity.RecordsBean>(), true)
            return
        }
        mAdapter?.update(dataBean.records, true)
    }

    override fun getCategoryList(dataList: List<CategoryBean>) {
        val showList = dataList as ArrayList<CategoryBean>
        val firstBean = CategoryBean(0, "全部显示", true)
        preBean = firstBean
        showList.add(0, firstBean)
        mAdapterCategory?.update(showList, true)
    }

    override fun getFilterData(dataBean: FilterBean) {
        mAdapterfilterService?.update(dataBean.promotionTags, true)
        mAdapterfilterPrice?.update(dataBean.stageRange, true)

    }
}