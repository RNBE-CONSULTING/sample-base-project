package fr.rnbe.android.core.extensions

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Single<T>.subscribeOnIo(): Single<T> = this.subscribeOn(Schedulers.io())
fun <T> Observable<T>.subscribeOnIo(): Observable<T> = this.subscribeOn(Schedulers.io())
fun <T> Flowable<T>.subscribeOnIo(): Flowable<T> = this.subscribeOn(Schedulers.io())
fun Completable.subscribeOnIo(): Completable = this.subscribeOn(Schedulers.io())
fun <T> Maybe<T>.subscribeOnIo(): Maybe<T> = this.subscribeOn(Schedulers.io())

fun <T> Single<T>.subscribeOnMainThread(): Single<T> = this.subscribeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.subscribeOnMainThread(): Observable<T> = this.subscribeOn(AndroidSchedulers.mainThread())
fun <T> Flowable<T>.subscribeOnMainThread(): Flowable<T> = this.subscribeOn(AndroidSchedulers.mainThread())
fun Completable.subscribeOnMainThread(): Completable = this.subscribeOn(AndroidSchedulers.mainThread())
fun <T> Maybe<T>.subscribeOnMainThread(): Maybe<T> = this.subscribeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.observeOnIo(): Single<T> = this.observeOn(Schedulers.io())
fun <T> Observable<T>.observeOnIo(): Observable<T> = this.observeOn(Schedulers.io())
fun <T> Flowable<T>.observeOnIo(): Flowable<T> = this.observeOn(Schedulers.io())
fun Completable.observeOnIo(): Completable = this.observeOn(Schedulers.io())
fun <T> Maybe<T>.observeOnIo(): Maybe<T> = this.observeOn(Schedulers.io())

fun <T> Single<T>.observeOnMainThread(): Single<T> = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.observeOnMainThread(): Observable<T> = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Flowable<T>.observeOnMainThread(): Flowable<T> = this.observeOn(AndroidSchedulers.mainThread())
fun Completable.observeOnMainThread(): Completable = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Maybe<T>.observeOnMainThread(): Maybe<T> = this.observeOn(AndroidSchedulers.mainThread())



fun <T : Any> T.toObservable(): Observable<T> = Observable.just(this)
fun <T : Any> T.toSingle(): Single<T> = Single.just(this)

fun <T> Single<T>.subscribeEmpty():Disposable = this.subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())


fun <T> Observable<T>.delayEach(interval: Long, timeUnit: TimeUnit): Observable<T> =
        Observable.zip(this, Observable.interval(interval, timeUnit), { item, _ -> item })