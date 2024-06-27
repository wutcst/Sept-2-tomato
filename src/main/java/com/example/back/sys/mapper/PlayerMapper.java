package com.example.back.sys.mapper;

import com.example.back.sys.entity.Player;

import java.util.List;

/**
 * Player 数据访问层接口，定义了与玩家相关的数据库操作方法。
 */
public interface PlayerMapper {

    List<Player> getAllPlayers();

    Player getPlayerByName(String playerName);

    List<Player> judgeExist(Player player);

    boolean registerPlayer(Player player);

    boolean renewPlayer(Player player);

    void setLogin(Integer playerID);

    List<Integer> checkPlayerOnline();

    void setOffLine(Integer uId);
}
