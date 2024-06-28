package com.example.back.sys.service;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.ItemMapper;
import com.example.back.sys.service.impl.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ItemServiceTest 是针对 ItemService 类的单元测试类。
 */
public class ItemServiceTest {

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    /**
     * 在每个测试方法执行之前初始化 Mockito。
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试 getItemById 方法。
     * 验证根据物品ID获取物品信息的逻辑是否正确。
     */
    @Test
    public void testGetItemById() {
        Integer itemId = 1;
        Item item = new Item();
        item.setItemID(itemId);

        when(itemMapper.getItemById(itemId)).thenReturn(item);

        Item result = itemService.getItemById(itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getItemID());
        verify(itemMapper, times(1)).getItemById(itemId);
    }

    /**
     * 测试 takeItem 方法。
     * 验证玩家拿起物品的逻辑是否正确。
     */
    @Test
    public void testTakeItem() {
        Player player = new Player();
        player.setCurrentRoomID(1);
        Item item = new Item();
        item.setItemID(1);

        when(itemMapper.checkPlayerInRoom(player.getCurrentRoomID())).thenReturn(new ArrayList<>());

        boolean result = itemService.takeItem(player, item);

        assertTrue(result);
        verify(itemMapper, times(1)).renewMap_delete(player.getCurrentRoomID(), item.getItemID());
        verify(itemMapper, times(1)).renewPlayerItem_add(player, item);
        verify(itemMapper, times(1)).renewPlayer(player, item);
    }

    /**
     * 测试 checkItem 方法。
     * 验证检查玩家是否拥有物品的逻辑是否正确。
     */
    @Test
    public void testCheckItem() {
        Integer playerId = 1;
        Integer itemId = 1;

        when(itemMapper.check(playerId, itemId)).thenReturn(1);

        boolean result = itemService.checkItem(playerId, itemId);

        assertTrue(result);
        verify(itemMapper, times(1)).check(playerId, itemId);
    }

    /**
     * 测试 dropItem 方法。
     * 验证玩家丢弃物品的逻辑是否正确。
     */
    @Test
    public void testDropItem() {
        Player player = new Player();
        player.setCurrentRoomID(1);
        Item item = new Item();
        item.setItemID(1);

        when(itemMapper.checkPlayerInRoom(player.getCurrentRoomID())).thenReturn(new ArrayList<>());

        boolean result = itemService.dropItem(player, item);

        assertTrue(result);
        verify(itemMapper, times(1)).renewMap_add(player.getCurrentRoomID(), item.getItemID());
        verify(itemMapper, times(1)).renewPlayerItem_delete(player, item);
        verify(itemMapper, times(1)).renewPlayer(player, item);
    }

    /**
     * 测试 checkItemsInBag 方法。
     * 验证检查玩家背包中物品的逻辑是否正确。
     */
    @Test
    public void testCheckItemsInBag() {
        Integer playerId = 1;
        List<Object> itemIds = new ArrayList<>();
        itemIds.add(1);
        itemIds.add(2);

        when(itemMapper.checkItemsInBag(playerId)).thenReturn(itemIds);
        when(itemMapper.getItemById(1)).thenReturn(new Item());
        when(itemMapper.getItemById(2)).thenReturn(new Item());

        List<Item> result = itemService.checkItemsInBag(playerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(itemMapper, times(1)).checkItemsInBag(playerId);
    }

    /**
     * 测试 eatItem 方法。
     * 验证玩家食用物品的逻辑是否正确。
     */
    @Test
    public void testEatItem() {
        Player player = new Player();
        player.setCurrentWeight(10);
        player.setMaxCarryWeight(20);
        Item item = new Item();
        item.setItemID(1);
        item.setWeight(5);
        item.setIsMagic(1);

        boolean result = itemService.eatItem(player, item);

        assertTrue(result);
        assertEquals(5, player.getCurrentWeight());
        assertEquals(30, player.getMaxCarryWeight());
        verify(itemMapper, times(1)).renewPlayerItem_delete(player, item);
        verify(itemMapper, times(1)).renewPlayer(player, item);
    }

    /**
     * 测试 getItemEffectNum 方法。
     * 验证获取物品效果编号的逻辑是否正确。
     */
    @Test
    public void testGetItemEffectNum() {
        Integer currentRoomId = 1;
        Integer itemId = 1;

        when(itemMapper.checkEffect(currentRoomId, itemId)).thenReturn(2);

        Integer result = itemService.getItemEffectNum(currentRoomId, itemId);

        assertNotNull(result);
        assertEquals(2, result);
        verify(itemMapper, times(1)).checkEffect(currentRoomId, itemId);
    }

    /**
     * 测试 getItemEffectDescription 方法。
     * 验证获取物品效果描述的逻辑是否正确。
     */
    @Test
    public void testGetItemEffectDescription() {
        Integer currentRoomId = 1;
        Integer itemId = 1;

        when(itemMapper.checkEffectDescription(currentRoomId, itemId)).thenReturn("效果描述");

        String result = itemService.getItemEffectDescription(currentRoomId, itemId);

        assertNotNull(result);
        assertEquals("效果描述", result);
        verify(itemMapper, times(1)).checkEffectDescription(currentRoomId, itemId);
    }

    /**
     * 测试 feedItem 方法。
     * 验证玩家使用物品的逻辑是否正确。
     */
    @Test
    public void testFeedItem() {
        Player player = new Player();
        Item item = new Item();
        item.setItemID(1);

        itemService.feedItem(player, item);

        verify(itemMapper, times(1)).renewPlayerItem_delete(player, item);
    }
}
