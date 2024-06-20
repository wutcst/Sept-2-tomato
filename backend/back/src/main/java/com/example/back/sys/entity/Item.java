package com.example.back.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("item")
public class Item {
    private String description;
    private Integer isMagic;
    private Integer itemID;
    private Integer itemName;
    private Integer weight;
}
