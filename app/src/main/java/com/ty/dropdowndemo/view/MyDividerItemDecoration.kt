package com.ty.dropdowndemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.ty.dropdowndemo.R
import com.ty.utils.DensityUtil


/**
 * @author ty
 * @date 2019/11/19.
 * GitHub：
 * email：
 * description：
 */
class MyDividerItemDecoration : RecyclerView.ItemDecoration {

    private var mDivider: Drawable? = null     //分割线Drawable
    private var mDividerHeight: Int = 0  //分割线高度
    private var mLeft: Int = 0
    private var mTop: Int = 0
    private var mRight: Int = 0
    private var mIsLastGone: Boolean = false

    private var specialTop: Int =0

    /**
     * 使用line_divider中定义好的颜色
     *
     * @param context
     * @param dividerHeight 分割线高度
     */
    constructor(context: Context, dividerHeight: Int) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
        mDividerHeight = dividerHeight
    }

    /**
     * @param context
     * @param divider       分割线Drawable
     * @param dividerHeight 分割线高度
     * @param lastGone      最后一条是否显示
     */
    constructor(context: Context, divider: Drawable?, dividerHeight: Int, lastGone: Boolean) : this(context, divider, dividerHeight, 0, 0, 0, lastGone) {}

    constructor(context: Context, divider: Drawable?, dividerHeight: Int, left: Int, lastgone: Boolean) : this(context, divider, dividerHeight, left, 0, 0, lastgone) {}

    constructor(context: Context, divider: Drawable?, dividerHeight: Int, left: Int, right: Int, lastgone: Boolean) : this(context, divider, dividerHeight, left, 0, right, lastgone) {}

    constructor(context: Context, divider: Drawable?, dividerHeight: Int, left: Int, top: Int, right: Int, lastgone: Boolean) {
        var left = left
        var top = top
        var right = right
        if (divider == null) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
        } else {
            mDivider = divider
        }
        top = DensityUtil.dp2px(context, top.toFloat())
        left = DensityUtil.dp2px(context, left.toFloat())
        right = DensityUtil.dp2px(context, right.toFloat())
        mDividerHeight = dividerHeight
        this.mLeft = left
        this.mTop = top
        this.mRight = right
        this.mIsLastGone = lastgone
    }

    constructor(context: Context, divider: Drawable, dividerHeight: Int, left: Int, top: Int, right: Int, specialTop: Int, lastgone: Boolean) : this(context, divider, dividerHeight, left, top, right, lastgone) {
        this.specialTop = specialTop
    }

    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, mDividerHeight)
        if (specialTop > 0 && parent.getChildAdapterPosition(view) == 0) {
            outRect.top = specialTop
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + mLeft
        val right = parent.width - parent.paddingRight - mRight
        var childCount = parent.childCount
        if (mIsLastGone) {
            childCount--
        }
        for (i in 0 until childCount) {
            drawLine(c, parent, left, right, i)
        }
    }

    private fun drawLine(c: Canvas, parent: RecyclerView, left: Int, right: Int, i: Int) {
        val child = parent.getChildAt(i)
        val params = child.layoutParams as RecyclerView.LayoutParams
        val top = child.bottom + params.bottomMargin + mTop
        val bottom = top + mDividerHeight
        mDivider!!.setBounds(left, top, right, bottom)
        mDivider!!.draw(c)
    }
}