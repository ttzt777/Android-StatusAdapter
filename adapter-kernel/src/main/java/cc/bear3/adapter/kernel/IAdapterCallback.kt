package cc.bear3.adapter.kernel

/**
 * Description:
 * Author: TT
 * Since: 2020-02-27
 */
interface IAdapterCallback {
    /**
     * 当数据被移除空后，触发的重新加载
     * @return 期望展示的状态（建议Null/Empty/Loading）
     */
    fun onReLoad(): AdapterStatus
}