package fr.rnbe.android.core.di.contract

import android.graphics.Bitmap
import androidx.annotation.ColorRes
import com.lmfr.applm.tech.core.R

interface ToolBarActivityView {
    var isToolbarVisible: Boolean

    fun setTitleToolBar(title: String, @ColorRes colorRes: Int = android.R.color.black)

    fun setSubTitleToolBar(subtitle: String)

    fun hideToolbarSeparator()

    fun showToolbarSeparator()

    fun setImageToolbar(image: Bitmap)

    fun setToolbarBrandInfo(image: String, title: String) {}

    fun hideImageToolbar()
}