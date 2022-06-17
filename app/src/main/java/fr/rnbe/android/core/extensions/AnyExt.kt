package fr.rnbe.android.core.extensions

import android.content.Context
import android.os.Build
import android.util.TypedValue

//Protect max TAG to 23 characters
val Any.TAG: String get() = this::class.java.simpleName.take(22)
fun Any?.isNull(): Boolean = this == null
fun Any?.isNotNull(): Boolean = this != null

fun <R> ifAtLeastApi(api: Int, f: () -> R): R? = if (api <= Build.VERSION.SDK_INT) {
    f()
} else null

fun <R> ifAtMostApi(api: Int, f: () -> R): R? = if (api >= Build.VERSION.SDK_INT) {
    f()
} else null

fun fromSpToPixel(spValue: Float, context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.resources.displayMetrics)
fun fromDpToPixel(spValue: Float, context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spValue, context.resources.displayMetrics)
