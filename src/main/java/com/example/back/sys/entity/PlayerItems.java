package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 玩家物品关联实体类。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("playeritems")
public class PlayerItems {
    private Integer itemID;
    private Integer playerID;
}
