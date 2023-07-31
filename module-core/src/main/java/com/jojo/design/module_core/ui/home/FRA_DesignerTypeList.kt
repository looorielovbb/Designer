package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_core.R
import com.jojo.design.module_core.adapter.ADA_DesignerTypeList
import com.jojo.design.module_core.bean.TagCategoryEntity
import com.jojo.design.module_core.databinding.FraDesignertypeListBinding
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/10 3:45 PM
 *    desc   : Designer-设计分类 行业、风格等
 */
class FRA_DesignerTypeList : BaseFragment<BaseContract.BasePresenter, BaseContract.BaseModel>() {
    private var mAdapter: ADA_DesignerTypeList? = null
    private var mSelectedPosition = 0
    var binding:FraDesignertypeListBinding? = null
    companion object {
        fun newInstance(type: String): FRA_DesignerTypeList {
            val pagerFragment = FRA_DesignerTypeList()
            val bundle = Bundle()
            bundle.putString("type", type)
            pagerFragment.arguments = bundle
            return pagerFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_designertype_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FraDesignertypeListBinding.bind(view)
         mSelectedPosition = FragmentPagerItem.getPosition(arguments)

        mAdapter = ADA_DesignerTypeList(mContext)
        binding?.gridview?.adapter = mAdapter
        binding?.gridview?.isFocusableInTouchMode = false
    }

    /**
     * 接收ShopingFragment请求的设计师分类列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshData(dataList: List<TagCategoryEntity>) {
        mAdapter?.update(dataList[mSelectedPosition].tags, true)
        //解决进入页面时列表高度显示不全的问题
        Handler().postDelayed({
            (activity as ACT_Home).mDesignerFragment?.resetViewPagerHeight(0)
        }, 500)

    }

    override fun isBindEventBus(isBind: Boolean): Boolean {
        return super.isBindEventBus(true)
    }
}