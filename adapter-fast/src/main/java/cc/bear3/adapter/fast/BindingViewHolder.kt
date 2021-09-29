package cc.bear3.adapter.fast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cc.bear3.adapter.kernal.AContentViewHolder

open class BindingViewHolder<VB : ViewBinding>(
    parent: ViewGroup,
    inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    val binding: VB = inflate(LayoutInflater.from(parent.context), parent, false)
) : AContentViewHolder(binding.root)