package com.example.back.sys.service.impl;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ItemService {

    @Resource
    private ItemMapper itemMapper;
    public Item getItemById(Integer itemID) {
        return itemMapper.getItemById(itemID);
    }

    public boolean takeItem(Player player, Item item) {
        try {
            itemMapper.renewMap(player.getCurrentRoomID(),item.getItemID());
            itemMapper.renewPlayer(player,item);
            itemMapper.renewPlayerItem(player,item);
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
