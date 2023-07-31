package com.jojo.design.module_core.ui.test

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.common_base.utils.ToastUtils
import com.jojo.design.module_core.R
import com.jojo.design.module_core.adapter.ADA_TestFragment
import com.jojo.design.module_core.databinding.TestFragmentBinding
import com.jojo.design.module_core.mvp.contract.TestContract
import com.jojo.design.module_core.mvp.model.TestModel
import com.jojo.design.module_core.mvp.presenter.TestPresenter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 3:56 PM
 *    desc   :
 */
class TestFragment : BaseFragment<TestPresenter, TestModel>(), TestContract.View {
    var binding: TestFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<TestFragmentBinding>(
            inflater,
            R.layout.test_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.getBinding(view)
        startFragmentEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun startFragmentEvents() {
//        var bean = ErrorBean()
//        bean.msg = "Fragment中测试DataBinding"
//        viewBinding?.bean = bean
//        tv_txt.text = "Fragment-kotlin初始化View的值"
        binding!!.tvTxt.text = "Fragment测试ButterKnife"
        Log.e("TAG", "TestFragment-Presenter=$mPresenter")

        mMultipleStatusView?.showLoading()
        mPresenter?.getData("presenter init successful Fragment-MVP-测试")
        binding!!.recyclerview.layoutManager = LinearLayoutManager(mContext)
        val mAdapter = ADA_TestFragment(mContext)
        binding!!.recyclerview.adapter = mAdapter
        val data = ArrayList<String>()
        for (i in 0..50) {
            data.add("item=$i")
        }
        mAdapter.update(data, true)
        Handler().postDelayed({
            mMultipleStatusView?.showContent()
        }, 2000)
    }

    override fun getData(result: String) {
        ToastUtils.makeShortToast(result)
    }
}