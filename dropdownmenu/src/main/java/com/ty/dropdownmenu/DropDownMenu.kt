package com.ty.dropdownmenu

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import com.ty.listener.OnMenuClickListener
import com.ty.utils.DensityUtil
import java.util.*


class DropDownMenu @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null) : LinearLayout(mContext, attrs) {


    //菜单 上的文字
    private val mTvMenuTitles = ArrayList<TextView>()

    //菜单 的背景布局
    private val mRlMenuBacks = ArrayList<RelativeLayout>()

    //菜单 的箭头
    private val mIvMenuArrow = ArrayList<ImageView>()

    private var mContext: Context? = null

    private var mPopupWindow: PopupWindow? = null

    // Menu 展开的列表 下部的阴影
    private var mRlShadow: RelativeLayout? = null


    // 主Menu的个数
    private var mMenuCount: Int = 0

    // Menu 展开的list 显示数量
    private var mShowCount: Int = 0


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

    private var mDrawable = false

    private var mDefaultMenuTitle: Array<String>? = null

    private var isDebug = true

    private var mCuttentIndex = -1

    var onMenuClickListener: OnMenuClickListener? = null

    init {
        init(mContext, attrs)
    }

    @SuppressLint("ResourceAsColor")
    private fun init(mContext: Context, attrs: AttributeSet?) {
        this.mContext = mContext

        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.DropDownMenu)
        mMenuTitleTextColor = ta.getColor(R.styleable.DropDownMenu_menuTitleTextColor, R.color.default_menu_text)
        mMenuTitleTextSize = DensityUtil.px2sp(mContext, ta.getDimension(R.styleable.DropDownMenu_menuTitleTextSize, 14f))
        mMenuBackColor = ta.getColor(R.styleable.DropDownMenu_menuBackColor, R.color.default_menu_back)
        mMenuPressedBackColor = ta.getColor(R.styleable.DropDownMenu_menuPressedBackColor, R.color.default_menu_press_back)
        mMenuPressedTitleTextColor = ta.getColor(R.styleable.DropDownMenu_menuPressedTitleTextColor, R.color.default_menu_press_text)
        mUpArrow = ta.getResourceId(R.styleable.DropDownMenu_menuDropDownSelectedIcon, R.mipmap.drop_down_selected_icon)
        mDownArrow = ta.getResourceId(R.styleable.DropDownMenu_menuDropDownUnSelectedIcon, R.mipmap.drop_down_unselected_icon)
        mArrowMarginTitle = DensityUtil.px2dp(mContext, ta.getDimension(R.styleable.DropDownMenu_menuArrowMarginTitle, 20f)).toInt()
        ta.recycle()
        mMenuListSelectorRes = R.color.white
        mArrowMarginTitle = 10
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


    // 设置 Menu的数量
    fun setMenuCount(menuCount: Int) {
        mMenuCount = menuCount
    }

    fun getMenuCount() {
        if(mMenuCount == 0)1 else 0
    }

    fun setmMenuListSelectorRes(menuListSelectorRes: Int) {
        mMenuListSelectorRes = menuListSelectorRes
    }

    fun setmArrowMarginTitle(arrowMarginTitle: Int) {
        mArrowMarginTitle = arrowMarginTitle
    }

    fun setmMenuPressedTitleTextColor(menuPressedTitleTextColor: Int) {
        mMenuPressedTitleTextColor = menuPressedTitleTextColor
    }

    fun setDefaultMenuTitle(defaultMenuTitle: Array<String>) {
        this.mDefaultMenuTitle = defaultMenuTitle
        mDrawable = true
        invalidate()
    }

    fun setIsDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    // 设置 show 数量
    fun setmShowCount(showCount: Int) {
        mShowCount = showCount
    }

    // 设置 Menu的字体颜色
    fun setmMenuTitleTextColor(menuTitleTextColor: Int) {
        mMenuTitleTextColor = menuTitleTextColor
    }

    // 设置 Menu的字体大小
    fun setmMenuTitleTextSize(menuTitleTextSize: Float) {
        mMenuTitleTextSize = menuTitleTextSize
    }

    //设置Menu的背景色
    fun setmMenuBackColor(menuBackColor: Int) {
        mMenuBackColor = menuBackColor
    }

    //设置Menu的按下背景色
    fun setmMenuPressedBackColor(menuPressedBackColor: Int) {
        mMenuPressedBackColor = menuPressedBackColor
    }

    //设置对勾的icon
    fun setmCheckIcon(checkIcon: Int) {
        mCheckIcon = checkIcon
    }

    fun setmUpArrow(upArrow: Int) {
        mUpArrow = upArrow
    }

    fun setmDownArrow(downArrow: Int) {
        mDownArrow = downArrow
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDrawable) {
            val width = width
            for (i in mDefaultMenuTitle!!.indices) {
                val v = LayoutInflater.from(mContext).inflate(R.layout.menu_item, null, false) as RelativeLayout
                val parms = RelativeLayout.LayoutParams(width / mMenuCount, LinearLayout.LayoutParams.WRAP_CONTENT)
                v.layoutParams = parms
                val tv = v.findViewById<View>(R.id.tv_menu_title) as TextView
                tv.setTextColor(mMenuTitleTextColor)
                tv.textSize = mMenuTitleTextSize
                tv.text = mDefaultMenuTitle!![i]
                val lp = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
                lp.weight = 1f
                this.addView(v, lp)
                mTvMenuTitles.add(tv)
                val rl = v.findViewById<View>(R.id.rl_menu_head) as RelativeLayout
                rl.setBackgroundColor(mMenuBackColor)
                mRlMenuBacks.add(rl)

                val iv = v.findViewById<View>(R.id.iv_menu_arrow) as ImageView
                mIvMenuArrow.add(iv)
                mIvMenuArrow[i].setImageResource(mDownArrow)

                val params = iv.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = mArrowMarginTitle
                iv.layoutParams = params

                v.setOnClickListener(OnClickListener {
                    if (mCuttentIndex == i) {
                        mPopupWindow!!.dismiss()
                        mCuttentIndex = -1
                        return@OnClickListener
                    }
                    mCuttentIndex = i
                    if (onMenuClickListener != null) {
                        onMenuClickListener!!.onMenuClickListener(mContext!!, i)
                    }
                    mColumnSelected = i
                    mTvMenuTitles[i].setTextColor(mMenuPressedTitleTextColor)
                    mRlMenuBacks[i].setBackgroundColor(mMenuPressedBackColor)
                    mIvMenuArrow[i].setImageResource(mUpArrow)
                    animUp(mIvMenuArrow[i])
                    mPopupWindow!!.showAsDropDown(v)
                })
                Log.d("dropdownMenu", "onDraw: " + width + "  " + v.measuredWidth)

            }
            mDrawable = false
        }
    }

    fun setCurrentTitle(cuttentIndex: Int, currentTitle: String) {
        mTvMenuTitles[cuttentIndex].text = currentTitle
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
