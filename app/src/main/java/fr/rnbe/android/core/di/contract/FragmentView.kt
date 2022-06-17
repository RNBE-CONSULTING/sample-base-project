package fr.rnbe.android.core.di.contract

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import fr.rnbe.android.core.di.BaseChildFragment
import fr.rnbe.android.core.di.BaseDialogFragment
import com.lmfr.applm.tech.core.utils.AppAlertDialog

interface FragmentView : FragmentContainerView {

    fun parentActivity(): ActivityView

    fun updateToolbarTitle(@StringRes titleId: Int)

    fun updateToolbarTitle(title: String)

    fun changeChildFragment(fragment: BaseChildFragment<*, *>)

    fun showDialog(dialog: BaseDialogFragment<*, *>, tag: String = dialog::class.java.simpleName)

    fun setActivityResult(resultCode: Int, data: Intent? = null)

    fun closeActivity()

    fun attachFragmentResult(requestKey: String, result: ((Bundle) -> Unit))

    /**
     * If the fragment would handle onBackPressed itself.
     * Just return true to inform that the event is consume, otherwise, return false
     */
    fun onBackPressed(): Boolean = false

    fun requestPermissions(permissions: Array<String>, requestCode: Int)

    fun alertDialog(alertDialog: AlertDialog)

    fun createAppAlertBuilder(style: Int = AppAlertDialog.NO_MARGIN_H) = getContext()?.let { AppAlertDialog.Builder( it, style) }
}