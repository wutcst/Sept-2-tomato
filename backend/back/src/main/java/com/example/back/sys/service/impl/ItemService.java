package com.example.back.sys.service.impl;

import com.example.back.sys.entity.Item;
import com.example.back.sys.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ItemService {

    @Resource
    private ItemMapper itemMapper;
    public Item getItemById(Integer itemID) {
        return itemMapper.getItemById(itemID);
    }
}
