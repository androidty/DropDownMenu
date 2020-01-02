package com.ty.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author ty
 * @date 2019/11/22.
 * GitHub：
 * email：
 * description：
 */
abstract class CommonAdapter<T>(layoutResId: Int, data: List<T>?) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data)
