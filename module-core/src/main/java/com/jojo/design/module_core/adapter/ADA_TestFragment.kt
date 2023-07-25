package com.jojo.design.module_core.adapter

import android.content.Context
import com.jojo.design.common_base.adapter.rv.CommonAdapter
import com.jojo.design.common_base.adapter.rv.ViewHolder
import com.jojo.design.module_core.R
import com.jojo.design.module_core.databinding.ItemTestBinding

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 5:43 PM
 *    desc   :
 */
class ADA_TestFragment constructor(context: Context) : CommonAdapter<String>(context) {
    override fun convert(holder: ViewHolder?, t: String?, position: Int) {
        holder?.setText(R.id.tv,t)
    }

    override fun itemLayoutId(): Int = R.layout.item_test
}