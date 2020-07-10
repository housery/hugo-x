package com.xiaohoo.service.spider.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.xiaohoo.dao.WeiboHotSearchRepository;
import com.xiaohoo.entity.WeiboHotSearch;
import com.xiaohoo.service.spider.WeiboSpiderService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/7/9
 * @email :1126457667@qq.com
 */
@Service
@Slf4j
public class WeiboSpiderServiceImpl implements WeiboSpiderService {

    @Autowired
    private WeiboHotSearchRepository weiboHotSearchRepository;

    private static final String WEB_URL = "https://s.weibo.com/top/summary";
    private static final String ROOT_URL = "https://s.weibo.com";

    @Override
    public List<WeiboHotSearch> syncHotSearch() {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36";
        String body = HttpRequest.get(WEB_URL)
                .header(Header.USER_AGENT, userAgent)   // 设置代理，防止请求不到
                .timeout(20000)
                .execute()
                .body();
        Document doc = Jsoup.parse(body);
        // 获取到热点列表
        Elements rows = doc.select("#pl_top_realtimehot tbody tr");
        List<WeiboHotSearch> weiboHotSearches = parseList(rows);

        // 数据持久化
        List<WeiboHotSearch> result = weiboHotSearchRepository.saveAll(weiboHotSearches);
        return result;
    }

    private List<WeiboHotSearch> parseList(Elements rows) {
        List<WeiboHotSearch> result = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            WeiboHotSearch hotSearch = new WeiboHotSearch();
            Element row = rows.get(i);
            String orderStr = row.select(".td-01").get(0).html();
            Elements tagEle = row.select(".td-03 i");
            String tag = "";
            if (tagEle.size() > 0) {
                tag = tagEle.get(0).html();
            }
            String keyword = row.select(".td-02").get(0).select("a").get(0).html();
            String peopleCount = row.select(".td-02").get(0).select("span").get(0).html();
            String url = row.select(".td-02").get(0).select("a").first().attr("href");

            hotSearch.setOrders(Integer.valueOf(orderStr));
            hotSearch.setTag(tag);
            hotSearch.setKeyword(keyword);
            hotSearch.setPeopleCount(Long.valueOf(peopleCount));
            hotSearch.setUrl(ROOT_URL + url);

            result.add(hotSearch);
        }
        return result;
    }
}
