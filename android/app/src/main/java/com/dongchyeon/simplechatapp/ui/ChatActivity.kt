package com.dongchyeon.simplechatapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dongchyeon.simplechatapp.R
import com.dongchyeon.simplechatapp.data.socket.model.Chat
import com.dongchyeon.simplechatapp.ui.component.CenterMessageCard
import com.dongchyeon.simplechatapp.ui.component.Conversation
import com.dongchyeon.simplechatapp.ui.component.LeftMessageCard
import com.dongchyeon.simplechatapp.ui.component.RightMessageCard
import com.dongchyeon.simplechatapp.ui.theme.Black
import com.dongchyeon.simplechatapp.ui.theme.SimpleChatAppTheme
import com.dongchyeon.simplechatapp.ui.theme.White
import com.dongchyeon.simplechatapp.ui.viewmodel.ChatViewModel
import com.dongchyeon.simplechatapp.util.TakePictureFromCameraOrGallery
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private val chatViewModel: ChatViewModel by viewModels()
    private val getImageContent =
        registerForActivityResult(TakePictureFromCameraOrGallery()) { result: String? ->
            if (result != null) {
                chatViewModel.sendImage(result)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleChatAppTheme {
                ChatScreen(chatViewModel)
            }
        }
    }

    @Composable
    fun ChatScreen(chatViewModel: ChatViewModel) {
        val chats: List<Chat> by chatViewModel.chat.observeAsState(listOf())

        Scaffold(
            bottomBar = {
                InputBox()
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Conversation(chats)
                }
            }
        )
    }

    @Composable
    fun InputBox() {
        Box(
            modifier = Modifier
                .background(color = Color(0xFFE8E8F1))
                .border(
                    width = 1.dp,
                    color = Color(0xFFF7F7Fa)
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(8.dp)
                    )

                    IconButton(
                        onClick = { getImageContent.launch(Unit) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_image),
                            //imageVector = ImageVector.vectorResource(R.drawable.image_button),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .background(
                                    color = Black,
                                    shape = CircleShape
                                )
                                .padding(
                                    all = 8.dp
                                )
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    var msgContent by remember { mutableStateOf("") }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        BasicTextField(
                            value = msgContent,
                            onValueChange = { msgContent = it },
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Black
                            ),
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier
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
                                            text = "메시지를 입력해주세요",
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

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Button(
                        onClick = {
                            if (msgContent.isNotEmpty()) {
                                chatViewModel.sendMessage(msgContent)
                                msgContent = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Text(
                            "전송",
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .wrapContentHeight()
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewChatScreen() {
        SimpleChatAppTheme {
            Scaffold(
                bottomBar = {
                    InputBox()
                },
                content = { padding ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                    ) {
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
            )
        }
    }
}
