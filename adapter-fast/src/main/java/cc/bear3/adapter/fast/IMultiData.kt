package cc.bear3.adapter.fast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 *
 * @author TT
 * @since 2021-6-2
 */
interface IMultiData {
    fun getViewBindingFun() : (LayoutInflater, ViewGroup?, Boolean) -> ViewBinding
}