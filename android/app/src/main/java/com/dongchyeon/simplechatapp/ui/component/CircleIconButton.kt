package com.dongchyeon.simplechatapp.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dongchyeon.simplechatapp.ui.theme.Black

@Composable
fun CircleIconButton(
    @DrawableRes drawable: Int,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(drawable),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = modifier
                .background(
                    color = Black,
                    shape = CircleShape
                )
                .padding(
                    all = 8.dp
                )
        )
    }
}