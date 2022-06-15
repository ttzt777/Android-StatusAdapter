package cc.bear3.adapter.kernel

import androidx.annotation.DrawableRes

class AdapterStatus {
    sealed interface Status

    // 空，初始化的状态
    object Null : Status

    // 加载中
    object Loading : Status

    // 空状态
    object Empty : Status

    // 错误状态
    class Error(
        @DrawableRes drawableResId: Int = 0,
        message: String? = null,
        obj1: Any? = null,
        obj2: Any? = null
    ) : Status

    // 有数据的内容状态
    object Content : Status
}