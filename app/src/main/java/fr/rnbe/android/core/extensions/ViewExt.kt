package fr.rnbe.android.core.extensions

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun <T : View> T?.isVisible() = this?.visibility == View.VISIBLE
fun <T : View> T?.isInvisible() = this?.visibility != View.VISIBLE

fun <T : View> T.gone(gone: Boolean) {
    this.visibility = if (gone) View.GONE else View.VISIBLE
}

fun <T : View> T.visibleIfNotNull(o: Any?) {
    this.visibility = if (o != null) View.VISIBLE else View.GONE
}

fun <T : View> T.visible() {
    this.visibility = View.VISIBLE
}

fun <T : View> T.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun <T : View> T.gone() {
    this.visibility = View.GONE
}

fun <T : View> T.invisible() {
    this.visibility = View.INVISIBLE
}

fun <T : View> T.getString(@StringRes resId: Int): String = context.getString(resId)

fun <T : View> T.getString(@StringRes resId: Int, vararg args: Any): String = context.getString(resId, *args)

@ColorInt
fun <T : View> T.getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

fun <T : View> T.getString(@StringRes stringRes: Int, string: String): String {
    return context.resources.getString(stringRes, string)
}

fun <T : View> T.getPluralsArg(@PluralsRes stringRes: Int, count: Int): String {
    return getPlurals(stringRes, count, count)
}

fun <T : View> T.getPlurals(@PluralsRes stringRes: Int, count: Int, vararg formatArgs: Any): String {
    return context.resources.getQuantityString(stringRes, count, *formatArgs)
}

fun <T : View> T.getQuantityStringFormat(@PluralsRes pluralRes: Int, quantity: Int): String {
    return context.resources.getQuantityString(pluralRes, quantity, quantity)
}

fun <T : View> T.getDrawable(@DrawableRes drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(context, drawableResId)
}

fun <T : View> T.getDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    val drawable = getDrawable(drawableId)
    DrawableCompat.setTint(drawable?.mutate()!!, getColor(colorId))
    return drawable
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun <T : TextView> T.strike() {
    this.paintFlags = (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
}

fun <T : TextView> T.underline() {
    this.paintFlags = (paintFlags or Paint.UNDERLINE_TEXT_FLAG)
}

fun <T : TextView> T.underline(start: Int, end: Int) {
    text = SpannableString(text).apply {
        setSpan(UnderlineSpan(), start, end, 0)
    }
}

fun <T : TextView> T.bold() {
    bold(0, text.length)
}

fun <T : TextView> T.bold(start: Int, end: Int) {
    text = SpannableString(text).apply {
        setSpan(StyleSpan(Typeface.BOLD), start, end, 0)
    }
}

var <T : TextView> T.drawableStart: Drawable?
    get() = compoundDrawablesRelative.getOrNull(0)
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(value, null, null,null)

var <T : TextView> T.drawableTop: Drawable?
    get() = compoundDrawablesRelative.getOrNull(1)
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(null, value, null,null)

var <T : TextView> T.drawableEnd: Drawable?
    get() = compoundDrawablesRelative.getOrNull(2)
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, value,null)

var <T : TextView> T.drawableBottom: Drawable?
    get() = compoundDrawablesRelative.getOrNull(2)
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, value)


fun <T : RecyclerView> T.linearLayoutVerticalEndListener(itemBeforeEnd: Int = 5, endReach: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            (layoutManager as? LinearLayoutManager)?.apply {
                val lastVisibleItemPosition = findLastVisibleItemPosition()
                val totalItemCount = itemCount

                if (lastVisibleItemPosition + itemBeforeEnd > totalItemCount) {
                    endReach()
                }
            }
        }
    })
}

fun View.closeKeyboard() {
    val inputManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, 0)
}
