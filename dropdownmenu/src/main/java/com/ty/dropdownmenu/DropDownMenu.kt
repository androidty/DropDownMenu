package com.ty.dropdownmenu

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
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
@Suppress("DEPRECATION")
class DropDownMenu constructor(context: Context, attributes: AttributeSet? = null) : FrameLayout(context, attributes) {
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

    private var mCurrentIndex = -1

    var onMenuClickListener: OnMenuClickListener? = null


    init {
        init(context, attributes)
    }


    @SuppressLint("ResourceAsColor")
    private fun init(context: Context, attributes: AttributeSet? = null) {
        this.mContext = context


        val ta = mContext!!.obtainStyledAttributes(attributes, R.styleable.DropDownMenu)
        mMenuTitleTextColor = ta.getColor(R.styleable.DropDownMenu_menuTitleTextColor, R.color.default_menu_press_back)
        mMenuTitleTextSize = DensityUtil.px2sp(mContext!!, ta.getDimension(R.styleable.DropDownMenu_menuTitleTextSize, 14f))

        mMenuBackColor = ta.getColor(R.styleable.DropDownMenu_menuBackColor, R.color.default_menu_back)

        mMenuPressedBackColor = ta.getColor(R.styleable.DropDownMenu_menuPressedBackColor, R.color.default_menu_press_back)
        mMenuPressedTitleTextColor = ta.getColor(R.styleable.DropDownMenu_menuPressedTitleTextColor, R.color.default_menu_press_text)

        mUpArrow = ta.getResourceId(R.styleable.DropDownMenu_menuDropDownSelectedIcon, R.mipmap.drop_down_selected_icon)
        mDownArrow = ta.getResourceId(R.styleable.DropDownMenu_menuDropDownUnSelectedIcon, R.mipmap.drop_down_unselected_icon)
        mArrowMarginTitle = DensityUtil.px2dp(mContext!!, ta.getDimension(R.styleable.DropDownMenu_menuArrowMarginTitle, 20f)).toInt()

        ta.recycle()
        mMenuListSelectorRes = R.color.white
        mArrowMarginTitle = 10
        setScrollAble(false)
    }


    private fun setScrollAble(scroll: Boolean) {
        scrollAble = scroll
        addRootView()
    }

    private fun addRootView() {
        removeAllViews()
        var rootView: View? = null
        rootView = if (scrollAble!!) LayoutInflater.from(context)?.inflate(R.layout.drop_menu_scroll, null)
        else LayoutInflater.from(context)?.inflate(R.layout.drop_menu_no_scroll, null)
        addView(rootView)
        dropDownMenuLl = rootView!!.findViewById(R.id.dropMenuLl)
        horizontalScrollView = rootView!!.findViewById(R.id.hScrollView)
    }


    fun initMenu(defaultMenuTitle: Array<String>) {
        initMenu(defaultMenuTitle, false)
    }


    fun initMenu(defaultMenuTitle: Array<String>, scroll: Boolean) {
        defaultStrs = defaultMenuTitle
        setScrollAble(scroll)
        mDrawable = true
        invalidate()
    }

    fun setDropWindow(popupWindow: PopupWindow) {

        setDropWindow(popupWindow, null)
    }

    fun setDropWindow(contentView: View) {
        setDropWindow(PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false))
    }


    var clickOut = false
    fun setDropWindow(popupWindow: PopupWindow, rlShadow: RelativeLayout?) {
        mPopupWindow = popupWindow
        mRlShadow = rlShadow

        mPopupWindow!!.isTouchable = true
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.setBackgroundDrawable(BitmapDrawable())
        mRlShadow?.setOnClickListener { dismiss() }

        mPopupWindow!!.setTouchInterceptor { v, event ->
            if (event.y < 0) {
                clickOut = true
                true
            }
            false
        }


        mPopupWindow!!.setOnDismissListener {
            for (i in defaultStrs!!.indices) {
                mIvMenuArrow[i].setImageResource(mDownArrow)
                animDown(mIvMenuArrow[mColumnSelected])
                mRlMenuBacks[i].setBackgroundColor(mMenuBackColor)
                mTvMenuTitles[i].setTextColor(mMenuTitleTextColor)
            }
        }

    }

    fun dismiss() {
        mPopupWindow?.dismiss()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mDrawable) {
            mDrawable = false
            for (i in defaultStrs!!.indices) {
                val v = LayoutInflater.from(context).inflate(R.layout.menu_item, null)
                var tv = v.findViewById<View>(R.id.tv_menu_title) as TextView
                var iv = v.findViewById<View>(R.id.iv_menu_arrow) as ImageView
                iv.setImageResource(R.mipmap.drop_down_unselected_icon)
                tv.setTextColor(context.resources.getColor(R.color.black))
                tv.maxWidth = (DensityUtil.getScreenWidth(context) / defaultStrs!!.size) * 3 / 4
                tv.text = defaultStrs!![i]
                mTvMenuTitles.add(tv)
                val vP = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                v.layoutParams = vP
                dropDownMenuLl.addView(v, getLayoutParam())
                val rl = v.findViewById<View>(R.id.rl_menu_head) as RelativeLayout
                rl.setBackgroundColor(mMenuBackColor)
                mRlMenuBacks.add(rl)

                mIvMenuArrow.add(iv)
                mIvMenuArrow[i].setImageResource(mDownArrow)

                val params = iv.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = mArrowMarginTitle
                iv.layoutParams = params
                v.setOnClickListener {

                    if (clickOut) {
                        clickOut = false
                        if (mCurrentIndex == i && !mPopupWindow!!.isShowing) {
                            return@setOnClickListener
                        }
                    }


//                    if (mCurrentIndex == i && mPopupWindow!!.isShowing) {
//                        dismiss()
//                        return@setOnClickListener
//                    }
                    mCurrentIndex = i
                    v.findViewById<ImageView>(R.id.iv_menu_arrow).setImageResource(mUpArrow)
                    if (onMenuClickListener != null) {
                        onMenuClickListener!!.onMenuClickListener(mContext!!, i)
                    }
                    mColumnSelected = i
                    mTvMenuTitles[i].setTextColor(mMenuPressedTitleTextColor)
                    mRlMenuBacks[i].setBackgroundColor(mMenuPressedBackColor)
                    mIvMenuArrow[i].setImageResource(mUpArrow)
                    animUp(mIvMenuArrow[i])
                    mPopupWindow!!.showAsDropDown(v)
                }
            }
        }
    }


    private fun getLayoutParam(): LinearLayout.LayoutParams {
        return if (scrollAble) {
            val lp = LinearLayout.LayoutParams(DensityUtil.getScreenWidth(context) / 7 * 2, ViewGroup.LayoutParams.MATCH_PARENT)
            lp
        } else {
            val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            lp.weight = 1f
            lp
        }

    }

    fun setCurrentTitle(currentIndex: Int, currentTitle: String) {
        mTvMenuTitles[currentIndex].text = currentTitle
    }

    private fun animUp(imageView: ImageView) {
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f)
        animator.duration = 300
        animator.start()
    }

    private fun animDown(imageView: ImageView) {
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f)
        animator.duration = 300
        animator.start()
    }

}