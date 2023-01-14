package com.dongchyeon.simplechatapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dongchyeon.simplechatapp.presentation.SimpleChatApp.Companion.roomName
import com.dongchyeon.simplechatapp.presentation.SimpleChatApp.Companion.userName
import com.dongchyeon.simplechatapp.ui.theme.Black
import com.dongchyeon.simplechatapp.ui.theme.SimpleChatAppTheme
import com.dongchyeon.simplechatapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContent {
            SimpleChatAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                ) {
                    Text(
                        text = "Simple ChatApp",
                        color = Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )

                    Spacer(
                        modifier = modifier
                            .height(32.dp)
                    )

                    val username = remember { mutableStateOf("") }
                    val roomname = remember { mutableStateOf("") }

                    TextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            cursorColor = Black,
                            focusedIndicatorColor = Black
                        ),
                        placeholder = {
                            Text("유저 이름")
                        },
                        modifier = modifier
                            .width(296.dp)
                            .wrapContentHeight()
                    )
                    TextField(
                        value = roomname.value,
                        onValueChange = { roomname.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            cursorColor = Black,
                            focusedIndicatorColor = Black
                        ),
                        placeholder = {
                            Text("방 이름")
                        },
                        modifier = modifier
                            .width(296.dp)
                            .wrapContentHeight()
                    )

                    Spacer(
                        modifier = modifier
                            .height(32.dp)
                    )

                    Button(
                        onClick = {
                            val intent = Intent(applicationContext, ChatActivity::class.java)
                            userName = username.value
                            roomName = roomname.value
                            startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                        modifier = modifier
                            .width(296.dp)
                            .wrapContentHeight()
                    ) {
                        Text(
                            "입장하기",
                            color = White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .wrapContentHeight()
                        )
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun ShowMainScreen() {
        SimpleChatAppTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                MainScreen()
            }
        }
    }
}