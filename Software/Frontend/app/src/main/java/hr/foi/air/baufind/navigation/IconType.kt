package hr.foi.air.baufind.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class IconType{
    data class Vector(val imageVector: ImageVector) : IconType()
    data class Drawable(val drawableId: Int) : IconType()
    }

