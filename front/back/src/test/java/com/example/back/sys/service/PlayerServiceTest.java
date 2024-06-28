package com.example.back.sys.service;

import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.PlayerMapper;
import com.example.back.sys.service.impl.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * PlayerService 测试类
 */
public class PlayerServiceTest {

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;

    /**
     * 每个测试方法执行前的初始化操作
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试获取所有玩家信息的方法
     */
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

    /**
     * 测试根据玩家姓名获取玩家信息的方法
     */
    @Test
    public void testFindByPlayerName() {
        String playerName = "Player1";
        Player player = new Player();
        player.setPlayerName(playerName);

        when(playerMapper.getPlayerByName(playerName)).thenReturn(player);

        Player result = playerService.findByPlayerName(playerName);

        assertNotNull(result);
        assertEquals(playerName, result.getPlayerName());

        verify(playerMapper, times(1)).getPlayerByName(playerName);
    }

    /**
     * 测试玩家登录成功的方法
     */
    @Test
    public void testLoginSuccess() {
        Player player = new Player();
        player.setPlayerName("Player1");
        player.setPassWord("password");
        List<Player> players = new ArrayList<>();
        players.add(player);
        when(playerMapper.judgeExist(player)).thenReturn(players);

        boolean result = playerService.login(player);

        assertTrue(result);

        verify(playerMapper, times(1)).judgeExist(player);
    }

    /**
     * 测试玩家注册的方法
     */
    @Test
    public void testRegister() {
        Player player = new Player();
        player.setPlayerName("NewPlayer");

        when(playerMapper.registerPlayer(player)).thenReturn(true);

        boolean result = playerService.register(player);

        assertTrue(result);

        verify(playerMapper, times(1)).registerPlayer(player);
    }

    /**
     * 测试重置玩家密码的方法
     */
    @Test
    public void testRenewPlayer() {
        Player player = new Player();
        player.setPlayerName("Player1");
        player.setPassWord("newPassword");

        when(playerMapper.renewPlayer(player)).thenReturn(true);

        boolean result = playerService.renewPlayer(player);

        assertTrue(result);

        verify(playerMapper, times(1)).renewPlayer(player);
    }

    /**
     * 测试获取不存在的玩家信息的方法
     */
    @Test
    public void testFindByPlayerNameNotFound() {
        String playerName = "NonExistentPlayer";

        when(playerMapper.getPlayerByName(playerName)).thenReturn(null);

        Player result = playerService.findByPlayerName(playerName);

        assertNull(result);

        verify(playerMapper, times(1)).getPlayerByName(playerName);
    }

    /**
     * 测试注册已存在的玩家的方法
     */
    @Test
    public void testRegisterExistingPlayer() {
        Player player = new Player();
        player.setPlayerName("ExistingPlayer");

        when(playerMapper.registerPlayer(player)).thenReturn(true);

        boolean result = playerService.register(player);

        assertTrue(result);

    }
}
