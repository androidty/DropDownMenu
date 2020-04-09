package com.ty.dropdownmenu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ty.utils.DensityUtil

/**
 * @author ty
 * @date 2020/4/2.
 * GitHub：
 * email：
 * description：
 */
class DropDownMenu1 constructor(context: Context, attributes: AttributeSet? = null) : FrameLayout(context, attributes) {
    private var scrollAble = true
    private var mDrawable = false
    private var horizontalScrollView: HorizontalScrollView? = null
    private lateinit var dropDownMenuLl: LinearLayout

    //recyclerview附着在popupwindow上
    lateinit var popupWindow: PopupWindow

    //recyclerView下面的阴影区域
    lateinit var shadowLl: LinearLayout

    //文字
    private val titleTvs: ArrayList<TextView> = ArrayList<TextView>()

    //箭头
    private val arrowIvs: ArrayList<ImageView> = ArrayList<ImageView>()

    //初始文字
    var defaultStrs: Array<String>? = null


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
                        popupWindow.showAsDropDown(v)
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


}