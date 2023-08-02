package com.jojo.design.module_test.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jojo.design.module_test.R

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/28 2:15 PM
 *    desc   :
 */
class ADA_TestLoadMore constructor(context: Context, data: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null
    private var mData = ArrayList<String>()
    private var isNoMore = false

    init {
        mContext = context
        mData = data as ArrayList<String>
    }

    companion object{
        const val TYPE_NORMAL = 0
        const val TYPE_FOOTER = 1
    }

    class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemView: View? = null
        var tv: TextView? = null

        init {
            mItemView = itemView
            tv = mItemView?.findViewById(R.id.tv)
        }

    }

    class FooterHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemView: View? = null
        var tvFooter: TextView? = null
        var pb: ProgressBar? = null

        init {
            mItemView = itemView
            pb = mItemView?.findViewById(R.id.pb)
            tvFooter = mItemView?.findViewById(R.id.tv_footer)
        }

    }

    var mFooterHolder: FooterHolder? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_NORMAL) {
            val normalItemView = LayoutInflater.from(mContext).inflate(R.layout.item_normal, null)
            return MyViewHolder(normalItemView)
        } else if (viewType == TYPE_FOOTER) {
            val footerView = LayoutInflater.from(mContext).inflate(R.layout.item_footer, null)
            val footerHolder = FooterHolder(footerView)
            mFooterHolder = footerHolder
            return footerHolder
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mData.size) {
            TYPE_FOOTER
        } else {
            TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder: RecyclerView.ViewHolder
        if (holder is MyViewHolder) {
            mHolder = holder
            mHolder.tv?.text = mData[position]
        } else {
            mHolder = holder as FooterHolder
            mHolder.tvFooter?.text = "我是FooterView"
            Log.e("TAG", "isNoMore=" + isNoMore)
            if (isNoMore) {
                mHolder.tvFooter?.text = "没有更多了"
                mHolder.pb?.visibility = View.GONE
            } else {
                mHolder.tvFooter?.text = "加载中..."
                mHolder.pb?.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = mData.size + 1
    fun setisNoMore(b: Boolean) {
        isNoMore = b
        Log.e("TAG", "setisNoMore")
    }

    fun updata(data: ArrayList<String>) {
        mData.addAll(data)
        isNoMore = true
        notifyDataSetChanged()
    }
}
