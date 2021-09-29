package cc.bear3.adapter.kernal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author TT
 * @since 2021-9-2
 */

fun ViewGroup.inflate(resId: Int): View {
    return LayoutInflater.from(context).inflate(resId, this, false)
}