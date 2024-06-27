package com.example.back.sys.service.impl;

import com.example.back.sys.entity.Player;
import com.example.back.sys.mapper.PlayerMapper;
import com.example.back.sys.service.IPlayerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PlayerService implements IPlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Override
    public List<Player> gatAll() {
        return playerMapper.getAllPlayers();
    }

    @Override
    public Player findByPlayerName(String playerName) {
        return playerMapper.getPlayerByName(playerName);
    }

    @Override
    public boolean login(Player player) {
        return playerMapper.judgeExist(player).size() != 0;
    }

    @Override
    public boolean register(Player player) {
        return playerMapper.registerPlayer(player);
    }

    @Override
    public boolean renewPlayer(Player player) {
        return playerMapper.renewPlayer(player);
    }

    @Override
    public void setLogin(Integer playerID) {
        playerMapper.setLogin(playerID);
    }

    @Override
    public List<Integer> checkPlayerOnline() {
        return playerMapper.checkPlayerOnline();
    }

    @Override
    public void setOffLine(Long uId) {
        playerMapper.setOffLine(uId.intValue());
    }
}
