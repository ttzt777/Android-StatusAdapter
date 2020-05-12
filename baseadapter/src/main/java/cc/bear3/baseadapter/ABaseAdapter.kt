package cc.bear3.baseadapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

private const val TYPE_DATA = 0x7FFF0000        // 数据默认
private const val TYPE_LOADING = 0x7FFF0010     // 加载
private const val TYPE_EMPTY = 0x7FFF0011       // 空状态
private const val TYPE_ERROR = 0x7FFF0012       // 错误状态
private const val TYPE_NO_MORE = 0x7FFF0013    // 没有更多 - 底部

private const val TYPE_HEADER_INIT_INDEX = 0x80000000
private const val TYPE_FOOTER_INIT_INDEX = 0xB0000000

/**
 * Description: RecyclerView 适配器基类
 * * 内容展示 *
 * -- Header... --
 * -- Content(Loading/Empty/Error/Content) --
 * -- Footer... --
 * -- NoMore --
 * Author: TT
 * Since: 2020-02-25
 */
abstract class ABaseAdapter<T, VH : AContentViewHolder> :
    RecyclerView.Adapter<AViewHolder>() {

    // 状态
    var status = Status.Null
        set(value) {
            if (field == value) {
                return
            }

            field = value
            notifyDataSetChanged()
        }

    var callback: IAdapterCallback? = null

    // 数据集合
    private val dataList = mutableListOf<T>()

    // Header View 集合
    private val mHeaderViews = SparseArray<View>()
    // Footer View 集合
    private val mFooterViews = SparseArray<View>()

    // 没有更多数据标记
    private var noMoreData = false

    // 用于界面上固定的头部或者顶部，与Item中的逻辑无关
    private var mHeaderTypeIndex = TYPE_HEADER_INIT_INDEX
    private var mFooterTypeIndex = TYPE_FOOTER_INIT_INDEX

    init {
        status = getInitStatus()
    }

    fun dataRefresh(targetList: List<T>?, noMoreData: Boolean = false) {
        dataList.clear()
        targetList?.let {
            dataList.addAll(it)
        }

        this.noMoreData = noMoreData

        onDataChanged()
        notifyDataSetChanged()
    }

    fun dataMore(targetList: List<T>?, noMoreData: Boolean = false) {
        if (targetList.isNullOrEmpty()) {
            return
        }

        dataList.addAll(targetList)

        this.noMoreData = noMoreData

        onDataChanged()
        notifyDataSetChanged()
    }

//    fun dataRemove(position: Int) {
//        if (position < 0 || position >= dataList.size) {
//            return
//        }
//
//        dataList.removeAt(position)
//        notifyItemRemoved(position)
//    }
//
//    fun dataRemove(data: T) {
//
//    }

    fun getData(position: Int): T {
        return dataList[position]
    }

    // <editor-folder desc="头部和底部Api">
    open fun addHeaderView(vararg views: View) {
        var notify = false
        for (view in views) {
            if (mHeaderViews.indexOfValue(view) == -1) {
                mHeaderViews.put(getHeaderTypeIndex(), view)
                notify = true
            }
        }
        if (notify) {
            notifyDataSetChanged()
        }
    }

    open fun addFooterView(vararg views: View) {
        var notify = false
        for (view in views) {
            if (mFooterViews.indexOfValue(view) == -1) {
                mFooterViews.put(getFooterTypeIndex(), view)
                notify = true
            }
        }
        if (notify) {
            notifyDataSetChanged()
        }
    }

    open fun removeHeaderView(view: View) {
        val key = mHeaderViews.indexOfValue(view)
        if (key != -1) {
            mHeaderViews.remove(key)
            notifyDataSetChanged()
        }
    }

    open fun removeAllHeaderView() {
        mHeaderViews.clear()
    }

    open fun removeFooterView(view: View) {
        val key = mHeaderViews.indexOfValue(view)
        if (key != -1) {
            mHeaderViews.remove(key)
            notifyDataSetChanged()
        }
    }

    open fun removeAllFooterView() {
        mFooterViews.clear()
    }

    open fun getHeaderViewSize(): Int {
        return mHeaderViews.size()
    }

    open fun getFooterViewSize(): Int {
        return mFooterViews.size()
    }
    // </editor-folder>

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when {
            mHeaderViews.get(viewType) != null -> {
                HeaderViewHolder(mHeaderViews.get(viewType))
            }
            mFooterViews.get(viewType) != null -> {
                FooterViewHolder(mFooterViews.get(viewType))
            }
            viewType == TYPE_LOADING -> onCreateLoadingViewHolder(inflater, parent)
            viewType == TYPE_EMPTY -> onCreateEmptyViewHolder(inflater, parent)
            viewType == TYPE_ERROR -> onCreateLErrorViewHolder(inflater, parent)
            viewType == TYPE_NO_MORE -> onCreateNoMoreViewHolder(inflater, parent)
            else -> onCreateCustomViewHolder(inflater, parent, viewType)
        }
    }

    final override fun onBindViewHolder(holder: AViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
            }
            is FooterViewHolder -> {
            }
            is ALoadingViewHolder -> onBindLoadingViewHolder(holder)
            is AEmptyViewHolder -> onBindEmptyViewHolder(holder)
            is AErrorViewHolder -> onBindErrorViewHolder(holder)
            is ANoMoreViewHolder -> onBindNoMoreViewHolder(holder)
            is AContentViewHolder -> onBindCustomViewHolder(holder as VH, position)
        }
    }

    final override fun getItemCount(): Int {
        return getHeaderViewSize() + getContentCount() + getFooterViewSize() + getNoMoreCount()
    }

    final override fun getItemViewType(position: Int): Int {
        return when {
            isHeaderPosition(position) -> {
                mHeaderViews.keyAt(position)
            }
            isFooterPosition(position) -> {
                mFooterViews.keyAt(position)
            }
            else -> {
                when (status) {
                    Status.Null -> TYPE_DATA     // 不会出现这种类型
                    Status.Loading -> TYPE_LOADING
                    Status.Empty -> TYPE_EMPTY
                    Status.Error -> TYPE_ERROR
                    Status.Content -> {
                        return if (noMoreData && position == itemCount - 1) {
                            TYPE_NO_MORE
                        } else {
                            val realPos = position - getHeaderViewSize()
                            val type = getCustomViewType(realPos)

                            check(type in 0..TYPE_DATA) { IllegalStateException("View type must in 0 .. $TYPE_DATA") }

                            type
                        }
                    }
                }
            }
        }
    }

    protected abstract fun onCreateCustomViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): VH

    protected abstract fun onBindCustomViewHolder(holder: VH, position: Int)

    protected abstract fun onCreateLoadingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ALoadingViewHolder

    protected abstract fun onCreateEmptyViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AEmptyViewHolder

    protected abstract fun onCreateLErrorViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AErrorViewHolder

    protected abstract fun onCreateNoMoreViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ANoMoreViewHolder

    protected abstract fun onBindLoadingViewHolder(holder: ALoadingViewHolder)

    protected abstract fun onBindEmptyViewHolder(holder: AEmptyViewHolder)

    protected abstract fun onBindErrorViewHolder(holder: AErrorViewHolder)

    protected abstract fun onBindNoMoreViewHolder(holder: ANoMoreViewHolder)

    protected open fun getCustomItemCount(): Int {
        return dataList.size
    }

    protected open fun getCustomViewType(position: Int): Int {
        return TYPE_DATA
    }

    protected open fun getInitStatus(): Status {
        return Status.Loading
    }

    private fun onDataChanged() {
        status = if (getCustomItemCount() > 0) {
            Status.Content
        } else {
            if (callback != null) {
                callback!!.onReLoad()
                Status.Loading
            } else {
                Status.Empty
            }
        }
    }

    /**
     * 获取中间内容（Content/Loading/Error/Empty）的item个数
     */
    private fun getContentCount(): Int {
        var count = getCustomItemCount()

        return if (count == 0) {
            when (status) {
                Status.Loading, Status.Error, Status.Empty -> ++count
                else -> count
            }
        } else {
            count
        }
    }

    /**
     * 获取底部NoMore的Item个数
     */
    private fun getNoMoreCount(): Int {
        return if (noMoreData && status == Status.Content) {
            1
        } else {
            0
        }
    }

    private fun getHeaderTypeIndex(): Int {
        mHeaderTypeIndex++
        if (mHeaderTypeIndex >= TYPE_FOOTER_INIT_INDEX) {
            mHeaderTypeIndex = TYPE_HEADER_INIT_INDEX
        }
        return mHeaderTypeIndex.toInt()
    }

    private fun getFooterTypeIndex(): Int {
        mFooterTypeIndex++
        if (mFooterTypeIndex >= 0xFFFFFFFF) {
            mFooterTypeIndex = TYPE_FOOTER_INIT_INDEX
        }
        return mFooterTypeIndex.toInt()
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < getHeaderViewSize()
    }

    private fun isFooterPosition(position: Int): Boolean {
        val footerIndexStart = getHeaderViewSize() + getContentCount()
        val footerIndexEnd = footerIndexStart + getFooterViewSize() - 1
        return position in footerIndexStart..footerIndexEnd
    }

    enum class Status {
        Null,               // 空，初始化的状态
        Loading,            // 加载中
        Empty,              // 空状态，显示该列表没有内容却有一个item展示
        Error,              // 错误状态
        Content             // 有数据的内容状态
    }
}