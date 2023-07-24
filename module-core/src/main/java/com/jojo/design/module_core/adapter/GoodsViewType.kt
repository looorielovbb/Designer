package com.jojo.design.module_core.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jojo.design.common_base.adapter.rv.ItemViewDelegate
import com.jojo.design.common_base.adapter.rv.ViewHolder
import com.jojo.design.module_core.R
import com.jojo.design.module_core.bean.ContentBean

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/12 10:02 PM
 *    desc   : 商品列表（大图+商品列表）
 */
class GoodsViewType constructor(context: Context) : ItemViewDelegate<ContentBean> {
    var mContext: Context? = null

    init {
        mContext = context
    }

    override fun getItemViewLayoutId(): Int = R.layout.goods_view_type

    override fun isForViewType(item: ContentBean, position: Int): Boolean {
        return item.type == 2
    }

    override fun convert(holder: ViewHolder, bean: ContentBean, position: Int) {
        var adapter = ADA_ItemGoods(mContext!!)
        var rv = holder.getView<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(mContext)
        rv.adapter = adapter
        adapter.update(bean.goods, true)

    }
}