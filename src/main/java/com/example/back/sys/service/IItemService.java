package com.example.back.sys.service;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;

import java.util.List;

public interface IItemService {
    boolean takeItem(Player player, Item item);

    boolean checkItem(Integer playerID, Integer itemID);

    boolean dropItem(Player player, Item item);

    List<Item> checkItemsInBag(Integer playerID);

    boolean eatItem(Player player, Item item);

    Integer getItemEffectNum(Integer currentRoomID, Integer itemID);

    String getItemEffectDescription(Integer currentRoomID, Integer itemID);

    void feedItem(Player player, Item item);
}
