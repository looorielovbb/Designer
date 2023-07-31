package com.jojo.design.module_core.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseFragment
import com.jojo.design.module_core.R
import com.jojo.design.module_core.adapter.TextTagsAdapter
import com.jojo.design.module_core.databinding.FraDiscorveryBinding

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/7 11:34 AM
 *    desc   : 发现(供拓展的学习模块)
 */
class DiscoveryFragment : BaseFragment<BaseContract.BasePresenter, BaseContract.BaseModel>() {
    private var mTitle: String? = null
    private var binding: FraDiscorveryBinding? = null

    companion object {
        fun getInstance(title: String): DiscoveryFragment {
            var fragment = DiscoveryFragment()
            var bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fra_discorvery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FraDiscorveryBinding.bind(view)
        initCloudView()
    }

    private fun initCloudView() {
        binding!!.tagCloud.setBackgroundColor(
            ContextCompat.getColor(
                mContext,
                R.color.color_ffffff
            )
        );
        val data = arrayListOf(
            "开眼视频",
            "Kotlin",
            "组件化",
            "MVP",
            "Retrofit",
            "Rxjava",
            "开眼视频",
            "好好学习",
            "进入开眼视频",
            "有趣的内容",
            "组件化",
            "Kotlin",
            "Dagger2",
            "开眼视频",
            "Room数据库",
            "Retrofit",
            "开眼视频",
            "沉浸式状态栏",
            "5.0新特性",
            "Rxjava",
            "烟火里的尘埃",
            "进入开眼视频",
            "猪年快乐",
            "加油努力",
            "进入开眼视频",
            "猪事顺利",
            "ARouter"
        )
        val tagsAdapter = TextTagsAdapter(data)//new String[20]-*arrayOfNulls(20)
        binding!!.tagCloud.setAdapter(tagsAdapter)
    }
}