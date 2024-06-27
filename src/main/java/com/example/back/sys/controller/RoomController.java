package com.example.back.sys.controller;

import com.example.back.sys.Result;
import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import com.example.back.sys.service.impl.PlayerService;
import com.example.back.sys.service.impl.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PlayerService playerService;

    private static final Logger logger = Logger.getLogger(RoomController.class.getName());

    /**
     * 在指定方向移动玩家。
     *
     * @param playerName 玩家名称。
     * @param direction  移动的方向（0：东，1：南，2：西，3：北）。
     * @return 表示移动成功或失败的 Result 对象。
     */
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
                logger.warning("提供了无效的方向。");
                return Result.fail("不存在该方向！");
        }

        Room nextRoom = roomService.findRoomByRoomId(nextRoomID);
        if (nextRoom == null) {
            logger.warning("指定方向上没有房间。");
            return Result.fail("该方向上无房间");
        }
        player.setCurrentRoomID(nextRoomID);

        roomService.updatePlayer(player);

        return Result.success(nextRoom);
    }

    /**
     * 接收前端请求，处理向指定方向移动的操作。
     *
     * @param request 包含玩家名称和移动方向的请求数据。
     * @return 表示移动结果的 Result 对象。
     */
    @PostMapping("/go")
    public Result go(@RequestBody Map<String, Object> request) {
        System.out.println(request);
        String playerName = (String) request.get("playerName");
        int direction = (int) request.get("direction");

        return move(playerName, direction);
    }

    /**
     * 接收前端请求，处理向相反方向移动的操作。
     *
     * @param request 包含玩家名称和移动方向的请求数据。
     * @return 表示移动结果的 Result 对象。
     */
    @PostMapping("/back")
    public Result back(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        int direction = (int) request.get("direction");

        return move(playerName, direction);
    }

    /**
     * 获取指定房间内的所有物品信息。
     *
     * @param request 包含房间ID的请求数据。
     * @return 包含物品信息的 Result 对象。
     */
    @PostMapping("/check")
    public Result getItemsInRoom(@RequestBody Map<String, Integer> request) {
        Integer roomID = request.get("roomID");
        logger.info("检查房间内物品，房间ID：" + roomID);
        Room curRoom = roomService.findRoomByRoomId(roomID);
        if (curRoom == null) {
            logger.warning("不存在ID为" + roomID + "的房间。");
            return Result.fail("不存在该房间");
        }

        List<Item> items = roomService.checkItemsInRoom(roomID);
        logger.info("房间内的物品：" + items);
        return Result.success(items);
    }

}