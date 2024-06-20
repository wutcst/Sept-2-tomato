package com.example.back.sys.service.impl;


import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import com.example.back.sys.mapper.ItemMapper;
import com.example.back.sys.mapper.PlayerMapper;
import com.example.back.sys.mapper.RoomMapper;
import com.example.back.sys.service.IRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomService implements IRoomService {

    @Resource
    private RoomMapper roomMapper;

    @Override
    public Room findRoomByRoomId(Integer currentRoomID) {
        return roomMapper.findRoomByRoomId(currentRoomID);
    }

    @Override
    public void updatePlayer(Player player) {
        roomMapper.renewPlayer(player);
    }
}
