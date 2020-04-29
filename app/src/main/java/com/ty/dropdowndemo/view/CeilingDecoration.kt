package com.ty.dropdowndemo.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.SparseArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


import com.ty.dropdowndemo.bean.DropBean
import com.ty.dropdowndemo.R
import com.ty.utils.DensityUtil

import java.util.HashMap

class CeilingDecoration(context: Context) : RecyclerView.ItemDecoration() {
    protected var TAG = "QDX"
    private val mHeaderTxtPaint: Paint
    private val mHeaderContentPaint: Paint
    private val mHeaderLinePaint: Paint
    private var LinePaddingLeft = 15
    private var LinePaddingRight = 49
    private var headerHeight: Int = 100//头部高度
    private var textPaddingLeft = 50//头部文字左边距
    private var textSize = 50
    private var textColor = Color.BLACK
    private var headerContentColor = -0x111112
    private var headerLineColor = -0x111112
    private val txtYAxis: Float
    private var mRecyclerView: RecyclerView? = null
    private var mDropBeans: List<DropBean.GroupBean.BrandsBean>? = null

    private var isInitHeight = false

    private val stickyHeaderPosArray = SparseArray<Int>()//记录每个头部和悬浮头部的坐标信息【用于点击事件】
    private var gestureDetector: GestureDetector? = null

    private val headViewMap = HashMap<Int, View>()
    private val viewLineMap = HashMap<Int, View>()

    private var headerClickEvent: OnHeaderClickListener? = null


    private val gestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(e: MotionEvent): Boolean {
            return false
        }

