package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    buttonWidth: Dp? = null,
    buttonHeight: Dp? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (buttonWidth != null) Modifier.width(buttonWidth) else Modifier.wrapContentWidth())
            .then(if (buttonHeight != null) Modifier.height(buttonHeight) else Modifier.height(48.dp)), // Ispravljeno
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = padding
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(text = text, style = textStyle)
    }
}

