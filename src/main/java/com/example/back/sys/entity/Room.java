package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("room")
public class Room {
    private String description;
    private Integer roomID;
    private String roomName;

    private Integer eastRoomID;
    private Integer northRoomID;
    private Integer southRoomID;
    private Integer westRoomID;
}
