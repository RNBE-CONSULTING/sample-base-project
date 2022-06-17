package fr.rnbe.android.core.di

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lmfr.applm.tech.core.R
import com.lmfr.applm.tech.core.di.contract.ChildFragmentView
import com.lmfr.applm.tech.core.di.contract.FragmentView
import com.lmfr.applm.tech.core.extensions.gone
import com.lmfr.applm.tech.core.extensions.isNotNull
import com.lmfr.applm.tech.core.extensions.visible
import fr.rnbe.android.core.extensions.safeStartActivityForResult
import javax.inject.Inject

abstract class BaseChildFragment<T : BasePresenter<*>, B : ViewBinding> : Fragment(), ChildFragmentView {
    @Inject
    lateinit var presenter: T

    lateinit var parentFragmentView: FragmentView

    lateinit var binding: B

    protected lateinit var progressView: View

    abstract fun getViewBinding(container: ViewGroup?, attachParent: Boolean): B

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val p = parentFragment
                ?: throw ClassCastException("You try to use a childFragment like a normal Fragment")
        if (p !is FragmentView) throw ClassCastException("The parentFragment of this fragment must be a child of FragmentView")
        parentFragmentView = p
    }

    override fun changeFragment(fragment: BaseChildFragment<*, *>) {
        parentFragmentView.changeChildFragment(fragment)
    }

    override fun changeParentFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean) {
        parentFragmentView.changeFragment(fragment, addToBackStack)
    }

    override fun showDialog(dialog: BaseDialogFragment<*, *>, tag: String) {
        parentFragmentView.showDialog(dialog, tag)
    }

    override fun changeActivity(i: Intent, finishActivity: Boolean) {
        parentFragmentView.changeActivity(i, finishActivity)
    }

    override fun changeActivity(classActivity: Class<out BaseActivity<*, *, *>>, finishActivity: Boolean, extras: Bundle?, intentEditor: ((Intent) -> Unit)?) {
        parentFragmentView.changeActivity(classActivity, finishActivity, extras, intentEditor)
    }

    override fun changeActivity(action: String, intentEditor: ((Intent) -> Unit)?) {
        parentFragmentView.changeActivity(action, intentEditor)
    }

    /**
     * Be careful with caller of startActivityForResult.
     * onActivityResult will be notify inside the caller
     *
     * Don't start the activity through parentFragmentView.
     */
    override fun changeActivityForResult(i: Intent, requestCode: Int, intentEditor: ((Intent) -> Unit)?, isForResultInFragment: Boolean) {
        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode, isForResultInFragment = isForResultInFragment)
    }

    override fun changeActivityForResult(classActivity: Class<out BaseActivity<*, *, *>>,
                                         requestCode: Int,
                                         extras: Bundle?,
                                         intentEditor: ((Intent) -> Unit)?,
                                         isForResultInFragment: Boolean) {
        val i = Intent(requireContext(), classActivity)
        extras?.let { i.putExtras(extras) }

        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode, isForResultInFragment)
    }

    override fun changeActivityForResult(action: String, requestCode: Int, isForResultInFragment: Boolean, intentEditor: ((Intent) -> Unit)?) {
        val i = Intent(action)
        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode, isForResultInFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        progressView = layoutInflater.inflate(R.layout.activity_progress, (view as? ViewGroup), false)
        binding = getViewBinding(container, false)
        return binding.root
    }

    override fun showProgress() {
        if (isAdded) {
            progressView.isClickable = true

            try {
                //Show only if the current view is not displayed
                (view as? ViewGroup)?.also {
                    if (it is NestedScrollView && it.childCount == 1) {
                        if ((it[0] as ViewGroup).contains(progressView).not()) {
                            (it[0] as ViewGroup).addView(progressView)
                        }
                    } else if (it.contains(progressView).not()) {
                        it.addView(progressView)
                    }
                    progressView.visible()
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    override fun hideProgress() {
        progressView.gone()
    }

    override fun isProgressVisible(): Boolean = progressView?.parent.isNotNull()

}