package com.xiaohoo.hotsearch.service.impl;

import com.xiaohoo.hotsearch.dao.HotSearchRepository;
import com.xiaohoo.hotsearch.entity.HotSearch;
import com.xiaohoo.hotsearch.service.HotSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/8/14
 * @email :1126457667@qq.com
 */
@Service
@Slf4j
public class HotSearchServiceImpl implements HotSearchService {

    @Autowired
    private HotSearchRepository hotSearchRepository;

    @Override
    public List<HotSearch> findTodayHot(String date) {
        return hotSearchRepository.findTodayHot(date);
    }
}
