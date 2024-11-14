package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import hr.foi.air.baufind.ui.theme.BaufindTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    drawableId : Int? = null,
    iconDescription: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    buttonWidth: Dp? = null,
    buttonHeight: Dp? = null,
    maxWidth: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (buttonWidth != null) Modifier.width(buttonWidth) else Modifier.wrapContentWidth())
            .then(if (maxWidth) Modifier.fillMaxWidth() else Modifier)
            .then(if (buttonHeight != null) Modifier.height(buttonHeight) else Modifier.height(48.dp)),
        enabled = enabled,
        contentPadding = padding
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        if(drawableId != null){
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = iconDescription,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(text = text, style = textStyle)
    }
}


