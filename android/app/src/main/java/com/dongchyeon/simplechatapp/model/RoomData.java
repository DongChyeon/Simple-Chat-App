package com.dongchyeon.simplechatapp.model;

public class RoomData {
    private String username;
    private String roomNumber;

    public RoomData(String username, String roomNumber) {
        this.username = username;
        this.roomNumber = roomNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
