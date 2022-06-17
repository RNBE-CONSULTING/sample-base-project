package fr.rnbe.android.core.di.contract

import fr.rnbe.android.core.di.BaseChildFragment
import fr.rnbe.android.core.di.BaseDialogFragment
import fr.rnbe.android.core.di.BaseFragment
import com.lmfr.applm.tech.core.extensions.TAG

interface ChildFragmentView: BaseView {
	fun changeFragment(fragment: BaseChildFragment<*, *>)

	fun changeParentFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean = true)

	fun showDialog(dialog: BaseDialogFragment<*, *>, tag: String = dialog.TAG)
}