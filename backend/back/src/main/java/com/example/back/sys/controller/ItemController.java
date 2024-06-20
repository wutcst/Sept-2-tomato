package com.example.back.sys.controller;

import com.example.back.sys.Result;
import com.example.back.sys.entity.Item;
import com.example.back.sys.service.impl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/getItemInfo")
    public Result getItemById(@RequestBody Integer itemID){
        Item item = itemService.getItemById(itemID);
        if(item==null) return Result.fail("未找到该物品！");
        return Result.success(item);
    }

}
