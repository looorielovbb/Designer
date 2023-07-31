package com.jojo.design.common_base.dagger.mvp.databinding

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.jojo.design.common_base.R
import com.jojo.design.common_base.bean.ErrorBean
import com.jojo.design.common_base.config.constants.BroadCastConstant
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.utils.StatusBarHelper
import com.jojo.design.common_ui.dialog.LoadingDialog
import com.jojo.design.common_ui.view.MultipleStatusView
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/4 9:21 PM
 *    desc   : Dagger2_MVP-Activity的基类 (Activity动画、支持DataBinding、事件订阅EventBus/广播、状态栏、ButterKnife，多状态View切换)
 */
abstract class BaseDBActivity<P : BaseContract.BasePresenter, M : BaseContract.BaseModel, DB : ViewDataBinding> :
    AppCompatActivity(), BaseContract.BaseView {
    @Inject
    @JvmField
    var mPresenter: P? = null

    @Inject
    @JvmField
    var mModel: M? = null

    private lateinit var mContext: Context
    protected var viewDataBinding: DB? = null
    private var mIsBind: Boolean = false
    private var mTransitionMode = TransitionMode.RIGHT
    private var mIsRegisterReceiver = false
    private lateinit var mLoadingDialog: LoadingDialog
    protected var mMultipleStatusView:MultipleStatusView? = null

    /**
     * OverridePendingTransition
     */
    enum class TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE, ZOOM, NOON
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mLoadingDialog = LoadingDialog(this)
        //根据子类布局自定义的区域show多状态布局
        //如果子类返回null,则处理成showloading为整个布局范围
        mPresenter?.attachViewModel(this, mModel!!)

        //Activity默认动画为右进右出
        when (getOverridePendingTransitionMode(mTransitionMode)) {
            TransitionMode.LEFT -> overridePendingTransition(R.anim.left_in, R.anim.left_out)
            TransitionMode.RIGHT -> overridePendingTransition(R.anim.enter_trans, R.anim.exit_scale)
            TransitionMode.TOP -> overridePendingTransition(R.anim.top_in, R.anim.top_out)
            TransitionMode.BOTTOM -> overridePendingTransition(R.anim.bottom_in, 0)
            TransitionMode.SCALE -> overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
            TransitionMode.FADE -> overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            TransitionMode.ZOOM -> overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
            else -> {}
        }

        //事件订阅
        if (isBindEventBus(mIsBind)) {
            EventBus.getDefault().register(this)
        }
        registerBroadCastReceiver()
        //设置沉浸式状态栏
        StatusBarHelper.setStatusBar(
            this,
            useThemeStatusBarColor = false,
            isStatusBarLightMode = true
        )
    }

    override fun onStart() {
        super.onStart()
        mMultipleStatusView = MultipleStatusView(mContext)
    }

    /**
     * 发送一个广播
     * @param value
     */
    protected fun sendCommonBroadcast(value: Int) {
        sendBroadcast(this, value)
    }

    /**
     * 发送一个广播
     * @param value
     */
    open fun sendBroadcast(context: Context, value: Int) {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            val intent = Intent()
            intent.action = info.packageName + BroadCastConstant.BROADCAST_ADDRESS
            intent.putExtra(BroadCastConstant.BROADCAST_INTENT, value)
            context.sendBroadcast(intent)
        } catch (_: PackageManager.NameNotFoundException) {
        }
    }

    //广播接收器
    private var broadcastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val info = packageManager.getPackageInfo(packageName, 0)

                if (intent.action == info.packageName + BroadCastConstant.BROADCAST_ADDRESS) {
                    val bundle = intent.extras
                    val i = bundle!!.getInt(BroadCastConstant.BROADCAST_INTENT)
                    this@BaseDBActivity.onReceiveBroadcast(i, bundle)
                }
            } catch (_: PackageManager.NameNotFoundException) {
            }
        }
    }

    /**
     * 注册广播
     */
    private fun registerBroadCastReceiver() {
        try {
            val info = packageManager.getPackageInfo(packageName, 0)
            registerReceiver(
                broadcastReceiver,
                IntentFilter(info.packageName + BroadCastConstant.BROADCAST_ADDRESS)
            )
            mIsRegisterReceiver = true
        } catch (_: Exception) {
        }
    }

    protected open fun onReceiveBroadcast(intent: Int, bundle: Bundle) {
        if (intent == BroadCastConstant.LOGOUT) {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        when (getOverridePendingTransitionMode(mTransitionMode)) {
            TransitionMode.LEFT -> overridePendingTransition(R.anim.left_in, R.anim.left_out)
            TransitionMode.RIGHT -> overridePendingTransition(R.anim.enter_scale, R.anim.exit_trans)
            TransitionMode.TOP -> overridePendingTransition(R.anim.top_in, R.anim.top_out)
            TransitionMode.BOTTOM -> overridePendingTransition(0, R.anim.bottom_out)
            TransitionMode.SCALE -> overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
            TransitionMode.FADE -> overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            TransitionMode.ZOOM -> overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mIsBind) {
            EventBus.getDefault().unregister(this)
        }
        if (mIsRegisterReceiver && broadcastReceiver != null) {
            try {
                mIsRegisterReceiver = false
                this.unregisterReceiver(broadcastReceiver)
            } catch (_: Exception) {
            } finally {
                broadcastReceiver = null
            }
        }
        mMultipleStatusView = null
        mPresenter?.detachView()
        mPresenter?.onDestroy()

    }

    override fun showLoading() {
        mMultipleStatusView?.showLoading()
    }


    fun isBindEventBus(isBind: Boolean): Boolean {
        mIsBind = isBind
        return mIsBind
    }

    open fun getOverridePendingTransitionMode(transitionMode: BaseDBActivity.TransitionMode): BaseDBActivity.TransitionMode {
        mTransitionMode = transitionMode
        return mTransitionMode
    }

    override fun showDialogLoading(msg: String) {
        mLoadingDialog.setTitleText(msg).show()
    }

    override fun dismissDialogLoading() {
        mLoadingDialog.dismiss()
    }

    override fun showBusinessError(error: ErrorBean) {
//        mMultipleStatusView?.showError()
    }

    override fun showException(error: ErrorBean) {
//        mMultipleStatusView?.showNoNetwork()
    }

}