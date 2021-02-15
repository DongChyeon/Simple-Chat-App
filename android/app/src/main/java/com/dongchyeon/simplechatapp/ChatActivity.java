package com.dongchyeon.simplechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dongchyeon.simplechatapp.databinding.ActivityChatBinding;
import com.dongchyeon.simplechatapp.model.MessageData;
import com.dongchyeon.simplechatapp.model.RoomData;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    private Socket mSocket;
    private String username;
    private String roomNumber;
    private ChatAdapter adapter;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        try {
            mSocket = IO.socket("http://10.0.2.2:80");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomNumber = intent.getStringExtra("roomNumber");

        adapter = new ChatAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        binding.sendBtn.setOnClickListener(v -> sendMessage());

        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, args -> mSocket.emit("subscribe", gson.toJson(new RoomData(username, roomNumber))));
        mSocket.on("update", (Emitter.Listener) args -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            Log.d("UPDATE", data.toString());
            addChat(data);
        });
    }

    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_CONTENT));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                if (username.equals(data.getFrom())) {
                    adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.RIGHT_CONTENT));
                    binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                } else {
                    adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_CONTENT));
                    binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
    }

    private void sendMessage() {
        mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                username,
                roomNumber,
                binding.contentEdit.getText().toString(),
                System.currentTimeMillis())));
        Log.d("MESSAGE", new MessageData("MESSAGE",
                username,
                roomNumber,
                binding.contentEdit.getText().toString(),
                System.currentTimeMillis()).toString());
        binding.contentEdit.setText("");
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("unsubscribe", gson.toJson(new RoomData(username, roomNumber)));
        mSocket.disconnect();
    }
}