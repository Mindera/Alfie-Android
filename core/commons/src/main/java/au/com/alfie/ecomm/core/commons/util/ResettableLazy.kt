package au.com.alfie.ecomm.core.commons.util

interface ResettableLazy<out T> : Lazy<T> {

    fun reset()
}
