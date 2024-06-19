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
}
