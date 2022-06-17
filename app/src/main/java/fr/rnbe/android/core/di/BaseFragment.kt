package fr.rnbe.android.core.di

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.contains
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import fr.rnbe.android.core.di.contract.ActivityView
import fr.rnbe.android.core.di.contract.FragmentView
import fr.rnbe.android.core.di.contract.ToolBarActivityView
import fr.rnbe.android.core.extensions.isNotNull
import fr.rnbe.android.core.extensions.safeStartActivityForResult
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

abstract class BaseFragment<T : BasePresenter<*>, B : ViewBinding> : Fragment(), FragmentView {

    @Inject
    lateinit var presenter: T

    lateinit var parentActivity: ActivityView

    private var _binding: B? = null
    val binding get() = _binding!!

    /**
     * This attribute is used from BottomViewNavigator to determine whether the bottomNavigationView should
     * display the shadow or not. Sometimes we may have a footer in a fragment which is displayed on top
     * of the bottomNavigationView, and having the shadow results in a bad visual effect
     */
    open val hasFooter: Boolean = false

    /**
     * Use this when you are not sure that binding is != null, to avoid NullPointerException.
     * Keeping the !! operator on the get property of binding is good because there are some cases,
     * like in the onViewCreated fun, where binding is always != null, so having a non nullable property
     * makes the code cleaner. When an access to the views is needed from a callback or
     * from onSaveInstanceState, safeBinding should be used to avoid crashes.
     */
    fun safeBinding(safe: B.() -> Unit) {
        _binding?.safe()
    }

    protected var compositeDisposable = CompositeDisposable()

    abstract fun getViewBinding(container: ViewGroup?, attachParent: Boolean): B

    protected lateinit var progressView: View

    /**
     * override this on a fragment to customize the navigation
     */
    protected open val navigator: Navigator
        get() = if (parentFragment != null)
            (parentFragment as BaseFragment<*, *>).navigator
        else parentActivity.getActivityNavigator()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        progressView = layoutInflater.inflate(R.layout.activity_progress, (view as? ViewGroup), false)
        return getViewBinding(container, false).also { _binding = it }.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.onAttach()
        if (context !is ActivityView) throw ClassCastException("The parentActivity of this fragment must be a child of ActivityView")
        parentActivity = context
    }

    override fun parentActivity(): ActivityView {
        return parentActivity
    }

    override fun changeFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean, transition: Boolean) {
        navigator.navigate(fragment, addToBackStack, transition)
    }

    override fun changeFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean, enterTransition: Int, exitTransition: Int, popEnterTransition: Int, popExitTransition: Int) {
        navigator.navigate(fragment, addToBackStack, enterTransition, exitTransition, popEnterTransition, popExitTransition)
    }

    override fun popFragment(tag: String?) {
        parentActivity.popFragment(tag)
    }

    override fun changeChildFragment(fragment: BaseChildFragment<*, *>) {
        //Need To Override
    }

    override fun popFragment() {
        parentActivity.popFragment()
    }

    override fun changeActivity(i: Intent, finishActivity: Boolean) {
        parentActivity.changeActivity(i, finishActivity)
    }

    override fun changeActivity(classActivity: Class<out BaseActivity<*, *, *>>, finishActivity:
    Boolean, extras: Bundle?, intentEditor: ((Intent) -> Unit)?) {
        parentActivity.changeActivity(classActivity, finishActivity, extras, intentEditor)
    }

    override fun changeActivity(action: String, intentEditor: ((Intent) -> Unit)?) {
        parentActivity.changeActivity(action, intentEditor)
    }

    /**
     * Be careful with caller of startActivityForResult.
     * onActivityResult will be notify inside the caller
     *
     * Don't start the activity through parentActivity.
     */
    override fun changeActivityForResult(i: Intent, requestCode: Int,
                                         intentEditor: ((Intent) -> Unit)?, isForResultInFragment: Boolean) {
        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode, isForResultInFragment = isForResultInFragment)
    }

    override fun changeActivityForResult(classActivity: Class<out BaseActivity<*, *, *>>,
                                         requestCode: Int, extras: Bundle?,
                                         intentEditor: ((Intent) -> Unit)?,
                                         isForResultInFragment: Boolean) {
        val i = Intent(requireContext(), classActivity)
        extras?.let { i.putExtras(extras) }

        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode, isForResultInFragment)
    }

    override fun changeActivityForResult(action: String,
                                         requestCode: Int,
                                         isForResultInFragment: Boolean,
                                         intentEditor: ((Intent) -> Unit)?
                                         ) {
        val i = Intent(action)
        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode, isForResultInFragment)
    }

    override fun closeActivity() {
        parentActivity.finish()
    }

    override fun setActivityResult(resultCode: Int, data: Intent?) {
        parentActivity.setResult(resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onStart() {
        super.onStart()
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        presenter.onStart()
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
        compositeDisposable.clear()
        presenter.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        presenter.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun alertDialog(alertDialog: AlertDialog) {
        alertDialog.show()
    }

    override fun showDialog(dialog: BaseDialogFragment<*, *>, tag: String) {
        dialog.show(parentFragmentManager, tag.ifEmpty { dialog.javaClass.simpleName })
    }

    override fun attachFragmentResult(requestKey: String, result: (Bundle) -> Unit) {
        parentFragmentManager.setFragmentResultListener(requestKey, viewLifecycleOwner) { _, bundle ->
            result.invoke(bundle)
        }
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

    fun <T> subscribeOnUi(o: Observable<T>, onNext: Consumer<T>, error: Consumer<Throwable> = Consumer { }) {
        presenter.subscribeOnUi(o, onNext, error)
    }

    fun <T> subscribeOnIo(o: Observable<T>, onNext: Consumer<T>, error: Consumer<Throwable> = Consumer { }) {
        presenter.subscribeOnIo(o, onNext, error)
    }

    override fun updateToolbarTitle(titleId: Int) {
        (activity as? ToolBarActivityView)?.setTitleToolBar(getString(titleId))
    }

    override fun updateToolbarTitle(title: String) {
        (activity as? ToolBarActivityView)?.setTitleToolBar(title)
    }

    override fun showMessage(messageView: MessageView) {
        this.showMessageView(messageView)
    }

    override fun showMessage(messageId: Int) {
        toast(messageId)
    }
}