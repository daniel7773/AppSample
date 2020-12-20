package com.example.appsample.framework.presentation.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemViewBinder<M, in VH : RecyclerView.ViewHolder> : DiffUtil.ItemCallback<M>() {

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
    abstract fun getItemLayoutResource(): Int

    // Having these as non abstract because not all the viewBinders are required to implement them.
    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
}

typealias ItemBinder = BaseItemViewBinder<Any, RecyclerView.ViewHolder>

inline fun <reified T : ViewDataBinding> ViewGroup.inflate(
    @LayoutRes layout: Int,
    attachToRoot: Boolean = false
): T = DataBindingUtil.inflate<T>(
    LayoutInflater.from(context),
    layout,
    this,
    attachToRoot
)

abstract class MappableItemViewBinder<M, in VH : RecyclerView.ViewHolder>(
    val modelClazz: Class<out M>
) : BaseItemViewBinder<M, VH>()