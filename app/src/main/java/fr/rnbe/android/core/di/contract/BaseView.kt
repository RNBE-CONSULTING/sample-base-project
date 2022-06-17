package fr.rnbe.android.core.di.contract

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import com.lmfr.applm.tech.core.di.BaseActivity

interface BaseView {
	fun getContext(): Context?

	fun getString(@StringRes resId: Int): String

	fun getString(@StringRes resId: Int, vararg args: Any?): String

	fun changeActivity(i: Intent, finishActivity: Boolean = true)
	fun changeActivity(
            classActivity: Class<out BaseActivity<*, *, *>>,
            finishActivity: Boolean = true, extras: Bundle? = null,
            intentEditor: ((Intent) -> Unit)? = null
    )
	fun changeActivity(
            action: String,
            intentEditor: ((Intent) -> Unit)? = null
    )
	fun changeActivityForResult(
            i: Intent,
            requestCode: Int,
            intentEditor: ((Intent) -> Unit)? = null,
            isForResultInFragment: Boolean = true
    )
	fun changeActivityForResult(
            classActivity: Class<out BaseActivity<*, *, *>>,
            requestCode: Int,
            extras: Bundle? = null,
            intentEditor: ((Intent) -> Unit)? = null,
            isForResultInFragment: Boolean = true
    )
	fun changeActivityForResult(
            action: String,
            requestCode: Int,
            isForResultInFragment: Boolean = true,
            intentEditor: ((Intent) -> Unit)? = null
    )

	fun hideProgress()

	fun showProgress()

	fun isProgressVisible(): Boolean
}