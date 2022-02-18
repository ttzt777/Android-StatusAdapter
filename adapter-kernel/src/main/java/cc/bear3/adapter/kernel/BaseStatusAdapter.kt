package cc.bear3.adapter.kernel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Description:
 * Author: TT
 */
abstract class BaseStatusAdapter<T, VH : AContentViewHolder> :
    AStatusAdapter<T, VH>() {

    init {
        status = AdapterStatus.Loading
    }

    override fun onCreateEmptyViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AEmptyViewHolder {
        return StatusDecorator.proxy().onCreateEmptyViewHolder(inflater, parent)
    }

    override fun onCreateLErrorViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AErrorViewHolder {
        return StatusDecorator.proxy().onCreateLErrorViewHolder(inflater, parent)
    }

    override fun onCreateLoadingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ALoadingViewHolder {
        return StatusDecorator.proxy().onCreateLoadingViewHolder(inflater, parent)
    }

    override fun onCreateNoMoreViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ANoMoreViewHolder {
        return StatusDecorator.proxy().onCreateNoMoreViewHolder(inflater, parent)
    }

    override fun onBindEmptyViewHolder(holder: AEmptyViewHolder) {
        StatusDecorator.proxy().onBindEmptyViewHolder(holder)
    }

    override fun onBindErrorViewHolder(holder: AErrorViewHolder) {
        StatusDecorator.proxy().onBindErrorViewHolder(holder)
    }

    override fun onBindLoadingViewHolder(holder: ALoadingViewHolder) {
        StatusDecorator.proxy().onBindLoadingViewHolder(holder)
    }

    override fun onBindNoMoreViewHolder(holder: ANoMoreViewHolder) {
        StatusDecorator.proxy().onBindNoMoreViewHolder(holder)
    }

    open class EmptyViewHolder(view: View) : AEmptyViewHolder(view)

    open class LoadingViewHolder(view: View) : ALoadingViewHolder(view)

    open class ErrorViewHolder(view: View) : AErrorViewHolder(view)

    open class NoMoreViewHolder(view: View) : ANoMoreViewHolder(view)

    open class ContentViewHolder(parent: ViewGroup, resId: Int) :
        AContentViewHolder(parent.inflate(resId))
}