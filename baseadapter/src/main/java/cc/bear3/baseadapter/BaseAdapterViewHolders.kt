package cc.bear3.baseadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:
 * Author: TT
 * Since: 2020-02-27
 */
sealed class AViewHolder(view : View) : RecyclerView.ViewHolder(view)

abstract class ALoadingViewHolder(view: View) : AViewHolder(view)

abstract class AEmptyViewHolder(view: View) : AViewHolder(view)

abstract class AErrorViewHolder(view: View) : AViewHolder(view)

abstract class ANoMoreViewHolder(view: View) : AViewHolder(view)

abstract class AContentViewHolder(view: View) : AViewHolder(view)

class HeaderViewHolder(view: View) : AViewHolder(view)

class FooterViewHolder(view: View) : AViewHolder(view)