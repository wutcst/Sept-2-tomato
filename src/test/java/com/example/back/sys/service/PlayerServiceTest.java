package com.example.back.sys.service;

import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.PlayerMapper;
import com.example.back.sys.service.impl.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPlayers() {
        Player player1 = new Player();
        player1.setPlayerName("Player1");

        Player player2 = new Player();
        player2.setPlayerName("Player2");

        when(playerMapper.getAllPlayers()).thenReturn(Arrays.asList(player1, player2));

        List<Player> players = playerService.gatAll();

        assertEquals(2, players.size());
        assertEquals("Player1", players.get(0).getPlayerName());
        assertEquals("Player2", players.get(1).getPlayerName());

        verify(playerMapper, times(1)).getAllPlayers();
    }

    // 添加更多测试方法，覆盖其他PlayerService的功能

}
