package cc.bear3.adapter.fast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cc.bear3.adapter.kernel.AStatusAdapter

/**
 *
 * @author TT
 * @since 2021-6-2
 */
abstract class ASingleFastAdapter<T, VB : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    AStatusAdapter<T, BindingViewHolder<VB>>() {
    final override fun onBindCustomViewHolder(holder: BindingViewHolder<VB>, position: Int) {
        convert(holder.binding, getData(position))
    }

    final override fun onCreateCustomViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<VB> {
        return BindingViewHolder(parent, inflate)
    }

    abstract fun convert(binding: VB, data: T)
}