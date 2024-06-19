package com.example.back.sys.controller;

import com.example.back.sys.service.IPlayerService;
import com.example.back.sys.entity.Player;
import com.example.back.sys.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private IPlayerService iPlayerService;

    @GetMapping("/")
    public Result getAllPlayer(){
        List<Player> players = iPlayerService.gatAll();
        return Result.success(players);
    }

    @PostMapping("/getPlayerInfo")
    public Result getPlayerInfo(@RequestBody String playerName)
    {
        Player player = iPlayerService.findByPlayerName(playerName);
        if(player == null) return Result.fail("找不到该用户");
        return Result.success(player);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Player player){
        System.out.println(player.getPlayerName());
        System.out.println(player.getPassWord());
        if(iPlayerService.login(player)){
            return Result.success();
        }
        return Result.fail("用户名或密码输入错误");
    }

}
