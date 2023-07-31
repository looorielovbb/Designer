package com.jojo.design.module_core.ui.test

import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.ToastUtils
import com.jojo.design.module_core.databinding.TestBinding
import com.jojo.design.module_core.mvp.contract.TestContract
import com.jojo.design.module_core.mvp.model.TestModel
import com.jojo.design.module_core.mvp.presenter.TestPresenter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/4 10:07 PM
 *    desc   : 测试Dagger2+MVP 架构
 */
@Route(path = "/base/act_testdagger")
class TestDaggerActivity : BaseActivity<TestPresenter, TestModel>(), TestContract.View {
    override fun getData(result: String) {
        ToastUtils.makeShortToast(result)
        Log.e("TAG", "result=$result")
    }

    lateinit var binding:TestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        startEvents()
    }




    private fun startEvents() {
        binding.tvHead.text = "Dagger+MVP-Activity中测试ButterKnife"
        mMultipleStatusView?.showLoading()
        mPresenter?.getData("presenter init successful Activity-MVP-测试")
        Handler().postDelayed({
            mMultipleStatusView?.showContent()
        }, 2000)

    }

    override fun getOverridePendingTransitionMode(transitionMode: TransitionMode): TransitionMode {
        return super.getOverridePendingTransitionMode(TransitionMode.BOTTOM)
    }
}