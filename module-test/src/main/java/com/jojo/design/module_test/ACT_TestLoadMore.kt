package com.jojo.design.module_test

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.module_test.adapter.ADA_TestLoadMore
import com.jojo.design.module_test.databinding.ActTestLoadmoreBinding

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/28 2:12 PM
 *    desc   : 测试自定义上拉加载更多的RecyclerView
 */
class ACT_TestLoadMore : BaseActivity<BaseContract.BasePresenter, BaseContract.BaseModel>() {
    var mAdapter: ADA_TestLoadMore? = null
    private lateinit var binding: ActTestLoadmoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActTestLoadmoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startEvents()
    }

    private fun startEvents() {
        binding.recyclerview.layoutManager = LinearLayoutManager(mContext)
        val data = ArrayList<String>()
        (1..30).mapTo(data) { "item=$it" }
        mAdapter = ADA_TestLoadMore(mContext, data)
        binding.recyclerview.adapter = ADA_TestLoadMore(mContext, data)


        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager
                val itemCount = layoutManager?.itemCount
                Log.e("TAG", "itemCount=" + itemCount)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        Log.e("TAG", "SCROLL_STATE_DRAGGING")
                    }

                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Log.e("TAG", "SCROLL_STATE_IDLE")
                    }

                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        Log.e("TAG", "SCROLL_STATE_SETTLING")
                    }

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var layoutManager = recyclerView.layoutManager
                var lastVisibleItemPosition = findLastVisibleItemPosition(layoutManager!!)
                Log.e("TAG", "lastVisibleItemPosition=" + lastVisibleItemPosition)
                if (lastVisibleItemPosition == 30) {
                    mAdapter?.mFooterHolder?.tvFooter?.text = "嘻嘻嘻嘻"
                    loadMore()
                } else {
                    mAdapter?.mFooterHolder?.tvFooter?.text = "哈哈哈哈"
                }
            }
        })

//        binding.recyclerview.layoutManager = LinearLayoutManager(mContext)
//        var adapter = ADA_TestLoadMore(mContext,data)
//        binding.recyclerview.adapter = adapter;
//
////        设置 下拉刷新 和加载更多的 监听
//        binding.recyclerview.setMyRecyclerViewListener(object :MyRecyclerView.MyRecyclerViewListener {
//            override fun onRefresh() {
//                android.os.Handler().postDelayed({
//                    binding.recyclerview.setRefreshComplete()
//                },2000)
//            }
//
//            override fun onLoadMore() {
//                android.os.Handler().postDelayed({
//                    binding.recyclerview.setLoadMoreComplete()
//                },2000)
//            }
//        })
    }

    private fun loadMore() {
//        var data = ArrayList<String>()
//        (1..5).mapTo(data) { "loadmore的数据=" + it }
//        android.os.Handler().postDelayed({
//        mAdapter?.updata(data)
//            mAdapter?.setisNoMore(true)
//        }, 2000)
    }

    /**
     * 找到最后一个可见的item
     */
    private fun findLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is LinearLayoutManager) {
            return layoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
            return findMax(lastVisibleItemPositions)
        }
        return -1
    }

    /**
     * StaggeredGridLayoutManager时，查找position最大的列
     *
     * @param lastVisiblePositions
     * @return
     */
    fun findMax(lastVisiblePositions: IntArray): Int {
        var max = lastVisiblePositions[0]
        for (value in lastVisiblePositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }
}