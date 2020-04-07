package com.ty.utils

/**
 * @author ty
 * @date 2020/1/2.
 * GitHub：
 * email：
 * description：
 */
class Utils {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("kotlin".lastChar())//注意这里，打印'o'
        }
    }
}

fun String.lastChar(): Char {
    return this[this.length - 1]
}
