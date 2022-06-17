package fr.rnbe.android.core.di.contract

import android.os.Parcelable

interface ArgumentFragmentView<T: Parcelable>: FragmentView {
    fun getArgDataUi(): T
}