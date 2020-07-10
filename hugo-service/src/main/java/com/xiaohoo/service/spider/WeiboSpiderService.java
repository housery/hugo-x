package com.xiaohoo.service.spider;

import com.xiaohoo.entity.WeiboHotSearch;

import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/7/9
 * @email :1126457667@qq.com
 */
public interface WeiboSpiderService {

    List<WeiboHotSearch> syncHotSearch();
}
