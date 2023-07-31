package com.jojo.design.module_core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jojo.design.common_base.dagger.mvp.BaseContract
import com.jojo.design.common_base.dagger.mvp.BaseLazyFragment

/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/12/10 9:55 PM
 *    desc   :
 */
class ADA_PagerDesignerType<P : BaseContract.BasePresenter, M : BaseContract.BaseModel> constructor(
    fm: FragmentManager,
    fragmentList: List<BaseLazyFragment<P, M>>
) : FragmentStatePagerAdapter(fm) {
    var mFragmentList = ArrayList<BaseLazyFragment<P, M>>()

    init {
        mFragmentList = fragmentList as ArrayList<BaseLazyFragment<P, M>>
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}