package fr.rnbe.android.core.di.contract

interface DialogFragmentView: FragmentView {
    fun dismiss()
    fun dismissAllowingStateLoss()
}