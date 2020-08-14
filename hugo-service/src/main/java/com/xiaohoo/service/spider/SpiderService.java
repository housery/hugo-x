package com.xiaohoo.service.spider;

import com.xiaohoo.entity.HotSearch;

import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/7/9
 * @email :1126457667@qq.com
 */
public interface SpiderService {

    /**
     * 同步微博热搜
     * @return 热搜列表
     */
    List<HotSearch> syncHotSearch4Weibo();

    /**
     * 同步微博热搜
     * @return 热搜列表
     */
    List<HotSearch> syncHotSearch4Baidu();

    /**
     * 知乎热榜
     */
    List<HotSearch> syncHotSearch4Zhihu();

    /**
     * 微信热文
     */
    List<HotSearch> syncHotSearch4Wechat();
}
