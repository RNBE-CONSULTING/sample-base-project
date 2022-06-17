package fr.rnbe.android.core.di

import android.os.Bundle
import android.os.Parcelable
import androidx.viewbinding.ViewBinding
import com.lmfr.applm.tech.core.di.contract.ArgumentFragmentView

abstract class BaseArgumentFragment<T: BasePresenter<*>, B: ViewBinding, A: Parcelable>: BaseFragment<T, B>() , ArgumentFragmentView<A> {

    companion object {
        const val ARGUMENT_DATA_UI = "argument_data_ui"

        fun BaseArgumentFragment<*, *, *>.attachArgument(dataUi: Parcelable): BaseFragment<*, *> = this.apply {
            arguments = Bundle().apply { putParcelable(ARGUMENT_DATA_UI, dataUi) }
        }
    }

    override fun getArgDataUi(): A {
        return arguments?.getParcelable(ARGUMENT_DATA_UI) ?: throw IllegalArgumentException("ARGUMENT_DATA_UI must not be null on Bundle Object")
    }
}