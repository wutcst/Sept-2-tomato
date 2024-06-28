package com.example.back.sys.service;

import com.example.back.sys.entity.Item;
import com.example.back.sys.entity.Player;
import com.example.back.sys.entity.Room;
import com.example.back.sys.mapper.ItemMapper;
import com.example.back.sys.mapper.RoomMapper;
import com.example.back.sys.service.impl.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * RoomServiceTest 是针对 RoomService 类的单元测试类。
 */
public class RoomServiceTest {

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RoomService roomService;

    /**
     * 在每个测试方法执行之前初始化 Mockito。
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试 findRoomByRoomId 方法。
     * 验证根据房间ID获取房间信息的逻辑是否正确。
     */
    @Test
    public void testFindRoomByRoomId() {
        Room room = new Room();
        room.setRoomID(1);
        room.setRoomName("Test Room");

        when(roomMapper.findRoomByRoomId(1)).thenReturn(room);

        Room foundRoom = roomService.findRoomByRoomId(1);

        assertEquals(1, foundRoom.getRoomID());
        assertEquals("Test Room", foundRoom.getRoomName());

        verify(roomMapper, times(1)).findRoomByRoomId(1);
    }

    /**
     * 测试 updatePlayer 方法。
     * 验证更新玩家信息的逻辑是否正确。
     */
    @Test
    public void testUpdatePlayer() {
        Player player = new Player();
        player.setPlayerID(1);
        player.setPlayerName("Test Player");
        player.setCurrentRoomID(1);

        doNothing().when(roomMapper).renewPlayer(player);

        roomService.updatePlayer(player);

        verify(roomMapper, times(1)).renewPlayer(player);
    }

    /**
     * 测试 checkItemsInRoom 方法。
     * 验证检查房间内物品的逻辑是否正确。
     */
    @Test
    public void testCheckItemsInRoom() {
        Integer roomID = 1;
        List<Object> itemIDs = new ArrayList<>();
        itemIDs.add(1);
        itemIDs.add(2);

        Item item1 = new Item();
        item1.setItemID(1);
        Item item2 = new Item();
        item2.setItemID(2);

        when(roomMapper.checkItemsInRoom(roomID)).thenReturn(itemIDs);
        when(itemMapper.getItemById(1)).thenReturn(item1);
        when(itemMapper.getItemById(2)).thenReturn(item2);

        List<Item> items = roomService.checkItemsInRoom(roomID);

        assertEquals(2, items.size());
        assertEquals(1, items.get(0).getItemID());
        assertEquals(2, items.get(1).getItemID());

        verify(roomMapper, times(1)).checkItemsInRoom(roomID);
        verify(itemMapper, times(1)).getItemById(1);
        verify(itemMapper, times(1)).getItemById(2);
    }

    // 添加更多测试方法，覆盖其他 RoomService 的功能
}
