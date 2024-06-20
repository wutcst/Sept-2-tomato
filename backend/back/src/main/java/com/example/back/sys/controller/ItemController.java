package com.example.back.sys.controller;

import com.example.back.sys.Result;
import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.service.impl.ItemService;
import com.example.back.sys.service.impl.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/getItemInfo")
    public Result getItemInfoById(@RequestBody Integer itemID){
        Item item = itemService.getItemById(itemID);
        if(item==null) return Result.fail("未找到该物品！");
        return Result.success(item);
    }

    @PostMapping("/take")
    public Result takeItemById(Integer itemID,String playerName)
    {
        Player player = playerService.findByPlayerName(playerName);
        Item item = itemService.getItemById(itemID);

        //id传错了的情况
        if(player==null) return Result.fail("未找到该用户！");
        if(item==null) return Result.fail("未找到该物品！");

        //拿不下
        if(player.getCurrentWeight()+item.getWeight()>player.getMaxCarryWeight())
            return Result.fail("你拿不下更多东西了");
        player.setCurrentWeight(item.getWeight()+player.getCurrentWeight());
        if(!itemService.takeItem(player,item)) return Result.fail("服务器原因，拿取失败");
        return Result.success(player);
    }

    @PostMapping("/drop")
    public Result dropItemById(Integer itemID,String playerName)
    {
//        Player player = playerService.findByPlayerName(playerName);
//        Item item = itemService.getItemById(itemID);
//
//        //id传错了的情况
//        if(player==null) return Result.fail("未找到该用户！");
//        if(item==null) return Result.fail("未找到该物品！");
//
//        //玩家没有这个物品
//        if(!itemService.checkItem(player.getPlayerID(),itemID)) return Result.fail("玩家受伤没有该物品");
        return Result.success();
    }

}
