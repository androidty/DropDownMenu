package com.ty.listener

import android.content.Context
import android.widget.PopupWindow

import androidx.recyclerview.widget.RecyclerView

/**
 * @author ty
 * @date 2019/11/22.
 * GitHub：
 * email：
 * description：meun的点击事件
 */
interface OnMenuClickListener {
    fun onMenuClickListener(context: Context, index: Int)
}
