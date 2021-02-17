package com.dongchyeon.simplechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.dongchyeon.simplechatapp.databinding.ActivityChatBinding;
import com.dongchyeon.simplechatapp.model.MessageData;
import com.dongchyeon.simplechatapp.model.RoomData;
import com.dongchyeon.simplechatapp.retrofit.Result;
import com.dongchyeon.simplechatapp.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    private Socket mSocket;
    private RetrofitClient retrofitClient;

    private String username;
    private String roomNumber;
    private ChatAdapter adapter;

    private Gson gson = new Gson();

    private final int SELECT_IMAGE = 100;

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
        retrofitClient = RetrofitClient.getInstance();

        // MainActivity로부터 username과 roomNumber를 넘겨받음
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomNumber = intent.getStringExtra("roomNumber");

        adapter = new ChatAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        // 메시지 전송 버튼
        binding.sendBtn.setOnClickListener(v -> sendMessage());
        // 이미지 전송 버튼
        binding.imageBtn.setOnClickListener(v -> {
            Intent imageIntent = new Intent(Intent.ACTION_PICK);
            imageIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(imageIntent, SELECT_IMAGE);
        });

        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, args -> {
            mSocket.emit("enter", gson.toJson(new RoomData(username, roomNumber)));
        });
        mSocket.on("update", args -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            addChat(data);
        });
    }

    // 리사이클러뷰에 채팅 추가
    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (data.getType().equals("IMAGE")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_IMAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
        adapter.addItem(new ChatItem(username, binding.contentEdit.getText().toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE));
        binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        binding.contentEdit.setText("");
    }

    // 이미지 uri로부터 실제 파일 경로를 알아냄
    private String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();

        return result;
    }

    // Node.js 서버에 이미지를 업로드
    public void uploadImage(Uri imageUri, Context context) {
        File image = new File(getRealPathFromURI(imageUri, context));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), image);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestBody);

        retrofitClient.getApiService().uploadImage(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result.getResult() == 1) {
                    Log.d("PHOTO", "Upload success : " + result.getImageUri());
                    sendImage(result.getImageUri());
                } else {
                    Log.d("PHOTO", "Upload failed");
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("PHOTO", "Upload failed : " + t.getMessage());
            }
        });
    }

    private void sendImage(String imageUri) {
        mSocket.emit("newImage", gson.toJson(new MessageData("IMAGE",
                username,
                roomNumber,
                imageUri,
                System.currentTimeMillis())));
        Log.d("IMAGE", new MessageData("IMAGE",
                username,
                roomNumber,
                imageUri,
                System.currentTimeMillis()).toString());
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    // 이미지를 갤러리에서 선택했을 때 이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri;

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadImage(selectedImageUri, getApplicationContext());
            adapter.addItem(new ChatItem(username, String.valueOf(selectedImageUri), toDate(System.currentTimeMillis()), ChatType.RIGHT_IMAGE));
            binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    // 이전 버튼을 누를 시 방을 나가고 소켓 통신 종료
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(username, roomNumber)));
        mSocket.disconnect();
    }
}