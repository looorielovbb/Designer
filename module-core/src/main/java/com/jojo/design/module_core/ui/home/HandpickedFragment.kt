package com.jojo.design.module_core.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_core.R
import com.jojo.design.module_core.adapter.ADA_Handpicked
import com.jojo.design.module_core.bean.*
import com.jojo.design.module_core.databinding.FraHandpickedBinding
import com.jojo.design.module_core.mvp.contract.ShoppingContract
import com.jojo.design.module_core.mvp.model.ShoppingModel
import com.jojo.design.module_core.mvp.presenter.ShoppingPresenter

/**
 * author : JOJO
 * e-mail : 18510829974@163.com
 * date   : 2018/12/20 5:54 PM
 * desc   : 逛-底部-精选
 */
class HandpickedFragment : BaseFragment<ShoppingPresenter, ShoppingModel>(), ShoppingContract.View {
    private var mAdapter: ADA_Handpicked? = null
    private var binding:FraHandpickedBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_handpicked,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.getHandPickedGoods("2")
        binding = FraHandpickedBinding.bind(view)
        mAdapter = ADA_Handpicked(mContext)
        binding?.baseLayout?.recyclerview?.layoutManager = GridLayoutManager(mContext, 2)
        binding?.baseLayout?.recyclerview?.adapter = mAdapter
        //设置item之间的间距
        binding?.baseLayout?.recyclerview?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.left = 30
                outRect.top = 20
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun getCategoryList(dataList: List<CategoryEntity>) {
    }

    override fun getGoodsList(dataList: List<GoodsEntity>) {
    }

    /**
     * 精选
     */
    override fun getHandPickedGoods(bean: RecordsEntity) {
        mAdapter?.update(bean.records, true)
    }

    /**
     * 大家喜欢
     */
    override fun getPersonLike(dataList: List<AllfaverEntity>) {
    }
}
