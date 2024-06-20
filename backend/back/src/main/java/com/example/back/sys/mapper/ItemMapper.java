package com.example.back.sys.mapper;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.PlayerItems;

public interface ItemMapper {

    Item getItemById(Integer itemID);

    void renewMap_delete(Integer currentRoomID, Integer itemID);

    void renewPlayer(Player player, Item item);

    void renewPlayerItem_add(Player player, Item item);


    void renewMap_add(Integer currentRoomID, Integer itemID);

    void renewPlayerItem_delete(Player player, Item item);

    Object check(Integer playerID, Integer itemID);
}
