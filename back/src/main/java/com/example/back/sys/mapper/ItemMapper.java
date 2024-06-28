package com.example.back.sys.mapper;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.PlayerItems;

import java.util.List;

/**
 * Item 数据访问层接口，定义了与物品相关的数据库操作方法。
 */
public interface ItemMapper {

    Item getItemById(Integer itemID);

    void renewMap_delete(Integer currentRoomID, Integer itemID);

    void renewPlayer(Player player, Item item);

    void renewPlayerItem_add(Player player, Item item);


    void renewMap_add(Integer currentRoomID, Integer itemID);

    void renewPlayerItem_delete(Player player, Item item);

    Integer check(Integer playerID, Integer itemID);

    List<Object> checkItemsInBag(Integer playerID);

    Integer checkEffect(Integer currentRoomID, Integer itemID);

    String checkEffectDescription(Integer currentRoomID, Integer itemID);

    List<Integer> checkPlayerInRoom(Integer currentRoomID);
}
