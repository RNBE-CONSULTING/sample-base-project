package fr.rnbe.android.core.di.contract

import android.content.Intent
import fr.rnbe.android.core.di.BaseDialogFragment
import com.lmfr.applm.tech.core.navigation.Navigator

interface ActivityView : FragmentContainerView {
    fun getIntent(): Intent
    fun showDialog(dialog: BaseDialogFragment<*, *>, tag: String = dialog::class.java.simpleName)

    fun setResult(resultCode: Int, data: Intent?)

    fun hideKeyboard()

    fun getActivityNavigator(): Navigator
    fun finish()

    fun isInForeground(): Boolean
}