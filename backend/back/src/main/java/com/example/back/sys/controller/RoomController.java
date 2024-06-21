package com.example.back.sys.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.back.sys.Result;
import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import com.example.back.sys.service.impl.PlayerService;
import com.example.back.sys.service.impl.RoomService;
import com.example.back.sys.token.IntercepterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private PlayerService playerService;

    private Result move(String playerName, Integer direction) {
        Player player = playerService.findByPlayerName(playerName);
        Room curRoom = roomService.findRoomByRoomId(player.getCurrentRoomID());

        Integer nextRoomID;

        switch (direction) {
            case 0:
                nextRoomID = curRoom.getEastRoomID();
                break;
            case 1:
                nextRoomID = curRoom.getSouthRoomID();
                break;
            case 2:
                nextRoomID = curRoom.getWestRoomID();
                break;
            case 3:
                nextRoomID = curRoom.getNorthRoomID();
                break;
            default:
                return Result.fail("不存在该方向！");
        }

        Room nextRoom = roomService.findRoomByRoomId(nextRoomID);
        if (nextRoom == null) return Result.fail("该方向上无房间");
        player.setCurrentRoomID(nextRoomID);

        roomService.updatePlayer(player);

        return Result.success(nextRoom);
    }

    @PostMapping("/go")
    public Result go(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        int direction = (int) request.get("direction");

        return move(playerName, direction);
    }

    @PostMapping("/back")
    public Result back(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        int direction = (int) request.get("direction");

        return move(playerName, direction);
    }

    @GetMapping("/check")
    public Result getItemsInRoom(@RequestParam("RoomID") Integer roomID){
        Room curRoom = roomService.findRoomByRoomId(roomID);
        if(curRoom==null) return Result.fail("不存在该房间");

        List<Item> items =  roomService.checkItemsInRoom(roomID);
        System.out.println(items);
        return Result.success(items);
    }


}
