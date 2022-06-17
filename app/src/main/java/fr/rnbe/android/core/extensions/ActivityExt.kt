package fr.rnbe.android.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment

fun Activity.safeStartActivity(intent: Intent, onActivityNotFound: (() -> Unit)? = null, onActivityStarted: (() -> Unit)? = null) = try {
    startActivity(intent).also { onActivityStarted?.invoke() }
} catch (e: ActivityNotFoundException) {
    Log.e("ActivityExt", "Activity not found", e)
    onActivityNotFound?.invoke()
}

fun Fragment.safeStartActivity(intent: Intent, onActivityNotFound: (() -> Unit)? = null, onActivityStarted: (() -> Unit)? = null) = try {
    startActivity(intent).also { onActivityStarted?.invoke() }
} catch (e: ActivityNotFoundException) {
    Log.e("ActivityExt", "Activity not found", e)
    onActivityNotFound?.invoke()
}

fun Context.safeStartActivity(intent: Intent, onActivityNotFound: (() -> Unit)? = null, onActivityStarted: (() -> Unit)? = null) = try {
    startActivity(intent).also { onActivityStarted?.invoke() }
} catch (e: ActivityNotFoundException) {
    Log.e("ActivityExt", "Activity not found", e)
    onActivityNotFound?.invoke()
}


fun Activity.safeStartActivityForResult(intent: Intent, requestCode: Int, isForResultInFragment: Boolean = true, onActivityNotFound: (() -> Unit)? = null) = try {
    startActivityForResult(intent, requestCode)
} catch (e: ActivityNotFoundException) {
    Log.e("ActivityExt", "Activity not found", e)
    onActivityNotFound?.invoke()
}


fun Fragment.safeStartActivityForResult(intent: Intent, requestCode: Int, isForResultInFragment: Boolean = true, onActivityNotFound: (() -> Unit)? = null) = try {
    if (isForResultInFragment) {
        startActivityForResult(intent, requestCode)
    } else {
        activity?.startActivityForResult(intent, requestCode)
    }
} catch (e: ActivityNotFoundException) {
    Log.e("ActivityExt", "Activity not found", e)
    onActivityNotFound?.invoke()
}