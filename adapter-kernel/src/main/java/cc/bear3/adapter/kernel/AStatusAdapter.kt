package cc.bear3.adapter.kernel

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
private const val TYPE_NO_MORE = 0x7FFF0013     // 没有更多 - 底部

private const val TYPE_HEADER_INIT_INDEX = 0x80000000   // Header索引（viewType）起始值
private const val TYPE_FOOTER_INIT_INDEX = 0xB0000000   // Footer索引（viewType）起始值

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
@Suppress("MemberVisibilityCanBePrivate", "unused", "UNCHECKED_CAST")
abstract class AStatusAdapter<T, VH : AContentViewHolder> :
    RecyclerView.Adapter<AViewHolder>(), IStatusDecorator {

    // 状态
    var status: AdapterStatus.Status = AdapterStatus.Null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var callback: IAdapterCallback? = null

    // 数据集合
    val dataList by lazy {
        mutableListOf<T>()
    }

    // Header View 集合
    private val mHeaderViews by lazy {
        SparseArray<View>()
    }

    // Footer View 集合
    private val mFooterViews by lazy {
        SparseArray<View>()
    }

    // 没有更多数据标记
    private var noMoreData = false

    // 用于界面上固定的头部或者顶部，与Item中的逻辑无关
    private var mHeaderTypeIndex = TYPE_HEADER_INIT_INDEX
    private var mFooterTypeIndex = TYPE_FOOTER_INIT_INDEX

    /**
     * 刷新数据
     */
    @JvmOverloads
    fun dataRefresh(targetList: List<T>?, noMoreData: Boolean = false) {
        dataList.clear()
        targetList?.let {
            dataList.addAll(it)
        }

        this.noMoreData = noMoreData

        onDataChanged()
    }

    /**
     * 更多数据，拼接方式
     */
    @JvmOverloads
    fun dataMore(targetList: List<T>?, noMoreData: Boolean = false) {
        targetList?.let {
            dataList.addAll(it)
        }

        this.noMoreData = noMoreData

        onDataChanged()
    }

    /**
     * 获取指定position的数据
     */
    fun getData(position: Int): T {
        return dataList[position]
    }

    // <editor-folder desc="notifyxxx方法集，代替notifyDataSetChanged等">
    /**
     * 通知全局数据发生改变
     */
    fun notifyChanged() {
        onDataChanged()
    }

    /**
     * 通知部分数据发生改变
     * 此时一定是在Content模式下，无需校验status
     */
    @JvmOverloads
    fun notifyChanged(positionStart: Int, itemCount: Int = 1) {
        notifyItemRangeChanged(positionStart + getHeaderViewSize(), itemCount)
    }

    /**
     * 通知添加部分Item
     * 此时状态可能发生改变
     */
    @JvmOverloads
    fun notifyInsert(positionStart: Int, itemCount: Int = 1) {
        if (status is AdapterStatus.Content) {
            // 当前是Content状态，通知增加数据即可
            notifyItemRangeInserted(positionStart + getHeaderViewSize(), itemCount)
        } else {
            onDataChanged()
        }
    }

    /**
     * 通知移除部分Item
     * 此时状态一定为Content，变更后可能发生改变
     */
    @JvmOverloads
    fun notifyRemoved(positionStart: Int, itemCount: Int = 1) {
        if (dataList.size == 0) {
            // 被移除后的dataSize变为0了
            status = callback?.onReLoad() ?: AdapterStatus.Empty
        } else {
            notifyItemRangeRemoved(positionStart + getHeaderViewSize(), itemCount)
        }
    }

    /**
     * 通知item发生交换
     */
    fun notifyMoved(fromPosition: Int, toPosition: Int) {
        val headerSize = getHeaderViewSize()
        notifyItemMoved(fromPosition + headerSize, toPosition + headerSize)
    }
    // </editor-folder>

    /**
     * 根据adapterPosition获取数据的position
     */
    fun getDataPosition(adapterPos: Int): Int {
        return adapterPos - getHeaderViewSize()
    }

    // <editor-folder desc="弃用">
    /**
     * 根据数据的position获取adapterPosition
     */
    @Deprecated("添加notifyxxx方法集后，不会有此需求了", ReplaceWith("使用notifyxxx方法集代替Adapter的原生notifyItemInsert等"))
    fun getRealPosition(position: Int): Int {
        return position + getHeaderViewSize()
    }

    /**
     * 检查数据是否为空，用于itemRemove之后触发页面状态及reload
     */
    @Deprecated("添加notifyxxx方法集后，不会有此需求了", ReplaceWith("使用notifyxxx方法集代替Adapter的原生notifyItemRemoved等"))
    fun checkDataEmpty() {
        if (getCustomItemCount() > 0) {
            return
        }

        status = callback?.onReLoad() ?: AdapterStatus.Empty
    }
    // </editor-folder>

    // <editor-folder desc="头部和底部Api">
    /**
     * 添加HeaderView
     */
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

    /**
     * 添加FooterView
     */
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

    /**
     * 移除指定HeaderView
     */
    open fun removeHeaderView(view: View) {
        val key = mHeaderViews.indexOfValue(view)
        if (key != -1) {
            mHeaderViews.remove(key)
            notifyDataSetChanged()
        }
    }

    /**
     * 移除所有HeaderView
     */
    open fun removeAllHeaderView() {
        mHeaderViews.clear()
        notifyDataSetChanged()
    }

    /**
     * 移除指定的FooterView
     */
    open fun removeFooterView(view: View) {
        val key = mHeaderViews.indexOfValue(view)
        if (key != -1) {
            mHeaderViews.remove(key)
            notifyDataSetChanged()
        }
    }

    /**
     * 移除所有FooterView
     */
    open fun removeAllFooterView() {
        mFooterViews.clear()
        notifyDataSetChanged()
    }

    /**
     * 获取HeaderView的个数
     */
    open fun getHeaderViewSize(): Int {
        return mHeaderViews.size()
    }

    /**
     * 获取FooterView的个数
     */
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
            is AContentViewHolder -> onBindCustomViewHolder(holder as VH, getDataPosition(position))
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
                    is AdapterStatus.Null -> TYPE_DATA     // 不会出现这种类型
                    is AdapterStatus.Loading -> TYPE_LOADING
                    is AdapterStatus.Empty -> TYPE_EMPTY
                    is AdapterStatus.Error -> TYPE_ERROR
                    is AdapterStatus.Content -> {
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

    /**
     * 创建实际数据的VH
     */
    protected abstract fun onCreateCustomViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): VH

    /**
     * 绑定实际数据的VH
     */
    protected abstract fun onBindCustomViewHolder(holder: VH, position: Int)

    /**
     * 获取实际数据的个数
     */
    protected open fun getCustomItemCount(): Int {
        return dataList.size
    }

    /**
     * 获取手机数据的viewType
     */
    protected open fun getCustomViewType(position: Int): Int {
        return TYPE_DATA
    }

    /**
     * 数据发生改变后状态改变
     */
    private fun onDataChanged() {
        status = if (getCustomItemCount() > 0) {
            AdapterStatus.Content
        } else {
            AdapterStatus.Empty
        }
    }

    /**
     * 获取中间内容（Content/Loading/Error/Empty）的item个数
     */
    private fun getContentCount(): Int {
        var count = getCustomItemCount()

        return if (count == 0) {
            when (status) {
                is AdapterStatus.Loading, is AdapterStatus.Error, is AdapterStatus.Empty -> ++count
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
        return if (noMoreData && status == AdapterStatus.Content) {
            1
        } else {
            0
        }
    }

    /**
     * 获取HeaderView的索引（viewType）
     */
    private fun getHeaderTypeIndex(): Int {
        mHeaderTypeIndex++
        if (mHeaderTypeIndex >= TYPE_FOOTER_INIT_INDEX) {
            mHeaderTypeIndex = TYPE_HEADER_INIT_INDEX
        }
        return mHeaderTypeIndex.toInt()
    }

    /**
     * 获取FooterView的索引（viewType）
     */
    private fun getFooterTypeIndex(): Int {
        mFooterTypeIndex++
        if (mFooterTypeIndex >= 0xFFFFFFFF) {
            mFooterTypeIndex = TYPE_FOOTER_INIT_INDEX
        }
        return mFooterTypeIndex.toInt()
    }

    /**
     * 判断adapterPosition是否为HeaderView的position
     */
    private fun isHeaderPosition(adapterPos: Int): Boolean {
        return adapterPos < getHeaderViewSize()
    }

    /**
     * 判断adapterPosition是否为FooterView的position
     */
    private fun isFooterPosition(adapterPos: Int): Boolean {
        val footerIndexStart = getHeaderViewSize() + getContentCount()
        val footerIndexEnd = footerIndexStart + getFooterViewSize() - 1
        return adapterPos in footerIndexStart..footerIndexEnd
    }
}