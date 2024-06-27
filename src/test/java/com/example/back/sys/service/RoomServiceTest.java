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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoomServiceTest {

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

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

    // 添加更多测试方法，覆盖其他RoomService的功能

}
