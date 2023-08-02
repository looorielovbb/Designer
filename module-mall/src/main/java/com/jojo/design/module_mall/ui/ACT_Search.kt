package com.jojo.design.module_mall.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jojo.design.common_base.utils.RxJava2Helper
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.BaseApplication
import com.jojo.design.common_base.adapter.rv.MultiItemTypeAdapter
import com.jojo.design.common_base.config.arouter.ARouterConfig
import com.jojo.design.common_base.config.arouter.ARouterConstants
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.RecyclerviewHelper
import com.jojo.design.common_base.utils.ToastUtils
import com.jojo.design.module_mall.R
import com.jojo.design.module_mall.adapter.ADA_SearchHistory
import com.jojo.design.module_mall.bean.CategoryBean
import com.jojo.design.module_mall.bean.FilterBean
import com.jojo.design.module_mall.bean.RecordsEntity
import com.jojo.design.module_mall.databinding.ActSearchBinding
import com.jojo.design.module_mall.db.bean.SearchHistoryBean
import com.jojo.design.module_mall.db.AppDatabaseHelper
import com.jojo.design.module_mall.mvp.contract.SearchContract
import com.jojo.design.module_mall.mvp.contract.SearchContract.*
import com.jojo.design.module_mall.mvp.model.SearchModel
import com.jojo.design.module_mall.mvp.presenter.SearchPresenter
import com.smart.novel.adapter.ADA_HotSearchTag
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/29 10:08 AM
 *    desc   : 商品搜索页面
 */
@Route(path = ARouterConfig.ACT_SEARCH)
class ACT_Search : BaseActivity<SearchPresenter, SearchModel>(), SearchContract.View {
    private var mHistoryAdapter: ADA_SearchHistory? = null
    private var mHotSearchAdapter: ADA_HotSearchTag? = null
    private var mHistoryList: ArrayList<SearchHistoryBean> = ArrayList()
    private lateinit var binding:ActSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startEvents()
    }
    
    private fun startEvents() {
        val data = ArrayList<SearchHistoryBean>()
//        (0..10).mapTo(data) { SearchHistoryBean(it.toLong(), "item=" + it) }
        //搜索历史
        mHistoryAdapter = ADA_SearchHistory(mContext)
        RecyclerviewHelper.initNormalRecyclerView(binding.rvHistory, mHistoryAdapter!!, object : LinearLayoutManager(mContext) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        })
        mHistoryAdapter!!.update(data, true)
        mPresenter?.getHotList()
//        mPresenter?.getSearchGoods("2", "", 0)
        getLocalHistory()
        initListener()
    }

    private fun getLocalHistory() {
        AppDatabaseHelper.getInstance(mContext).appDataBase.historyDao().getAllHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    val msg = "query success, list is " + list.size
                    Log.e("TAG", msg)
                    mHistoryList = list as ArrayList<SearchHistoryBean>
                    mHistoryList.reverse()
                    mHistoryAdapter?.update(mHistoryList, true)
                }, { throwable -> Log.e("TAG", throwable.message.toString()) }).let { 
                    
            }
    }

    private fun initListener() {
        
        binding.llSearch.tvCancel.setOnClickListener {
            // 先隐藏键盘
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus!!
                            .windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            if (!TextUtils.isEmpty(binding.llSearch.etSearch.text)) binding.llSearch.etSearch.setText("") else finish()

        }
        //键盘搜索
        binding.llSearch.etSearch.setOnKeyListener { _, keyCode, event ->
            // event.action == KeyEvent.ACTION_UP 解决回车键执行两次的问题
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (currentFocus != null && currentFocus!!.windowToken != null) {
                    // 先隐藏键盘
                    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(
                            currentFocus!!
                                .windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                        )
                }
                doSearch(binding.llSearch.etSearch.text.toString().trim())
            }
            false
        }
        /*taglayout.setOnTagClickListener { view, position, parent ->
            var history = SearchHistoryBean()
            history.searchKeyWords = mHotSearchAdapter!!.getItem(position)
            binding.llSearch.etSearch.setText(history.searchKeyWords)

            doSearch(mHotSearchAdapter!!.getItem(position))
            true
        }*/
        mHistoryAdapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                val bean = mHistoryAdapter!!.dataList[position]
                doSearch(bean?.searchKeyWords!!)
            }

            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

        })

        binding.ivDeleteAll.setOnClickListener {
            //删除所有记录
            RxJava2Helper.getFlowable {
                AppDatabaseHelper.getInstance(mContext).appDataBase.historyDao().run {
                    deleteAll(mHistoryAdapter?.dataList!!)
                }
            }.subscribe {
                Log.e("TAG", "delete success delete_count=$it")
            }.isDisposed
        }
    }

    /**
     * 搜索操作
     */
    private fun doSearch(keywords: String) {
        if (TextUtils.isEmpty(keywords.trim())) {
            ToastUtils.makeShortToast(BaseApplication.application.getString(R.string.content_search_content_not_empty))
            return
        }
        val bean = SearchHistoryBean()
        bean.searchKeyWords = keywords.trim()

        saveLocalByRoom(bean)

        //跳转到新页面进行搜索结果展示
        ARouter.getInstance().build(ARouterConfig.ACT_GOODS_FILTER)
                .withString(ARouterConstants.SEARCH_KEYWORDS, keywords)
                .withString(ARouterConstants.TAG_CATEGORY_ID, "")
                .navigation()
    }

    /**
     * 本地存储SearchHistoryBean
     */
    private fun saveLocalByRoom(bean: SearchHistoryBean) {
        var isAlreadyExistBean: SearchHistoryBean? = null
        //如果本地存在，先删除后插入存储
        (0 until mHistoryList.size)
                .filter { bean.searchKeyWords.equals(mHistoryList[it].searchKeyWords) }
                .forEach { isAlreadyExistBean = mHistoryList[it] }
        //删除一条记录
        isAlreadyExistBean?.let {
            RxJava2Helper.getFlowable {
                AppDatabaseHelper.getInstance(mContext).appDataBase.historyDao().run {
                    //it为isAlreadyExistBean
                    deleteItem(it)
                }
            }.subscribe {
                Log.e("TAG", "delete success delete_count=" + it)
            }.isDisposed
        }
        //插入一条记录
        RxJava2Helper.getFlowable {
            AppDatabaseHelper.getInstance(mContext).appDataBase.historyDao().apply {
                insert(bean)
            }
        }.subscribe {
            Log.e("TAG", "insert success index=$it")
        }.isDisposed
    }

    override fun getHotList(dataList: List<String>) {
        Log.e("TAG", "dataList=" + dataList.size)
        mHotSearchAdapter = ADA_HotSearchTag(dataList)
        binding.tagLayout.adapter = mHotSearchAdapter
    }

    override fun getSearchGoods(dataBean: RecordsEntity) {
        Log.e("TAG", "dataBean=" + dataBean.size)
    }

    override fun getCategoryList(dataList: List<CategoryBean>) {
    }

    override fun getFilterData(dataBean: FilterBean) {
    }

}