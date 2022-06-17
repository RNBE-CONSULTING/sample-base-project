package fr.rnbe.android.core.di.contract

import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import fr.rnbe.android.core.di.BaseFragment
import com.lmfr.applm.tech.core.model.MessageView

interface FragmentContainerView: BaseView {
    fun changeFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean = true, transition: Boolean = false)
    fun changeFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean = true,
                       @AnimRes enterTransition: Int, @AnimRes exitTransition: Int,
                       @AnimRes popEnterTransition: Int, @AnimRes popExitTransition: Int)
    fun popFragment()
    fun popFragment(tag: String?)

    fun showMessage(messageView: MessageView)
    fun showMessage(@StringRes messageId: Int)
}