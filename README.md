[![](https://jitpack.io/v/ttzt777/Android-StatusAdapter.svg)](https://jitpack.io/#ttzt777/Android-StatusAdapter)
## 引用方式
- 在项目根目录的build.gradle文件中添加
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
- 对应module添加依赖
```groovy
    dependencies {
        implementation 'com.github.ttzt777.Android-StatusAdapter:adapter-kernel:1.0.3'
        implementation 'com.github.ttzt777.Android-StatusAdapter:adapter-fast:1.0.3'
    }
```
### **adapter-kernel**
- 支持无数据、空状态、加载状态、错误状态、正常数据状态五种
- 支持添加及移除Header、Footer
- 支持正常状态下数据被移除后的回调
- 支持全局设置和单独设置每个Adapter的各种状态显示样式
### **adapter-fast**
- 支持快速单对象适配器，无需继承直接使用SingleFastAdapter即可快速实现列表
- 支持多对象适配器，数据类需要实现IMultiData接口，快速实现多种ViewType