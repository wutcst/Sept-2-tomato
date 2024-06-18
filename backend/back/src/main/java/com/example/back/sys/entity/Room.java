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
    private long roomID;
    private String roomName;

    private Long eastRoomID;
    private Long northRoomID;
    private Long southRoomID;
    private Long westRoomID;
}
