package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 物品实体类。
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("item")
public class Item {
    private String description;
    private Integer isMagic;
    private Integer itemID;
    private String itemName;
    private Integer weight;
}
