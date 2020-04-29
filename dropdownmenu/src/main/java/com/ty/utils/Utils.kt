package com.ty.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder

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

fun Context.getJsonFromAssets(fileName: String): String {
    var sb = StringBuilder()
    var assetManager = this.assets
    try {
        var brf = BufferedReader(InputStreamReader(assetManager.open(fileName), "utf-8"))
        while (brf.readLine() != null) {
            sb.append(brf.readLine())
        }
        brf.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return sb.toString().trim()
}

