package com.example.back.sys.service;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;

import java.util.List;

public interface IRoomService {
    Room findRoomByRoomId(Integer RoomID);

    void updatePlayer(Player player);

    List<Item> checkItemsInRoom(Integer roomID);
}
