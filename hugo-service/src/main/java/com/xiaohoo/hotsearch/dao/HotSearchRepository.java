package com.xiaohoo.hotsearch.dao;

import com.xiaohoo.hotsearch.entity.HotSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/7/9
 * @email :1126457667@qq.com
 */
public interface HotSearchRepository extends JpaRepository<HotSearch, Long> {

    @Query(value = "select * " +
            "from hugo_hot_search s " +
            "where s.orders = 1 " +
            "  and s.source in ('微博', '百度', '知乎', '微信') " +
            "  and date_format(s.create_time, '%Y-%m-%d') = ?1 ", nativeQuery = true)
    List<HotSearch> findTodayHot(String date);

    /**
     * 查询分类
     */
    @Query(value = "select distinct s.source from hugo_hot_search s", nativeQuery = true)
    List<String> findAllSource();
}
