package au.com.alfie.ecomm.core.commons.util

import java.util.concurrent.atomic.AtomicReference

fun <T> resettableLazy(initializer: () -> T): ResettableLazy<T> = ResettableLazyDelegate(initializer)

@Suppress("UNCHECKED_CAST")
private class ResettableLazyDelegate<out T>(val initializer: () -> T) : ResettableLazy<T> {

    private object UnitializedValue

    private val valueReference = AtomicReference<Any>(UnitializedValue)

    override val value: T
        get() {
            val valueAux = valueReference.get()
            return if (valueAux !== UnitializedValue) {
                valueAux as T
            } else {
                val typedValue = initializer()
                valueReference.lazySet(typedValue)
                typedValue
            }
        }

    override fun reset() {
        valueReference.lazySet(UnitializedValue)
    }

    override fun isInitialized(): Boolean = valueReference.get() !== UnitializedValue

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}
