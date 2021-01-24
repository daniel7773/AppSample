package com.example.appsample.framework.base.presentation.delegateadapter.separators

import android.util.Log
import com.example.appsample.databinding.EmptySpaceBinding
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.AdapterElement
import com.example.appsample.framework.base.presentation.delegateadapter.delegate.ViewBindingDelegateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EmptySpaceAdapterDelegate : ViewBindingDelegateAdapter<EmptySpace, EmptySpaceBinding>(
    EmptySpaceBinding::inflate
) {

    override fun EmptySpaceBinding.onBind(item: EmptySpace) {}

    override fun isForViewType(item: Any): Boolean = item is EmptySpace

    override fun EmptySpace.getItemId(): Any = id

    override fun EmptySpaceBinding.onRecycled() {}

    override fun EmptySpaceBinding.onAttachedToWindow() {
        Log.d(EmptySpaceAdapterDelegate::class.java.simpleName, "onAttachedToWindow")
    }

    override fun EmptySpaceBinding.onDetachedFromWindow() {
        Log.d(EmptySpaceAdapterDelegate::class.java.simpleName, "onDetachedFromWindow")
    }
}

// id пробелам нужен чтобы диффер с ними правильно работал
class EmptySpace(id: String) : AdapterElement(id) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EmptySpace
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
