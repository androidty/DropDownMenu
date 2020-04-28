package com.ty.dropdownmenu

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ty.listener.OnMenuClickListener
import com.ty.utils.DensityUtil

/**
 * @author ty
 * @date 2020/4/2.
 * GitHub：
 * email：
 * description：
 */
class ScrollDropDownMenu constructor(context: Context, attributes: AttributeSet? = null) : FrameLayout(context, attributes) {
    private var scrollAble = true
    private var mDrawable = false
    private var horizontalScrollView: HorizontalScrollView? = null
    private lateinit var dropDownMenuLl: LinearLayout

    //菜单 上的文字
    private val mTvMenuTitles = java.util.ArrayList<TextView>()

    //菜单 的背景布局
    private val mRlMenuBacks = java.util.ArrayList<RelativeLayout>()

    //菜单 的箭头
    private val mIvMenuArrow = java.util.ArrayList<ImageView>()

    private var mContext: Context? = null

    private var mPopupWindow: PopupWindow? = null

    // Menu 展开的列表 下部的阴影
    private var mRlShadow: RelativeLayout? = null

    // 主Menu的个数
    private var mMenuCount: Int = 0

    // Menu 展开的list 显示数量
    private var mShowCount: Int = 0

    //recyclerview附着在popupwindow上

    //recyclerView下面的阴影区域
    lateinit var shadowLl: LinearLayout

    //文字
    private val titleTvs: ArrayList<TextView> = ArrayList<TextView>()

    //箭头
    private val arrowIvs: ArrayList<ImageView> = ArrayList<ImageView>()

    //初始文字
    var defaultStrs: Array<String>? = null

    //选中列数
    private var mColumnSelected = 0

    //Menu的字体颜色
    private var mMenuTitleTextColor: Int = 0

    //Menu的字体大小
    private var mMenuTitleTextSize: Float = 0.toFloat()

    //Menu的按下的字体颜色
    private var mMenuPressedTitleTextColor: Int = 0

    //Menu的按下背景
    private var mMenuPressedBackColor: Int = 0

    //Menu的背景
    private var mMenuBackColor: Int = 0

    //列表的按下效果
    private var mMenuListSelectorRes: Int = 0

    //箭头距离
    private var mArrowMarginTitle: Int = 0

    //对勾的图片资源
    private var mCheckIcon: Int = 0

    //向上的箭头图片资源
    private var mUpArrow: Int = 0

    //向下的箭头图片资源
    private var mDownArrow: Int = 0


    private var mDefaultMenuTitle: Array<String>? = null

    private var isDebug = true

    private var mCuttentIndex = -1

    var onMenuClickListener: OnMenuClickListener? = null


    init {
        init(context, attributes)
    }


    private fun init(context: Context, attributes: AttributeSet? = null) {
        removeAllViews()
        var rootView: View? = null
        rootView = if (scrollAble!!) LayoutInflater.from(context)?.inflate(R.layout.drop_menu_scroll, null)
        else LayoutInflater.from(context)?.inflate(R.layout.drop_menu_no_scroll, null)

        dropDownMenuLl = rootView!!.findViewById(R.id.dropMenuLl)
        horizontalScrollView = rootView!!.findViewById(R.id.hScrollView)
        addView(rootView)
    }


    fun setDefaultStr(defaultMenuTitle: Array<String>) {
        defaultStrs = defaultMenuTitle
        mDrawable = true
        invalidate()
    }


    fun setDropWindow(popupWindow: PopupWindow, rlShadow: RelativeLayout) {
        mPopupWindow = popupWindow
        mRlShadow = rlShadow

        mPopupWindow!!.isTouchable = true
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.setBackgroundDrawable(BitmapDrawable())
        mRlShadow!!.setOnClickListener { mPopupWindow!!.dismiss() }

        mPopupWindow!!.setOnDismissListener {
            for (i in 0 until mMenuCount) {
                mIvMenuArrow[i].setImageResource(mDownArrow)
                animDown(mIvMenuArrow[mColumnSelected])
                mRlMenuBacks[i].setBackgroundColor(mMenuBackColor)
                mTvMenuTitles[i].setTextColor(mMenuTitleTextColor)
            }
        }

    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mDrawable) {
            mDrawable = false
            if (scrollAble) {
                for (i in defaultStrs!!.indices) {
                    val v = LayoutInflater.from(context).inflate(R.layout.menu_item, null)
                    var tv = v.findViewById<View>(R.id.tv_menu_title) as TextView
                    var iv = v.findViewById<View>(R.id.iv_menu_arrow) as ImageView
                    iv.setImageResource(R.mipmap.drop_down_unselected_icon)
                    tv.text = defaultStrs!![i]
                    tv.setTextColor(context.resources.getColor(R.color.default_menu_text))
                    val lp = LinearLayout.LayoutParams(DensityUtil.getScreenWidth(context) / 7 * 2, ViewGroup.LayoutParams.MATCH_PARENT)

                    v.setOnClickListener {
                        v.findViewById<ImageView>(R.id.iv_menu_arrow).setImageResource(R.mipmap.drop_down_selected_icon)
                        mPopupWindow?.showAsDropDown(v)
                    }
                    dropDownMenuLl.addView(v, lp)
                }
            } else {
                for (title in defaultStrs!!) {
                    val v = LayoutInflater.from(context).inflate(R.layout.menu_item, null)
                    var tv = v.findViewById<View>(R.id.tv_menu_title) as TextView
                    var iv = v.findViewById<View>(R.id.iv_menu_arrow) as ImageView
                    iv.setImageResource(R.mipmap.drop_down_unselected_icon)
                    tv.text = title
                    val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
                    lp.weight = 1f
                    dropDownMenuLl.addView(v, lp)
                }
            }
        }
    }


    fun animUp(imageView: ImageView) {
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f)
        animator.duration = 300
        animator.start()
    }

    fun animDown(imageView: ImageView) {
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f)
        animator.duration = 300
        animator.start()
    }

}