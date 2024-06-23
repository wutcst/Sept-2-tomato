package com.example.back.sys.service.impl;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Message;
import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.ItemMapper;
import com.example.back.sys.service.IItemService;
import com.example.back.sys.websocket.WebSocketServerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService implements IItemService {

    @Resource
    private ItemMapper itemMapper;
    public Item getItemById(Integer itemID) {
        return itemMapper.getItemById(itemID);
    }

    @Override
    public boolean takeItem(Player player, Item item) {
        try {
            itemMapper.renewMap_delete(player.getCurrentRoomID(),item.getItemID());
            itemMapper.renewPlayerItem_add(player,item);
            itemMapper.renewPlayer(player,item);
            //通知所有在该房间的玩家要更新信息
            List<Integer> list_to_renew = itemMapper.checkPlayerInRoom(player.getCurrentRoomID());
            for(Integer i:list_to_renew) {
                Message message = new Message("message",player.getPlayerName()+"捡起了"+item.getItemName());
                WebSocketServerUtil.sendInfo(String.valueOf(message), Long.valueOf(i));
            }
        }catch(Exception e){
            return false;
        }
        return true;
    }
    @Override
    public boolean checkItem(Integer playerID, Integer itemID) {
        return itemMapper.check(playerID,itemID)!=0;
    }
    @Override
    public boolean dropItem(Player player, Item item) {
        try {
            itemMapper.renewMap_add(player.getCurrentRoomID(),item.getItemID());
            itemMapper.renewPlayerItem_delete(player,item);
            itemMapper.renewPlayer(player,item);
            //通知所有在该房间的玩家要更新信息
            List<Integer> list_to_renew = itemMapper.checkPlayerInRoom(player.getCurrentRoomID());
            for(Integer i:list_to_renew) {
                Message message = new Message("message",player.getPlayerName()+"丢下了"+item.getItemName());
                WebSocketServerUtil.sendInfo(String.valueOf(message),Long.valueOf(i));
            }
        }catch(Exception e){
            return false;
        }
        return true;
    }
    @Override
    public List<Item> checkItemsInBag(Integer playerID) {
        List<Object> l = itemMapper.checkItemsInBag(playerID);
        List<Item> res = new ArrayList<>();
        for(Object i :l ){
            res.add(itemMapper.getItemById((Integer) i));
        }
        return res;
    }
    @Override
    public boolean eatItem(Player player, Item item) {
        try {
            player.setCurrentWeight(player.getCurrentWeight() - item.getWeight());
            //魔法饼干
            switch (item.getIsMagic()){
                case 1:player.setMaxCarryWeight(player.getMaxCarryWeight()+10);break;
                case 2:return false;
            }
            System.out.println(item.getIsMagic());
            itemMapper.renewPlayerItem_delete(player,item);
            itemMapper.renewPlayer(player,item);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Integer getItemEffectNum(Integer currentRoomID, Integer itemID) {
        return itemMapper.checkEffect(currentRoomID,itemID);
    }

    @Override
    public String getItemEffectDescription(Integer currentRoomID, Integer itemID) {
        return itemMapper.checkEffectDescription(currentRoomID,itemID);
    }

    @Override
    public void feedItem(Player player, Item item) {
        itemMapper.renewPlayerItem_delete(player,item);
    }


}
