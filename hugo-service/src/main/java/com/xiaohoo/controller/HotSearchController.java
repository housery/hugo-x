package com.xiaohoo.controller;

import com.xiaohoo.annotation.AnonymousAccess;
import com.xiaohoo.entity.HotSearch;
import com.xiaohoo.service.hotSearch.HotSearchService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/8/14
 * @email :1126457667@qq.com
 */
@RestController
@RequestMapping("/hugo")
@Api(tags = "HUGO：热搜")
public class HotSearchController {

    private static final SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private HotSearchService hotSearchService;

    @GetMapping("/findTodayHot")
    @AnonymousAccess
    public ResponseEntity findTodayHot(String date) {
        return ResponseEntity.ok(hotSearchService.findTodayHot(date));
    }
}
