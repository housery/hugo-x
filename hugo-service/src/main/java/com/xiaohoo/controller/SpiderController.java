package com.xiaohoo.controller;

import com.xiaohoo.service.spider.SpiderService;
import io.swagger.annotations.Api;
import com.xiaohoo.annotation.AnonymousAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：yangxiaohu
 * @date ：2020/7/9
 * @email :1126457667@qq.com
 */
@Api(tags = "爬虫：微博热搜")
@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private SpiderService weiboSpiderService;

    @GetMapping("/weiboSync")
    @AnonymousAccess
    public ResponseEntity weiboSync() {
        return ResponseEntity.ok(weiboSpiderService.syncHotSearch4Weibo());
    }

    @GetMapping("/baiduSync")
    @AnonymousAccess
    public ResponseEntity baiduSync() {
        return ResponseEntity.ok(weiboSpiderService.syncHotSearch4Baidu());
    }
}
