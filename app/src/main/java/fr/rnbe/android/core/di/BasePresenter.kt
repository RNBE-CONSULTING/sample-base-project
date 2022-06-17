package fr.rnbe.android.core.di

import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import fr.rnbe.android.core.di.contract.BaseView
import fr.rnbe.android.core.extensions.TAG
import fr.rnbe.android.core.extensions.observeOnMainThread
import fr.rnbe.android.core.extensions.subscribeOnIo
import fr.rnbe.android.core.extensions.subscribeOnMainThread
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.addTo
import java.lang.ref.WeakReference

open class BasePresenter<T : BaseView>(v: T) {

    val view: T? by lazy { targetView.get() }

    private val targetView: WeakReference<T> = WeakReference(v)
    protected var compositeDisposable = CompositeDisposable()

    @CallSuper
    open fun onAttach() {
    }

    @CallSuper
    open fun onCreate() {
    }

    @CallSuper
    open fun onViewCreated() {
        // called only when presenter is linked to an activity
        if(compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
    }

    @CallSuper
    open fun onStart() {
    }

    @CallSuper
    open fun onResume() {
    }

    @CallSuper
    open fun onPause() {
    }

    @CallSuper
    open fun onStop() {
        compositeDisposable.clear()
    }

    @CallSuper
    open fun onDestroyView() {
        compositeDisposable.dispose()
    }

    @CallSuper
    open fun onDestroy() {
    }

    @CallSuper
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    @CallSuper
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

    }

    fun <O> subscribeOnUi(observable: Observable<O>, onNext: Consumer<in O>, error: Consumer<in Throwable> = Functions.emptyConsumer()) {
        observable
                .subscribeOnMainThread()
                .observeOnMainThread()
                .subscribe(onNext, error)
                .addTo(compositeDisposable)
    }

    fun <O> subscribeOnIo(observable: Observable<O>, onNext: Consumer<in O>, error: Consumer<Throwable> = Functions.emptyConsumer()) {
        observable
                .subscribeOnIo()
                .observeOnMainThread()
                .subscribe(onNext, error)
                .addTo(compositeDisposable)
    }

    fun <O> subscribeOnIo(single: Single<O>, onNext: Consumer<in O>, error: Consumer<Throwable> = Functions.emptyConsumer()) {
        subscribeOnIo(single.toObservable(), onNext, error)
    }


    fun <O> Observable<O>.subscribeOnUI(onError: ((Throwable) -> Unit) = { Log.w(TAG, "An error occurred", it) }, onNext: ((O) -> Unit)) {
        this.subscribeOnMainThread().subscribe({ onNext.invoke(it) }, { onError.invoke(it) }).addTo(compositeDisposable)
    }
}