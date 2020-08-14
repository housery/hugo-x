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
@Api(tags = "HUGO：爬虫")
@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @GetMapping("/syncAll")
    @AnonymousAccess
    public ResponseEntity syncAll() {
        spiderService.syncHotSearch4Weibo();
        spiderService.syncHotSearch4Baidu();
        spiderService.syncHotSearch4Wechat();
        spiderService.syncHotSearch4Zhihu();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/weiboSync")
    @AnonymousAccess
    public ResponseEntity weiboSync() {
        return ResponseEntity.ok(spiderService.syncHotSearch4Weibo());
    }

    @GetMapping("/baiduSync")
    @AnonymousAccess
    public ResponseEntity baiduSync() {
        return ResponseEntity.ok(spiderService.syncHotSearch4Baidu());
    }

    @GetMapping("/zhihuSync")
    @AnonymousAccess
    public ResponseEntity zhihuSync() {
        return ResponseEntity.ok(spiderService.syncHotSearch4Zhihu());
    }

    @GetMapping("/weixinSync")
    @AnonymousAccess
    public ResponseEntity weixinSync() {
        return ResponseEntity.ok(spiderService.syncHotSearch4Wechat());
    }
}
