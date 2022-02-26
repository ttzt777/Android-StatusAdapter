package cc.bear3.adapter.kernel

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 适配器多状态的规范
 */
interface IStatusDecorator {
    /**
     * 创建Loading状态的VH
     */
    fun onCreateLoadingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ALoadingViewHolder

    /**
     * 绑定Loading状态的VH
     */
    fun onBindLoadingViewHolder(holder: ALoadingViewHolder)

    /**
     * 创建Empty状态的VH
     */
    fun onCreateEmptyViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AEmptyViewHolder

    /**
     * 绑定Empty状态的VH
     */
    fun onBindEmptyViewHolder(holder: AEmptyViewHolder)


    /**
     * 创建Error状态的VH
     */
    fun onCreateLErrorViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AErrorViewHolder

    /**
     * 绑定Error状态的VH
     */
    fun onBindErrorViewHolder(holder: AErrorViewHolder)

    /**
     * 创建NoMore状态的VH
     */
    fun onCreateNoMoreViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ANoMoreViewHolder

    /**
     * 绑定NoMore状态的VH
     */
    fun onBindNoMoreViewHolder(holder: ANoMoreViewHolder)
}