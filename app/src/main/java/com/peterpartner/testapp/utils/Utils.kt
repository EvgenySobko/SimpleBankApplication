package com.peterpartner.testapp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.item_history.view.*

fun logd(any: Any?) = Log.d("DEBUG", any.toString())

fun loge(any: Any?) = Log.d("ERROR", any.toString())

fun Fragment.toast(any: Any?) = Toast.makeText(requireContext(), any.toString(), Toast.LENGTH_SHORT).show()

fun Double.beautify(): String {
    with (this) {
        val hundreds = (this % 1000).toInt()
        val thousands = (this / 1000).toInt()
        val decades = (this % 100).toInt()
        val penny = this.toString().split(".")[1]
        val result = java.lang.StringBuilder()
        result.append("$thousands ${if (hundreds < 100) "0$decades" else hundreds},${penny.removeRange(2, penny.length)}")
        return result.toString()
    }
}

fun Double.beautifyRub(currencyChar: String): String {
    val penny = this.toString().split(".")[1]
    val resultPenny = penny.removeRange(2, penny.length)
    val sum = this.toString().replace("-", "- $currencyChar ").split(".")[0]
    return "$sum,$resultPenny"
}
