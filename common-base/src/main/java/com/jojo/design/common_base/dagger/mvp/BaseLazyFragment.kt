package com.jojo.design.common_base.dagger.mvp


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.jojo.design.common_base.bean.ErrorBean
import com.jojo.design.common_base.config.constants.BroadCastConstant
import com.jojo.design.common_ui.dialog.LoadingDialog
import com.jojo.design.common_ui.view.MultipleStatusView
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/5 2:57 PM
 *    desc   : Dagger-MVP-Fragment懒加载
 */
abstract class BaseLazyFragment<P : BaseContract.BasePresenter, M : BaseContract.BaseModel> : Fragment(), BaseContract.BaseView {
    protected lateinit var mLoadingDialog: LoadingDialog
    private var mIsBind: Boolean = false
    private var mIsRegisterReceiver = false
    protected var viewDataBinding: ViewDataBinding? = null
    protected lateinit var mContext: Context
    protected var mMultipleStatusView: MultipleStatusView? = null

    private var isFirstResume = true
    private var isFirstVisible = true
    private var isFirstInvisible = true
    private var isPrepared: Boolean = false

    @Inject
    @JvmField
    var mPresenter: P? = null

    @Inject
    @JvmField
    var mModel: M? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoadingDialog = LoadingDialog(mContext)
        //事件订阅
        if (isBindEventBus(mIsBind)) {
            EventBus.getDefault().register(this)
        }
        registerBroadCastReceiver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //根据子类布局自定义的区域show多状态布局
        mMultipleStatusView = MultipleStatusView(context)
        mPresenter?.attachViewModel(this, mModel!!)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPrepare()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
    }

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false
                initPrepare()
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false
            }
        }
    }

    @Synchronized private fun initPrepare() {
        if (!isPrepared) {
            isPrepared = true
        }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
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
                activity?.unregisterReceiver(broadcastReceiver)
            } catch (e: Exception) {
                Log.e("TAG","onDestroy",e)
            } finally {
                broadcastReceiver = null
            }
        }
    }

    /**
     * 发送一个广播
     * @param value
     */
    protected fun sendCommonBroadcast(value: Int) {
        sendBroadcast(requireActivity(), value)
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
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("TAG","sendBroadcast",e)
        }
    }

    //广播接收器
    private var broadcastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val info = activity?.packageManager?.getPackageInfo(requireActivity().packageName, 0)
                if (intent.action == info?.packageName + BroadCastConstant.BROADCAST_ADDRESS) {
                    val bundle = intent.extras
                    val i = bundle!!.getInt(BroadCastConstant.BROADCAST_INTENT)
                    onReceiveBroadcast(i, bundle)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("TAG","onReceive",e)
            }
        }
    }

    /**
     * 注册广播
     */
    private fun registerBroadCastReceiver() {
        try {
            val info = activity?.packageManager?.getPackageInfo(requireActivity().packageName, 0)
            activity?.registerReceiver(broadcastReceiver, IntentFilter(info?.packageName + BroadCastConstant.BROADCAST_ADDRESS))
            mIsRegisterReceiver = true
        } catch (e: Exception) {
            Log.e("TAG","onReceive",e)
        }
    }

    protected open fun onReceiveBroadcast(intent: Int, bundle: Bundle) {
    }

    open fun isBindEventBus(isBind: Boolean): Boolean {
        mIsBind = isBind
        return mIsBind
    }

    override fun showLoading() {
        mMultipleStatusView?.showLoading()
    }

    override fun showDialogLoading(msg: String) {
        if (!TextUtils.isEmpty(msg)) mLoadingDialog.setTitleText(msg).show() else mLoadingDialog.show()
    }

    override fun dismissDialogLoading() {
        mLoadingDialog.dismiss()
    }

    override fun showBusinessError(error: ErrorBean) {
        mMultipleStatusView?.showError()
    }

    override fun showException(error: ErrorBean) {
        mMultipleStatusView?.showNoNetwork()
    }
}