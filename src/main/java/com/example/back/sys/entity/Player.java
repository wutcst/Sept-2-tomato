package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * 玩家实体类。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("player")
public class Player {
    private Integer currentRoomID;
    private Integer currentWeight;
    private Integer maxCarryWeight;
    private Integer playerID;
    private String playerName;
    private String passWord;

    public void enterRandomRoom() {
        Random rand = new Random();
        this.setCurrentRoomID(rand.nextInt(6));
    }

}
