package com.example.back.sys.controller;

import com.example.back.sys.Result;
import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.service.IPlayerService;
import com.example.back.sys.service.IRoomService;
import com.example.back.sys.service.impl.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IRoomService roomService;

    /**
     * 根据物品ID获取物品信息。
     *
     * @param itemID 物品ID。
     * @return 包含物品信息的 Result 对象。
     */
    @PostMapping("/getItemInfo")
    public Result getItemInfoById(@RequestBody Integer itemID) {
        Item item = itemService.getItemById(itemID);
        log.info("要查询的item是" + item);
        // id传错了的情况
        if (item == null) return Result.fail("未找到该物品！");
        return Result.success(item);
    }

    /**
     * 玩家根据物品ID拿起物品。
     *
     * @param request 包含itemID和playerName的请求体Map。
     * @return 包含玩家信息的 Result 对象。
     */
    @PostMapping("/take")
    public Result takeItemById(@RequestBody Map<String, Object> request) {
        Integer itemID = (Integer) request.get("itemID");
        String playerName = (String) request.get("playerName");

        Player player = playerService.findByPlayerName(playerName);
        Item item = itemService.getItemById(itemID);
        // id传错了的情况
        if (player == null) return Result.fail("未找到该用户！");
        if (item == null) return Result.fail("未找到该物品！");

        log.info("尝试让" + playerName + "拿起" + item.getItemName());

        // 拿不下
        if (player.getCurrentWeight() + item.getWeight() > player.getMaxCarryWeight())
            return Result.fail("你拿不下更多东西了");

        player.setCurrentWeight(item.getWeight() + player.getCurrentWeight());
        if (!itemService.takeItem(player, item)) return Result.fail("服务器原因，拿取失败");
        return Result.success(player);
    }

    /**
     * 玩家根据物品ID丢弃物品。
     *
     * @param request 包含itemID和playerName的请求体Map。
     * @return 包含玩家信息的 Result 对象。
     */
    @PostMapping("/drop")
    public Result dropItemById(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        Integer itemID = (Integer) request.get("itemID");

        Player player = playerService.findByPlayerName(playerName);
        Item item = itemService.getItemById(itemID);

        // id传错了的情况
        if (player == null) return Result.fail("未找到该用户！");
        if (item == null) return Result.fail("未找到该物品！");

        // 玩家没有这个物品
        if (!itemService.checkItem(player.getPlayerID(), itemID)) return Result.fail("玩家没有该物品");

        player.setCurrentWeight(player.getCurrentWeight() - item.getWeight());
        if (!itemService.dropItem(player, item)) return Result.fail("服务器原因，丢弃失败");
        return Result.success(player);
    }

    /**
     * 玩家根据姓名查询身上物品。
     *
     * @param request 包含playerName的请求体Map。
     * @return 包含物品信息的 Result 对象。
     */
    @PostMapping("/getItems")
    public Result checkItemsInBag(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        Player player = playerService.findByPlayerName(playerName);

        // id传错了的情况
        if (player == null) return Result.fail("未找到该用户！");

        List<Item> res = itemService.checkItemsInBag(player.getPlayerID());
        return Result.success(res);

    }

    /**
     * 玩家使用身上物品。
     *
     * @param request 包含ItemID和playerName的请求体Map。
     * @return 包含返回的 Result 对象。
     */
    @PostMapping("/use")
    public Result feedItems(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        Integer itemID = (Integer) request.get("itemID");

        Player player = playerService.findByPlayerName(playerName);
        Item item = itemService.getItemById(itemID);

        // id传错了的情况
        if (player == null) return Result.fail("未找到该用户！");
        if (item == null) return Result.fail("未找到该物品！");

        // 玩家没有这个物品
        if (!itemService.checkItem(player.getPlayerID(), itemID)) return Result.fail("玩家没有该物品");

        if (item.getIsMagic() == 2) return Result.success("不能吃掉地图！");
        if (!itemService.eatItem(player, item)) return Result.fail("服务器原因，食用失败");
        return Result.success("食用成功，你吃掉了" + item.getItemName());
    }

    /**
     * 玩家使用身上物品。
     *
     * @param request 包含ItemID和playerName的请求体Map。
     * @return 包含返回的 Result 对象。
     */
    @PostMapping("/feed")
    public Result giveItems(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        Integer itemID = (Integer) request.get("itemID");

        Player player = playerService.findByPlayerName(playerName);
        Item item = itemService.getItemById(itemID);

        // id传错了的情况
        if (player == null) return Result.fail("未找到该用户！");
        if (item == null) return Result.fail("未找到该物品！");

        // 玩家没有这个物品
        if (!itemService.checkItem(player.getPlayerID(), itemID)) return Result.fail("玩家没有该物品");

        Integer effectNum = itemService.getItemEffectNum(player.getCurrentRoomID(), itemID);

        String res = itemService.getItemEffectDescription(player.getCurrentRoomID(), itemID);
        if (res == null) res = item.getItemName() + "在这使用没有任何效果";
        else {
            player.setCurrentWeight(player.getCurrentWeight() - item.getWeight());
            if (effectNum == 1) {
                player.enterRandomRoom();
                roomService.updatePlayer(player);
            }
            itemService.feedItem(player, item);
        }

        return Result.success(res);

    }

}