package au.com.alfie.ecomm.core.ui.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

fun String.highlightTerm(
    term: String,
    highlightStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
): AnnotatedString = buildAnnotatedString {
    append(this@highlightTerm)

    term
        .toRegex(RegexOption.IGNORE_CASE)
        .findAll(this@highlightTerm)
        .forEach {
            addStyle(highlightStyle, it.range.first, it.range.last + 1)
        }
}

fun String.toColor(): Color? {
    val hex = if (startsWith("#").not()) "#$this" else this

    return try {
        Color(hex.toColorInt())
    } catch (_: IllegalArgumentException) {
        null
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun HighlightTermPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "Lorem ipsum".highlightTerm("Lor"))
        Text(text = "Lorem ipsum".highlightTerm("Lorem"))
        Text(text = "Lorem ipsum".highlightTerm("um"))
        Text(text = "Lorem ipsum".highlightTerm("Lorem ipsum"))
        Text(text = "Lorem ipsum Lorem ipsum".highlightTerm("Lorem"))
    }
}
