package com.jojo.design.common_base.dagger.component

import androidx.appcompat.app.AppCompatActivity
import com.jojo.design.common_base.component.ApplicationComponent
import dagger.Component


/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(dependencies = [(ApplicationComponent::class)])
interface HttpComponent {

    fun inject(activity: AppCompatActivity)

}
