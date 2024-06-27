package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 房间物品关联实体类。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("roomitems")
public class RoomItems {
    private Integer itemID;
    private Integer roomID;
}
