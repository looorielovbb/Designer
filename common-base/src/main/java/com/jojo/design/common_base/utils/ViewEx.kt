@file:Suppress("unused")

package com.jojo.design.common_base.utils

import android.view.View
import androidx.viewbinding.ViewBinding
import com.jojo.design.common_base.R

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> View.getBinding(bind: (View) -> VB): VB =
    getTag(0x11) as? VB ?: bind(this).also { setTag(0x11, it) }

