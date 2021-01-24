package com.example.appsample.framework.base.presentation.delegateadapter.separators

import android.util.Log
import com.example.appsample.databinding.DividerProfileBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DividerAdapterDelegate : ViewBindingDelegateAdapter<Divider, DividerProfileBinding>(
    DividerProfileBinding::inflate
) {

    override fun DividerProfileBinding.onBind(item: Divider) {}

    override fun isForViewType(item: Any): Boolean = item is Divider

    override fun Divider.getItemId(): Any = id

    override fun DividerProfileBinding.onRecycled() {}

    override fun DividerProfileBinding.onAttachedToWindow() {
        Log.d(DividerAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun DividerProfileBinding.onDetachedFromWindow() {
        Log.d(DividerAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}

// id дивайдерам нужен чтобы диффер с ними правильно работал
class Divider(id: String?) : AdapterElement(id ?: "divider") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Divider
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}