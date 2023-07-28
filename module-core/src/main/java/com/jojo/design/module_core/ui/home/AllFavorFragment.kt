package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_core.R
import com.jojo.design.module_core.adapter.ADA_PersonLike
import com.jojo.design.module_core.bean.AllfaverEntity
import com.jojo.design.module_core.bean.CategoryEntity
import com.jojo.design.module_core.bean.GoodsEntity
import com.jojo.design.module_core.bean.RecordsEntity
import com.jojo.design.module_core.databinding.FraAllFavorBinding
import com.jojo.design.module_core.mvp.contract.ShoppingContract
import com.jojo.design.module_core.mvp.model.ShoppingModel
import com.jojo.design.module_core.mvp.presenter.ShoppingPresenter

/**
 * author : JOJO
 * e-mail : 18510829974@163.com
 * date   : 2018/12/20 5:54 PM
 * desc   : 逛-底部-大家喜欢
 */
class AllFavorFragment : BaseFragment<ShoppingPresenter, ShoppingModel>(), ShoppingContract.View {

    private var mAdapter: ADA_PersonLike? = null
    private var _binding: FraAllFavorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_all_favor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FraAllFavorBinding.bind(view)
        mPresenter?.getPersonLike()
        mAdapter = ADA_PersonLike(mContext)
        with(binding.layoutR) {
            recyclerview.layoutManager = LinearLayoutManager(mContext)
            recyclerview.adapter = mAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getCategoryList(dataList: List<CategoryEntity>) {
    }

    override fun getGoodsList(dataList: List<GoodsEntity>) {
    }

    /**
     * 精选
     */
    override fun getHandPickedGoods(bean: RecordsEntity) {

    }

    /**
     * 大家喜欢
     */
    override fun getPersonLike(dataList: List<AllfaverEntity>) {
        mAdapter?.update(dataList[0].list, true)
    }

}