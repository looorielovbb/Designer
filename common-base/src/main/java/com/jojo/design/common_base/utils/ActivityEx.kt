package com.jojo.design.common_base.utils


import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import kotlin.LazyThreadSafetyMode.NONE

fun <VB : ViewBinding> ComponentActivity.binding(
    inflate: (LayoutInflater) -> VB,
    setContentView: Boolean = true
) = lazy(NONE) {
    inflate(layoutInflater).also { binding ->
        if (setContentView) setContentView(binding.root)
    }
}
