package com.example.back.sys.controller;

import com.example.back.sys.Result;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import com.example.back.sys.service.impl.PlayerService;
import com.example.back.sys.service.impl.RoomService;
import com.example.back.sys.token.IntercepterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;


@Controller
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private PlayerService playerService;

    @PostMapping("/go")
    public Result go(String playerName,Integer direction){

        Player player = playerService.findByPlayerName(playerName);
        Room curRoom = roomService.findRoomByRoomId(player.getCurrentRoomID());

        Integer nextRoomID;

        switch (direction){
            case 0: nextRoomID = curRoom.getEastRoomID(); break;
            case 1: nextRoomID = curRoom.getSouthRoomID(); break;
            case 2: nextRoomID = curRoom.getWestRoomID(); break;
            case 3: nextRoomID = curRoom.getNorthRoomID(); break;
            default: return Result.fail("不存在该方向！");
        }

        Room nextRoom = roomService.findRoomByRoomId(nextRoomID);
        if(nextRoom==null) return Result.fail("该方向上无房间");
        player.setCurrentRoomID(nextRoomID);

        roomService.updatePlayer(player);

        return Result.success(nextRoom);
    }

    @PostMapping("/back")
    public Result back(String playerName,Integer direction){
        Player player = playerService.findByPlayerName(playerName);
        Room curRoom = roomService.findRoomByRoomId(player.getCurrentRoomID());

        Integer originRoomID;

        switch (direction){
            case 0: originRoomID = curRoom.getEastRoomID(); break;
            case 1: originRoomID = curRoom.getSouthRoomID(); break;
            case 2: originRoomID = curRoom.getWestRoomID(); break;
            case 3: originRoomID = curRoom.getNorthRoomID(); break;
            default: return Result.fail("不存在该方向！");
        }

        Room originRoom = roomService.findRoomByRoomId(originRoomID);
        if(originRoom==null) return Result.fail("该方向上无房间");
        player.setCurrentRoomID(originRoomID);

        roomService.updatePlayer(player);

        return Result.success(originRoom);

    }

}
