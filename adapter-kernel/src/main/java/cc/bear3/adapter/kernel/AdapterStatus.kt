package cc.bear3.adapter.kernel

enum class AdapterStatus {
        Null,               // 空，初始化的状态
        Loading,            // 加载中
        Empty,              // 空状态，显示该列表没有内容却有一个item展示
        Error,              // 错误状态
        Content             // 有数据的内容状态
    }