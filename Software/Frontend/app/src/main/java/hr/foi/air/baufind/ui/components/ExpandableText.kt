package hr.foi.air.baufind.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ExpandableText(
    modifier: Modifier,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    text: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = text,
        overflow = overflow,
        maxLines = if (expanded) Int.MAX_VALUE else 1,
        modifier = modifier
            .clickable { expanded = !expanded }
            .animateContentSize()
    )
}