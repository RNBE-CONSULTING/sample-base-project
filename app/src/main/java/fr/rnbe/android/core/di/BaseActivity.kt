package fr.rnbe.android.core.di

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import fr.rnbe.android.core.di.contract.ActivityView
import fr.rnbe.android.core.di.contract.FragmentView
import fr.rnbe.android.core.R
import javax.inject.Inject

abstract class BaseActivity<T : BasePresenter<*>, B : ViewBinding, N : Navigator> :
        TranslationActivity(), ActivityView {

    @Inject
    lateinit var presenter: T

    var binding: B? = null

    @Inject
    lateinit var navigator: N

    override fun getActivityNavigator(): Navigator = navigator

    private var progressView: View? = null

    abstract fun getViewBinding(): B?

    override fun getContext(): Context? = this

    @IdRes
    open fun getContainerFragmentId(): Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        binding?.let { binding ->
            setContentView(binding.root)
        }

        navigator.installIn(getContainerFragmentId())

        //Call Presenter
        presenter.onCreate()
        presenter.onViewCreated()
    }


    override fun onStart() {
        super.onStart()
        //Call Presenter
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()

        //Call Presenter
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()

        //Call Presenter
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()

        //Call Presenter
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        //Call Presenter
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

    override fun changeActivity(i: Intent, finishActivity: Boolean) {
        safeStartActivity(i) { if (finishActivity) finish() }
    }

    override fun changeActivity(
        classActivity: Class<out BaseActivity<*, *, *>>,
        finishActivity: Boolean,
        extras: Bundle?,
        intentEditor: ((Intent) -> Unit)?
    ) {
        val i = Intent(this, classActivity)
        extras?.let { i.putExtras(extras) }
        intentEditor?.invoke(i)

        safeStartActivity(i) { if (finishActivity) finish() }
    }

    override fun changeActivityForResult(i: Intent, requestCode: Int, intentEditor: ((Intent) -> Unit)?, isForResultInFragment: Boolean) {
        intentEditor?.invoke(i)
        safeStartActivityForResult(i, requestCode)
    }

    override fun changeActivityForResult(
        classActivity: Class<out BaseActivity<*, *, *>>,
        requestCode: Int, extras: Bundle?,
        intentEditor: ((Intent) -> Unit)?,
        isForResultInFragment: Boolean
    ) {
        val i = Intent(this, classActivity)
        extras?.let { i.putExtras(extras) }
        intentEditor?.invoke(i)

        safeStartActivityForResult(i, requestCode)
    }

    override fun changeActivityForResult(action: String, requestCode: Int, isForResultInFragment: Boolean, intentEditor: ((Intent) -> Unit)?) {
        val i = Intent(action)
        intentEditor?.invoke(i)

        safeStartActivityForResult(i, requestCode, isForResultInFragment)
    }

    override fun changeFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean, transition: Boolean) {
        navigator.navigate(fragment, addToBackStack, transition)
    }

    override fun changeFragment(fragment: BaseFragment<*, *>, addToBackStack: Boolean, enterTransition: Int, exitTransition: Int, popEnterTransition: Int, popExitTransition: Int) {
        navigator.navigate(fragment, addToBackStack, enterTransition, exitTransition, popEnterTransition, popExitTransition)
    }

    override fun showDialog(dialog: BaseDialogFragment<*, *>, tag: String) {
        navigator.showDialog(dialog, tag)
    }

    override fun changeActivity(action: String, intentEditor: ((Intent) -> Unit)?) {
        val i = Intent(action)
        intentEditor?.invoke(i)
        safeStartActivity(i)
    }

    override fun hideKeyboard() {
        currentFocus?.windowToken?.apply { getInputMethodManager().hideSoftInputFromWindow(this, 0) }
    }

    override fun onBackPressed() {
        if ((supportFragmentManager.fragments.lastOrNull() as? FragmentView)?.onBackPressed() == true) {
            Log.d(TAG, "onBackPressed was handle by fragment")
            return
        }

        if (navigator.onBackPressed())
            return

        super.onBackPressed()
    }

    override fun popFragment(tag: String?) {
        supportFragmentManager.popBackStackImmediate(tag, 0)
    }

    override fun popFragment() {
        //Need to override
    }

    override fun showProgress() {
        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as? ViewGroup

        if (progressView.isNull()) {
            progressView = layoutInflater.inflate(R.layout.activity_progress, rootView, false) //Don't add to root view, it's just initialization
            progressView?.isClickable = true
        }

        //Show only if the current view is not displayed
        if (progressView?.parent.isNull()) {
            rootView?.addView(progressView)
        }
    }

    override fun hideProgress() {
        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as? ViewGroup
        rootView?.removeView(progressView)
    }

    override fun isProgressVisible(): Boolean = progressView?.parent.isNotNull()

    override fun showMessage(messageView: MessageView) {
        this.showMessageView(messageView)
    }

    override fun showMessage(messageId: Int) {
        toast(messageId)
    }

    override fun isInForeground() = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
}