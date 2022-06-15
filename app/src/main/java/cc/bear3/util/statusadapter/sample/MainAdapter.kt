package cc.bear3.util.statusadapter.sample

import cc.bear3.adapter.fast.ASingleFastAdapter
import cc.bear3.adapter.kernel.IDefaultStatusDecorator
import cc.bear3.util.statusadapter.sample.databinding.ItemMainBinding

/**
 *
 * @author TT
 * @since 2022-6-15
 */
class MainAdapter : ASingleFastAdapter<ItemData, ItemMainBinding>(ItemMainBinding::inflate),
    IDefaultStatusDecorator {
    override fun convert(binding: ItemMainBinding, data: ItemData) {
        binding.textView.text = data.title
        binding.textView.setBackgroundResource(
            if (data.status % 3 == 2) {
                android.R.color.holo_blue_dark
            } else if (data.status % 3 == 1) {
                android.R.color.holo_orange_dark
            } else {
                android.R.color.holo_purple
            }
        )
        binding.textView.setOnClickListener {
            data.status = data.status + 1
            notifyChanged(dataList.indexOf(data))
        }
        binding.textView.setOnLongClickListener {
            val position = dataList.indexOf(data)
            dataList.remove(data)
//            notifyChanged()
            notifyRemoved(position)
            false
        }
    }
}