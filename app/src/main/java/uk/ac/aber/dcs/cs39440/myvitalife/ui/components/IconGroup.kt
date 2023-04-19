package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a group of icons with a filled and outline version, along with a label.
 *
 * @property filledIcon The ImageVector for the filled version of the icon.
 * @property outlineIcon The ImageVector for the outline version of the icon.
 * @property label The label for the icon group.
 */
data class IconGroup(
    val filledIcon: ImageVector,
    val outlineIcon: ImageVector,
    val label: String
)
