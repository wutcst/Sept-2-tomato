package com.example.back.sys.mapper;

import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {
    Room findRoomByRoomId(Integer RoomID);

    void renewPlayer(Player player);
}
