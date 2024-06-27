package com.example.back.sys.service.impl;


import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import com.example.back.sys.mapper.ItemMapper;
import com.example.back.sys.mapper.PlayerMapper;
import com.example.back.sys.mapper.RoomMapper;
import com.example.back.sys.service.IRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 房间服务的实现类，提供对房间信息和房间内物品的操作服务。
 */
@Service
public class RoomService implements IRoomService {

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private ItemMapper itemMapper;

    @Override
    public Room findRoomByRoomId(Integer currentRoomID) {
        return roomMapper.findRoomByRoomId(currentRoomID);
    }

    @Override
    public void updatePlayer(Player player) {
        roomMapper.renewPlayer(player);
    }

    @Override
    public List<Item> checkItemsInRoom(Integer roomID) {
        List<Object> ll = roomMapper.checkItemsInRoom(roomID);
        List<Item> res = new ArrayList<>();
        for (Object i : ll) {
            res.add(itemMapper.getItemById((Integer) i));
        }

        return res;
    }
}
