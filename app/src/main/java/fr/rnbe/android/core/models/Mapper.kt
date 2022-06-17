package fr.rnbe.android.core.models

import android.util.Log


abstract class MapperUi<F : Any, T> {
    fun convert(item: F): T? {
        return item.takeIf { validator(it) }?.let { map(it) }
    }

    fun convertList(items: List<F>): List<T> {
        return items.mapNotNull { convert(it) }
    }

    /**
     * @see use #convert method, for check validator before.
     */
    protected abstract fun map(item: F): T
    protected abstract fun validator(item: F): Boolean
}

interface Mapper<F : Any, T> {
    fun map(item: F): T?
    fun validator(item: F): Boolean = true
}

abstract class MapperFail<F : Any, T> : Mapper<F, T> {
    class Mapping<OUT>(val result: OUT? = null, val fails: ArrayList<String> = arrayListOf())

    private val fails: ArrayList<String> = arrayListOf()

    fun mapping(item: F): Mapping<T> {
        return Mapping(map(item), fails)
    }

    fun fail(cause: String) {
        Log.w("Mapping fail", cause)
        fails.add(cause)
    }
}

interface Validate<T> {
    fun isValid(item: T): Boolean
}