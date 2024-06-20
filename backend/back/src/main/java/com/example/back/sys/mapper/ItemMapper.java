package com.example.back.sys.mapper;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;

public interface ItemMapper {

    Item getItemById(Integer itemID);

    void renewMap(Long currentRoomID, long itemID);

    void renewPlayer(Player player, Item item);

    void renewPlayerItem(Player player, Item item);
}
