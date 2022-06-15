package cc.bear3.util.statusadapter.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cc.bear3.adapter.fast.ASingleFastAdapter
import cc.bear3.util.statusadapter.sample.databinding.ActivityMainBinding
import cc.bear3.util.statusadapter.sample.databinding.ItemMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        MainAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter

        adapter.dataRefresh(createDataList())
    }

    private fun createDataList() : MutableList<ItemData> {
        val result = mutableListOf<ItemData>()
        repeat(1) {
            result.add(ItemData((it + 1).toString()))
        }
        return result
    }
}
