package fr.rnbe.android.core.di

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lmfr.applm.tech.core.R

abstract class BaseBottomSheetDialogFragment<T : BasePresenter<*>, B : ViewBinding>:
        BaseDialogFragment<T, B>() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?) = BottomSheetDialog(requireContext(), theme)
}