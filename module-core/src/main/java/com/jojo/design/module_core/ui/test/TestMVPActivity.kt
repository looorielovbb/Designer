package com.jojo.design.module_core.ui.test

import android.os.Handler
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jojo.design.common_base.bean.ErrorBean
import com.jojo.design.common_base.dagger.mvp.databinding.BaseDBActivity
import com.jojo.design.common_ui.view.MultipleStatusView
import com.jojo.design.module_core.R
import com.jojo.design.module_core.databinding.ActTestMvpBinding
import com.jojo.design.module_core.mvp.model.TestModel
import com.jojo.design.module_core.mvp.presenter.TestPresenter

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/11/29 3:07 PM
 *    desc   : Activity测试
 */
@Route(path = "/core/search")
class TestMVPActivity : BaseDBActivity<TestPresenter, TestModel, ActTestMvpBinding>() {
    lateinit var  tv_txt:TextView
    override fun getContentViewLayoutId()=R.layout.act_test_mvp

    override fun getLoadingMultipleStatusView(): MultipleStatusView? = null

    override fun initDaggerInject(mApplicationComponent: ApplicationComponent) {
//        DaggerCoreComponent.builder().applicationComponent(BaseApplication.mApplicationComponent).build().inject(this)
    }

    override fun startEvents() {
        mMultipleStatusView?.showLoading()
        Handler().postDelayed({
            mMultipleStatusView?.showContent()
            val fragment = TestFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fl_container, fragment)
            transaction.commitNowAllowingStateLoss()
        }, 2000)

        val bean = ErrorBean()
        bean.msg = "Activity中的DataBinding测试"
        viewDataBinding?.bean = bean
//
        tv_txt.text = "测试ButterKnife成功,点我跳转"
//
        tv_txt.setOnClickListener {
            ARouter.getInstance().build("/base/act_testdagger").navigation()
//            ARouter.getInstance().build("/appmodule/test").navigation()
        }
    }
}