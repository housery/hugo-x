package com.xiaohoo.hotsearch.service;

import com.xiaohoo.hotsearch.entity.HotSearch;

import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/8/14
 * @email :1126457667@qq.com
 */
public interface HotSearchService {

    List<HotSearch> findTodayHot(String date);
}
