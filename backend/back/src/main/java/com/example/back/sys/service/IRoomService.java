package com.example.back.sys.service;

import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;

public interface IRoomService {
    Room findRoomByRoomId(Integer RoomID);

    void updatePlayer(Player player);
}
