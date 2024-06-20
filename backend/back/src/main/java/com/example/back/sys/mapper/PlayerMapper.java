package com.example.back.sys.mapper;

import com.example.back.sys.entity.Player;

import java.util.List;

public interface PlayerMapper {

    List<Player> getAllPlayers();

    Player getPlayerByName(String playerName);

    List<Player> judgeExist(Player player);

    boolean registerPlayer(Player player);

    boolean renewPlayer(Player player);
}
