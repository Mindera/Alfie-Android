package au.com.alfie.ecomm.core.commons.string

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class StringResource : Parcelable {

    companion object {
        fun fromText(text: String): StringResource = TextResource(text)

        fun fromId(@StringRes id: Int, args: List<Any> = emptyList()): StringResource = IdResource(id, args)

        val EMPTY = fromText("")
    }

    internal data class TextResource(val text: String) : StringResource()

    internal data class IdResource(
        @StringRes val id: Int,
        val args: @RawValue List<Any> = emptyList()
    ) : StringResource()
}

fun StringResource.toString(context: Context): String {
    return when (this) {
        is StringResource.TextResource -> text
        is StringResource.IdResource -> when {
            id == -1 -> ""
            args.isEmpty() -> context.getString(id)
            else -> {
                val args: List<Any> = args.format(context)
                context.getString(id).format(args = args.toTypedArray())
            }
        }
    }
}

fun StringResource?.toStringOrEmpty(context: Context): String {
    return this?.toString(context).orEmpty()
}

fun StringResource?.toStringOrEmpty(): String {
    return (this as? StringResource.TextResource)?.text.orEmpty()
}

private fun List<Any>.format(context: Context) = this.map {
    if (it is StringResource) {
        it.toString(context)
    } else {
        it
    }
}