        override fun onShowPress(e: MotionEvent) {}

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (i in 0 until stickyHeaderPosArray.size()) {
                val value = stickyHeaderPosArray.valueAt(i)
                val y = e.y
                if (value - headerHeight <= y && y <= value) {//如果点击到分组头
                    if (headerClickEvent != null) {
                        headerClickEvent!!.headerClick(stickyHeaderPosArray.keyAt(i))
                    }
                    return true
                }
            }
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent) {}

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            return false
        }
    }

    private var headerDrawEvent: OnDecorationHeadDraw? = null

    private val imgDrawableMap = HashMap<String, Drawable>()

    init {
        headerHeight = DensityUtil.dp2px(context, 14f)
        textSize = DensityUtil.sp2px(context, 11f)
        textPaddingLeft = DensityUtil.dp2px(context, 16f)
        LinePaddingLeft = DensityUtil.dp2px(context, 15f)
        LinePaddingRight = DensityUtil.dp2px(context, 49f)
        textColor = ContextCompat.getColor(context, R.color.text_333333)
        headerContentColor = ContextCompat.getColor(context, R.color.c_gary_line)
        headerLineColor = ContextCompat.getColor(context, R.color.c_gary_line)

        mHeaderTxtPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHeaderTxtPaint.color = textColor
        mHeaderTxtPaint.textSize = textSize.toFloat()
        mHeaderTxtPaint.textAlign = Paint.Align.LEFT

        mHeaderLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHeaderLinePaint.color = headerLineColor

        mHeaderContentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHeaderContentPaint.color = headerContentColor
        val fontMetrics = mHeaderTxtPaint.fontMetrics
        val total = -fontMetrics.ascent + fontMetrics.descent
        txtYAxis = total / 2 - fontMetrics.descent
    }

    /**
     * 先调用getItemOffsets再调用onDraw
     */
    override fun getItemOffsets(outRect: Rect, itemView: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, itemView, parent, state)
        if (mRecyclerView == null) {
            mRecyclerView = parent
        }

        if (headerDrawEvent != null && !isInitHeight) {
            val headerView = headerDrawEvent!!.getHeaderView(0)
            headerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            headerHeight = headerView.measuredHeight
            isInitHeight = true
        }

        /*我们为每个不同头部名称的第一个item设置头部高度*/
        val pos = parent.getChildAdapterPosition(itemView) //获取当前itemView的位置
        val curHeaderName = getHeaderName(pos) ?: return         //根据pos获取要悬浮的头部名

        if (pos == 0 || curHeaderName != getHeaderName(pos - 1)) {//如果当前位置为0，或者与上一个item头部名不同的，都腾出头部空间
            outRect.top = headerHeight                                 //设置itemView PaddingTop的距离
        }
    }

    fun getHeaderName(pos: Int): String? {
        return if (mDropBeans != null && !mDropBeans!!.isEmpty() && mDropBeans!![pos] != null) {
            mDropBeans!![pos].firstAlphabet
        } else {
            null
        }
    }

    override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, recyclerView, state)
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView
        }
        if (gestureDetector == null) {
            gestureDetector = GestureDetector(recyclerView.context, gestureListener)
            recyclerView.setOnTouchListener { v, event -> gestureDetector!!.onTouchEvent(event) }
        }

        stickyHeaderPosArray.clear()
        val childCount = recyclerView.childCount//获取屏幕上可见的item数量
        val left = recyclerView.left + recyclerView.paddingLeft
        val right = recyclerView.right - recyclerView.paddingRight

        var firstHeaderName: String? = null
        var firstPos = 0
        var translateTop = 0//绘制悬浮头部的偏移量
        /*for循环里面绘制每个分组的头部*/
        for (i in 0 until childCount) {
            val childView = recyclerView.getChildAt(i)
            val pos = recyclerView.getChildAdapterPosition(childView) //获取当前view在Adapter里的pos
            val curHeaderName = getHeaderName(pos)                 //根据pos获取要悬浮的头部名
            if (i == 0) {
                firstHeaderName = curHeaderName
                firstPos = pos
            }
            if (curHeaderName == null)
                continue//如果headerName为空，跳过此次循环

            val viewTop = childView.top + recyclerView.paddingTop
            if (pos == 0 || curHeaderName != getHeaderName(pos - 1)) {//如果当前位置为0，或者与上一个item头部名不同的，都腾出头部空间
                if (headerDrawEvent != null) {
                    val headerView: View?
                    if (headViewMap[pos] == null) {
                        headerView = headerDrawEvent!!.getHeaderView(pos)
                        headerView
                                .measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        headerView.isDrawingCacheEnabled = true
                        headerView.layout(0, 0, right, headerHeight)//布局layout
                        headViewMap[pos] = headerView
                        canvas.drawBitmap(headerView.drawingCache, left.toFloat(), (viewTop - headerHeight).toFloat(), null)

                    } else {
                        headerView = headViewMap[pos]
                        canvas.drawBitmap(headerView!!.drawingCache, left.toFloat(), (viewTop - headerHeight).toFloat(), null)
                    }
                } else {
                    canvas.drawRect(left.toFloat(), (viewTop - headerHeight).toFloat(), right.toFloat(), viewTop.toFloat(), mHeaderContentPaint)
                    canvas.drawText(curHeaderName, (left + textPaddingLeft).toFloat(), viewTop - headerHeight / 2 + txtYAxis, mHeaderTxtPaint)
                }
                if (headerHeight < viewTop && viewTop <= 2 * headerHeight) { //此判断是刚好2个头部碰撞，悬浮头部就要偏移
                    translateTop = viewTop - 2 * headerHeight
                }
                stickyHeaderPosArray.put(pos, viewTop)//将头部信息放进array
                Log.i(TAG, "绘制各个头部$pos")
            } else {
                if (headerDrawEvent != null) {
                    val headerLineView: View?
                    if (viewLineMap[pos] == null) {
                        headerLineView = headerDrawEvent!!.getHeaderView(pos)
                        headerLineView
                                .measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        headerLineView.isDrawingCacheEnabled = true
                        headerLineView.layout(LinePaddingLeft, 0, right - LinePaddingRight, 1)//布局layout
                        viewLineMap[pos] = headerLineView
                        canvas.drawBitmap(headerLineView.drawingCache, (left + LinePaddingLeft).toFloat(), (viewTop - 1).toFloat(), null)

                    } else {
                        headerLineView = viewLineMap[pos]
                        canvas.drawBitmap(headerLineView!!.drawingCache, (left + LinePaddingLeft).toFloat(), (viewTop - 1).toFloat(), null)
                    }
                } else {
                    canvas.drawRect((left + LinePaddingLeft).toFloat(), (viewTop - 1).toFloat(), (right - LinePaddingRight).toFloat(), viewTop.toFloat(), mHeaderLinePaint)
                }
            }
        }
        if (firstHeaderName == null)
            return


        canvas.save()
        canvas.translate(0f, translateTop.toFloat())
        if (headerDrawEvent != null) {//inflater
            val headerView: View?
            if (headViewMap[firstPos] == null) {
                headerView = headerDrawEvent!!.getHeaderView(firstPos)
                headerView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                headerView.isDrawingCacheEnabled = true
                headerView.layout(0, 0, right, headerHeight)//布局layout
                headViewMap[firstPos] = headerView
                canvas.drawBitmap(headerView.drawingCache, left.toFloat(), 0f, null)
            } else {
                headerView = headViewMap[firstPos]
                canvas.drawBitmap(headerView!!.drawingCache, left.toFloat(), 0f, null)
            }
        } else {
            /*绘制悬浮的头部*/
            canvas.drawRect(left.toFloat(), 0f, right.toFloat(), headerHeight.toFloat(), mHeaderContentPaint)
            canvas.drawText(firstHeaderName, (left + textPaddingLeft).toFloat(), headerHeight / 2 + txtYAxis, mHeaderTxtPaint)
            //           canvas.drawLine(0, headerHeight / 2, right, headerHeight / 2, mHeaderTxtPaint);//画条线看看文字居中不
        }
        canvas.restore()
        Log.i(TAG, "绘制悬浮头部")
    }

    fun setDropBean(dropBeans: List<DropBean.GroupBean.BrandsBean>) {
        mDropBeans = dropBeans
    }

    interface OnHeaderClickListener {
        fun headerClick(pos: Int)
    }

    fun setOnHeaderClickListener(headerClickListener: OnHeaderClickListener?) {
        this.headerClickEvent = headerClickListener
    }

    interface OnDecorationHeadDraw {
        fun getHeaderView(pos: Int): View
    }

    /**
     * 只是用来绘制，不能做其他处理/点击事件等
     */
    fun setOnDecorationHeadDraw(decorationHeadDraw: OnDecorationHeadDraw?) {
        this.headerDrawEvent = decorationHeadDraw
    }

    fun loadImage(url: String, pos: Int, imageView: ImageView) {

        //        if (getImg(url) != null) {
        //            Log.i("qdx", "Glide 加载完图片" + pos);
        //
        //            imageView.setImageDrawable(getImg(url));
        //
        //        } else {
        //            Glide.with(mRecyclerView.getContext()).load(url).into(new SimpleTarget<Drawable>() {
        //                @Override
        //                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
        //                    Log.i("qdx", "Glide回调" + pos);
        //                    headViewMap.remove(pos);//删除，重新更新
        //                    imgDrawableMap.put(url, resource);
        //                    mRecyclerView.postInvalidate();
        //                }
        //            });
        //        }

    }

    private fun getImg(url: String): Drawable? {
        return imgDrawableMap[url]
    }

    fun onDestory() {
        headViewMap.clear()
        imgDrawableMap.clear()
        stickyHeaderPosArray.clear()
        mRecyclerView = null
        setOnHeaderClickListener(null)
        setOnDecorationHeadDraw(null)
    }


    fun setHeaderHeight(headerHeight: Int) {
        this.headerHeight = headerHeight
    }

    fun setTextPaddingLeft(textPaddingLeft: Int) {
        this.textPaddingLeft = textPaddingLeft
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        this.mHeaderTxtPaint.textSize = textSize.toFloat()
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        this.mHeaderTxtPaint.color = textColor
    }

    fun setHeaderContentColor(headerContentColor: Int) {
        this.headerContentColor = headerContentColor
        this.mHeaderContentPaint.color = headerContentColor
    }
}