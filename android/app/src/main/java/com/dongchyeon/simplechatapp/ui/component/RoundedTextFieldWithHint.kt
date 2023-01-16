package com.dongchyeon.simplechatapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dongchyeon.simplechatapp.ui.theme.Black
import com.dongchyeon.simplechatapp.ui.theme.White

@Composable
fun BasicTextFieldWithHint(
    msgContent: String,
    hint: String,
    onValueChange: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = msgContent,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Black
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFe8e8f1),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (msgContent.isEmpty()) {
                    Text(
                        text = hint,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        }
    )
}