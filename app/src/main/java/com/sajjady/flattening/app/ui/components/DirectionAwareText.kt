package com.sajjady.flattening.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow

/**
 * Direction-aware Text copied from ProfilerLab (https://github.com/sajjadyousefnia/ProfilerLab).
 * It detects RTL characters (like Persian/Arabic) and aligns the content so mixed-language
 * descriptions stay readable.
 */
@Composable
fun DirectionAwareText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    expandHorizontallyWhenRtl: Boolean = true
) {
    val containsRtl = remember(text) { text.containsRtlCharacters() }
    val resolvedTextAlign = textAlign ?: if (containsRtl) TextAlign.End else null
    val resolvedModifier = if (containsRtl && expandHorizontallyWhenRtl) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }
    val resolvedStyle = if (containsRtl) {
        style.merge(TextStyle(textDirection = TextDirection.Rtl))
    } else {
        style
    }

    Text(
        text = text,
        modifier = resolvedModifier,
        style = resolvedStyle,
        color = color,
        fontWeight = fontWeight,
        textAlign = resolvedTextAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

private val RtlCharRegex = Regex("[\\u0591-\\u08FF]")

private fun String.containsRtlCharacters(): Boolean = RtlCharRegex.containsMatchIn(this)
