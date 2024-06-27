package com.example.back.sys.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.back.sys.service.IPlayerService;
import com.example.back.sys.entity.Player;
import com.example.back.sys.Result;
import com.example.back.sys.token.TokenGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private IPlayerService iPlayerService;

    /**
     * 获取所有玩家信息。
     *
     * @return 包含所有玩家信息的 Result 对象。
     */
    @GetMapping("/")
    public Result getAllPlayer() {
        List<Player> players = iPlayerService.gatAll();
        log.info("获取所有玩家信息");
        return Result.success(players);
    }

    /**
     * 根据玩家姓名获取玩家信息。
     *
     * @param playerNameJson 包含玩家姓名的 JSON 字符串。
     * @return 包含玩家信息的 Result 对象。
     */
    @PostMapping("/getPlayerInfo")
    public Result getPlayerInfo(@RequestBody String playerNameJson) {
        JSONObject jsonObject = JSONObject.parseObject(playerNameJson);
        String playerName = jsonObject.getString("playerName");
        log.info("获取玩家信息，玩家姓名：" + playerName);
        Player player = iPlayerService.findByPlayerName(playerName);
        if (player == null) return Result.fail("找不到该用户");
        return Result.success(player);
    }

    /**
     * 玩家登录。
     *
     * @param player 包含玩家信息的 Player 对象。
     * @return 包含登录结果和 token 的 Result 对象。
     */
    @PostMapping("/login")
    public Result login(@RequestBody Player player) {
        if (iPlayerService.login(player)) {
            String token;
            token = new TokenGenerate().generateToken(player.getPlayerName());
            List<String> l = new ArrayList<>();
            l.add(token);
            l.add(player.getPlayerName());
            log.info("玩家登录成功，玩家姓名：" + player.getPlayerName());
            return Result.success(l, "登录成功");
        }
        log.info("玩家登录失败，玩家姓名：" + player.getPlayerName());
        return Result.fail("用户名或密码输入错误");
    }

    /**
     * 玩家注册。
     *
     * @param player 包含玩家信息的 Player 对象。
     * @return 包含注册结果的 Result 对象。
     */
    @PostMapping("/register")
    public Result register(@RequestBody Player player) {
        if (iPlayerService.findByPlayerName(player.getPlayerName()) != null)
            return Result.fail("该用户已存在");
        if (iPlayerService.register(player)) {
            log.info("玩家注册成功，玩家姓名：" + player.getPlayerName());
            return Result.success("注册成功");
        }
        log.info("玩家注册失败，玩家姓名：" + player.getPlayerName());
        return Result.fail("服务器异常，注册失败");
    }

    /**
     * 重置玩家密码。
     *
     * @param player 包含玩家信息的 Player 对象。
     * @return 包含重置结果的 Result 对象。
     */
    @PostMapping("/reset-password")
    public Result resetPassword(@RequestBody Player player) {
        if (iPlayerService.findByPlayerName(player.getPlayerName()) == null)
            return Result.fail("不存在该用户");
        if (iPlayerService.renewPlayer(player)) {
            log.info("玩家密码重置成功，玩家姓名：" + player.getPlayerName());
            return Result.success("修改成功");
        }
        log.info("玩家密码重置失败，玩家姓名：" + player.getPlayerName());
        return Result.fail("服务器异常，修改失败");
    }


}