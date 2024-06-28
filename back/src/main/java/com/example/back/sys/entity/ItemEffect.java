package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.back.sys.token.IntercepterConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 物品效果实体类。
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("itemEffect")
public class ItemEffect {
    private Integer roomID;
    private Integer itemID;
    private String effect;
    private Integer effectNum;
}
