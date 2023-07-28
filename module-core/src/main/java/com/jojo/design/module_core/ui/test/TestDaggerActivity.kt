package com.jojo.design.module_core.ui.test

import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.jojo.design.common_base.dagger.mvp.BaseActivity
import com.jojo.design.common_base.utils.ToastUtils
import com.jojo.design.common_ui.view.MultipleStatusView
import com.jojo.design.module_core.R
import com.jojo.design.module_core.mvp.contract.TestContract
import com.jojo.design.module_core.mvp.model.TestModel
import com.jojo.design.module_core.mvp.presenter.TestPresenter
import com.smart.novel.util.bindView

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/4 10:07 PM
 *    desc   : 测试Dagger2+MVP 架构
 */
@Route(path = "/base/act_testdagger")
class TestDaggerActivity : BaseActivity<TestPresenter, TestModel>(), TestContract.View {
    private val tv_head by bindView<TextView>(R.id.tv_head)
    override fun getData(result: String) {
        ToastUtils.makeShortToast(result)
        Log.e("TAG", "result=" + result)
//        var bean = ErrorBean()
//        bean.msg = result
//        viewDataBinding?.bean = bean
    }

    override fun getContentViewLayoutId(): Int = R.layout.test

    override fun getLoadingMultipleStatusView(): MultipleStatusView? = multiplestatusview

    override fun initDaggerInject(mApplicationComponent: ApplicationComponent) {
//        DaggerCoreComponent.builder().applicationComponent(mApplicationComponent).build().inject(this)
    }

    override fun startEvents() {
        tv_head.setText("Dagger+MVP-Activity中测试ButterKnife")
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