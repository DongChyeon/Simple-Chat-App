package com.dongchyeon.simplechatapp.ui.component

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dongchyeon.simplechatapp.ui.theme.Black
import com.dongchyeon.simplechatapp.ui.theme.White

@Composable
fun ButtonWithNoElevation(
    text: String,
    textSize: Int,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Black),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp
        ),
        modifier = modifier
    ) {
        Text(
            text,
            color = White,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
        )
    }
}