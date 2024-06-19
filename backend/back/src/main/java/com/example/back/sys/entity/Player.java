package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("player")
public class Player {
    private Long currentRoomID;
    private Long currentWeight;
    private long maxCarryWeight;
    private long playerID;
    private String playerName;
    private String passWord;
}
