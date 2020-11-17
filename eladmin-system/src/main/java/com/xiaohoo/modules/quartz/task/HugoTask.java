package com.xiaohoo.modules.quartz.task;

import com.xiaohoo.hotsearch.controller.SpiderController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：yangxiaohu
 * @date ：2020/10/28
 * @email :1126457667@qq.com
 */
@Slf4j
@Component
public class HugoTask {
    @Autowired
    private SpiderController spiderController;

    public void findAllHotSearch() {
        log.info("开始爬取全网热搜");
        spiderController.syncAll();
    }
}
