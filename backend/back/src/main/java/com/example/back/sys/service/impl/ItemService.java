package com.example.back.sys.service.impl;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Resource
    private ItemMapper itemMapper;
    public Item getItemById(Integer itemID) {
        return itemMapper.getItemById(itemID);
    }

    public boolean takeItem(Player player, Item item) {
        try {
            itemMapper.renewMap_delete(player.getCurrentRoomID(),item.getItemID());
            itemMapper.renewPlayerItem_add(player,item);
            itemMapper.renewPlayer(player,item);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean checkItem(Integer playerID, Integer itemID) {
        return itemMapper.check(playerID,itemID)!=0;
    }

    public boolean dropItem(Player player, Item item) {
        try {
            itemMapper.renewMap_add(player.getCurrentRoomID(),item.getItemID());
            itemMapper.renewPlayerItem_delete(player,item);
            itemMapper.renewPlayer(player,item);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public List<Item> checkItemsInBag(Integer playerID) {
        List<Object> l = itemMapper.checkItemsInBag(playerID);
        List<Item> res = new ArrayList<>();
        for(Object i :l ){
            res.add(itemMapper.getItemById((Integer) i));
        }
        return res;
    }

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
}
