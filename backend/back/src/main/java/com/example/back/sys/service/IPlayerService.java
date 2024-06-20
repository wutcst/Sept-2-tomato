package com.example.back.sys.service;

import com.example.back.sys.entity.Player;

import java.util.List;

public interface IPlayerService {
    List<Player> gatAll();

    Player findByPlayerName(String playerName);

    boolean login(Player player);

    boolean register(Player player);

    boolean renewPlayer(Player player);
}
