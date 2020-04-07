package com.ty.utils

import android.content.Context
import android.util.TypedValue

/**
 * 单位转换类
 */
class DensityUtil
/**
 * cannot be instantiated
 */
private constructor() {

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * dp转px
         *
         * @param context
         * @param dpVal
         * @return
         */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context.resources.displayMetrics).toInt()
        }

        /**
         * sp转px
         *
         * @param context
         * @param spVal
         * @return
         */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    spVal, context.resources.displayMetrics).toInt()
        }

        /** sp转换px  */
        fun sp2px2(context: Context, spValue: Int): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }


        /**
         * px转dp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /**
         * px转sp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }

        fun getScreenWidth(context: Context): Int {
            return context.resources.displayMetrics.widthPixels
        }
    }
}