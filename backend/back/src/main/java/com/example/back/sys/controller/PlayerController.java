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
        if(iPlayerService.login(player)){
            return Result.success();
        }
        return Result.fail("用户名或密码输入错误");
    }

    @PostMapping("/register")
    public Result register(@RequestBody Player player){
        if(iPlayerService.findByPlayerName(player.getPlayerName())!=null) return Result.fail("该用户已存在");
        if(iPlayerService.register(player)) return Result.success("注册成功");
        return Result.fail("服务器异常，注册失败");
    }
}
