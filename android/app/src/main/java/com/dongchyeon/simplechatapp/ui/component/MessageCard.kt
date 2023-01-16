package com.dongchyeon.simplechatapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dongchyeon.simplechatapp.data.model.Chat
import com.dongchyeon.simplechatapp.ui.theme.Black
import com.dongchyeon.simplechatapp.ui.theme.DarkGray
import com.dongchyeon.simplechatapp.ui.theme.Gray
import com.dongchyeon.simplechatapp.ui.theme.SimpleChatAppTheme
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun LeftMessageCard(msg: Chat, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = msg.from,
            color = Black,
            fontSize = 12.sp
        )

        Spacer(modifier = modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFFF5F6F9),
                elevation = 1.dp
            ) {
                Text(
                    text = msg.content,
                    color = DarkGray,
                    fontSize = 12.sp,
                    modifier = modifier.padding(8.dp)
                )
            }

            Spacer(modifier = modifier.width(8.dp))

            Text(
                text = msg.getFormattedTime(msg.sendTime),
                color = Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun LeftImageCard(msg: Chat, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = msg.from,
            color = Black,
            fontSize = 12.sp
        )

        Spacer(modifier = modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFFF5F6F9),
                elevation = 1.dp,
                modifier = modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                GlideImage(
                    imageModel = msg.content,
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = modifier.width(8.dp))

            Text(
                text = msg.getFormattedTime(msg.sendTime),
                color = Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun CenterMessageCard(msg: Chat, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = msg.content,
            color = Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun RightMessageCard(msg: Chat, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = msg.getFormattedTime(msg.sendTime),
                color = Gray,
                fontSize = 10.sp
            )

            Spacer(modifier = modifier.width(8.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFF3B3E4A),
                elevation = 1.dp,
            ) {
                Text(
                    text = msg.content,
                    color = Gray,
                    fontSize = 12.sp,
                    modifier = modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun RightImageCard(msg: Chat, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = msg.getFormattedTime(msg.sendTime),
                color = Gray,
                fontSize = 10.sp
            )

            Spacer(modifier = modifier.width(8.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFF3B3E4A),
                elevation = 1.dp,
                modifier = modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                GlideImage(
                    imageModel = msg.content,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewMessageCard() {
    SimpleChatAppTheme {
        Column {
            CenterMessageCard(
                msg = Chat("ENTER", "사용자1", "사용자2", " 사용자1 님이 입장하셨습니다.", 0)
            )
            LeftMessageCard(
                msg = Chat("MESSAGE", "사용자2", "사용자1", "상대방이 보낸 메시지", 0)
            )
            RightMessageCard(
                msg = Chat("MESSAGE", "사용자1", "사용자2", "내가 보낸 메시지", 0)
            )
        }
    }
}