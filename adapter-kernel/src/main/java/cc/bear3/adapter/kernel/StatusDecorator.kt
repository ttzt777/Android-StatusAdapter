package cc.bear3.adapter.kernel

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 为BaseStatusAdapter添加默认方法
 * @author TT
 * @since 2022-2-18
 */
object StatusDecorator {

    private lateinit var statusProxy: StatusProxy

    fun init(target: StatusProxy) {
        this.statusProxy = target
    }

    fun proxy(): StatusProxy {
        if (!this::statusProxy.isInitialized) {
            statusProxy = object : StatusProxy {}
        }

        return statusProxy
    }

    interface StatusProxy {
        fun onCreateEmptyViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): AEmptyViewHolder {
            return BaseStatusAdapter.EmptyViewHolder(parent.inflate(R.layout.adapter_empty))
        }

        fun onCreateLErrorViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): AErrorViewHolder {
            return BaseStatusAdapter.ErrorViewHolder(parent.inflate(R.layout.adapter_empty))
        }

        fun onCreateLoadingViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): ALoadingViewHolder {
            return BaseStatusAdapter.LoadingViewHolder(parent.inflate(R.layout.adapter_loading))
        }

        fun onCreateNoMoreViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): ANoMoreViewHolder {
            return BaseStatusAdapter.NoMoreViewHolder(parent.inflate(R.layout.adapter_no_more))
        }

        fun onBindEmptyViewHolder(holder: AEmptyViewHolder) {

        }

        fun onBindErrorViewHolder(holder: AErrorViewHolder) {

        }

        fun onBindLoadingViewHolder(holder: ALoadingViewHolder) {

        }

        fun onBindNoMoreViewHolder(holder: ANoMoreViewHolder) {

        }
    }
}