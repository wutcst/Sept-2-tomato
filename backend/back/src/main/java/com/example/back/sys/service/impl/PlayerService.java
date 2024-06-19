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
        return playerMapper.judgeExist(player).size()!=0;
    }
}
