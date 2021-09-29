package cc.bear3.adapter.kernal

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
        return EmptyViewHolder(parent.inflate(R.layout.adapter_empty))
    }

    override fun onCreateLErrorViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AErrorViewHolder {
        return ErrorViewHolder(parent.inflate(R.layout.adapter_empty))
    }

    override fun onCreateLoadingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ALoadingViewHolder {
        return LoadingViewHolder(parent.inflate(R.layout.adapter_loading))
    }

    override fun onCreateNoMoreViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ANoMoreViewHolder {
        return NoMoreViewHolder(parent.inflate(R.layout.adapter_no_more))
    }

    override fun onBindEmptyViewHolder(holder: AEmptyViewHolder) {

    }

    override fun onBindErrorViewHolder(holder: AErrorViewHolder) {

    }

    override fun onBindLoadingViewHolder(holder: ALoadingViewHolder) {

    }

    override fun onBindNoMoreViewHolder(holder: ANoMoreViewHolder) {

    }

    open class EmptyViewHolder(view: View) : AEmptyViewHolder(view)

    open class LoadingViewHolder(view: View) : ALoadingViewHolder(view)

    open class ErrorViewHolder(view: View) : AErrorViewHolder(view)

    open class NoMoreViewHolder(view: View) : ANoMoreViewHolder(view)

    open class ContentViewHolder(parent: ViewGroup, resId: Int) :
        AContentViewHolder(parent.inflate(resId))
}