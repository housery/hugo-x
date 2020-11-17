package com.xiaohoo.hotsearch.spider.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.xiaohoo.hotsearch.dao.HotSearchRepository;
import com.xiaohoo.hotsearch.entity.HotSearch;
import com.xiaohoo.hotsearch.spider.SpiderService;
import com.xiaohoo.util.HugoConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
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
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private HotSearchRepository hotSearchRepository;

    // 微博热搜地址
    private static final String WEB_URL = "https://s.weibo.com/top/summary";

    private static final String ROOT_URL = "https://s.weibo.com";

    private static final String BAIDU_URL = "http://top.baidu.com/buzz?b=1&fr=topindex";

    private static final String ZHIHU_URL = "https://tophub.today/";

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36";

    /**知乎 tophub 节点id*/
    private static final String ZHIHU_NODE_ID = "#node-6";

    /**微信 tophub 节点id*/
    private static final String WEIXIN_NODE_ID = "#node-5";


    @Override
    public List<HotSearch> syncHotSearch4Weibo() {
        String body = HttpRequest.get(WEB_URL)
                .header(Header.USER_AGENT, USER_AGENT)   // 设置代理，防止请求不到
                .timeout(20000)
                .execute()
                .body();
        Document doc = Jsoup.parse(body);
        // 获取到热点列表
        Elements rows = doc.select("#pl_top_realtimehot tbody tr");
        List<HotSearch> weiboHotSearches = parseWeiboList(rows);

        // 数据持久化
        List<HotSearch> result = hotSearchRepository.saveAll(weiboHotSearches);
        return result;
    }

    @Override
    public List<HotSearch> syncHotSearch4Baidu() {
        String body = HttpRequest.get(BAIDU_URL)
                .header(Header.USER_AGENT, USER_AGENT)   // 设置代理，防止请求不到
                .timeout(20000)
                .execute()
                .body();
        Document doc = Jsoup.parse(body);
        Elements rows = doc.select(".list-table tbody tr");
        return hotSearchRepository.saveAll(parseBaiduHtml(rows));
    }

    @Override
    public List<HotSearch> syncHotSearch4Zhihu() {
        List<HotSearch> hotSearches = commonTophubSpider(ZHIHU_NODE_ID, HugoConstant.ZHIHU);
        return hotSearchRepository.saveAll(hotSearches);

    }

    @Override
    public List<HotSearch> syncHotSearch4Wechat() {
        List<HotSearch> hotSearches = commonTophubSpider(WEIXIN_NODE_ID, HugoConstant.WEIXIN);
        return hotSearchRepository.saveAll(hotSearches);
    }

    /**
     * 解析html数据
     */
    private List<HotSearch> parseWeiboList(Elements rows) {
        List<HotSearch> result = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            HotSearch hotSearch = new HotSearch();
            Element row = rows.get(i);
            String orderStr = row.select(".td-01").get(0).html();
            Elements tagEle = row.select(".td-03 i");
            if (tagEle.size() > 0) {
                String tag = tagEle.get(0).html();
                hotSearch.setTag(tag);
            }
            String keyword = row.select(".td-02").get(0).select("a").get(0).html();
            String peopleCount = row.select(".td-02").get(0).select("span").get(0).html();
            String url = row.select(".td-02").get(0).select("a").first().attr("href");

            hotSearch.setOrders(Integer.valueOf(orderStr));
            hotSearch.setKeyword(keyword);
            hotSearch.setPeopleCount(Long.valueOf(peopleCount));
            hotSearch.setUrl(ROOT_URL + url);
            hotSearch.setSource(HugoConstant.WEIBO);

            result.add(hotSearch);
        }
        return result;
    }

    private List<HotSearch> parseBaiduHtml(Elements rows) {
        List<HotSearch> result = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            HotSearch hotSearch = new HotSearch();
            Element rowEle = rows.get(i);

            // 跳过无效节点
            if (rowEle.className().equalsIgnoreCase("item-tr")) {
                continue;
            }

            String orders = rowEle.select(".first span").first().html();
            String keyword = rowEle.select(".keyword a").first().text();
            // baidu 搜索url
            //String href = rowEle.select(".keyword a").first().attr("href");
            String newsUrl = rowEle.select(".tc a").first().attr("href");
            String peopleCount = rowEle.select(".last span").first().text();

            hotSearch.setSource(HugoConstant.BAIDU);
            hotSearch.setOrders(Integer.valueOf(orders));
            hotSearch.setKeyword(keyword);
            hotSearch.setUrl(newsUrl);
            hotSearch.setPeopleCount(Long.valueOf(peopleCount));

            result.add(hotSearch);
        }
        return result;
    }

    /**
     * 解析今日热榜热搜表单
     */
    private List<HotSearch> parseTophubHtml(Elements rows, String source) {
        List<HotSearch> result = new ArrayList<>();
        for (Element aEle : rows) {
            String reduStr = "";
            HotSearch hotSearch = new HotSearch();
            String href = aEle.attr("href");
            String order = aEle.select(".s").first().text();
            String title = aEle.select(".t").first().text();
            if (source.equals(HugoConstant.ZHIHU)) {
                reduStr = aEle.select(".e").first().text().split(" ")[0];// 热度，单位-万热度
                // 跳过非数字类
                if (!NumberUtils.isDigits(reduStr)) {
                    continue;
                }
            } else if (source.equals(HugoConstant.WEIXIN)) {
                reduStr = aEle.select(".e").first().text().split(" ")[3]; // 微信在看
            }

            hotSearch.setUrl(href);
            hotSearch.setOrders(Integer.valueOf(order));
            hotSearch.setKeyword(title);
            hotSearch.setPeopleCount(Long.valueOf(reduStr));
            hotSearch.setSource(source);

            result.add(hotSearch);
        }

        return result;
    }

    /**
     * tophub 通用列表解析
     * @param htmlListId 榜单html页面列表ID
     * @return 解析结果
     */
    private List<HotSearch> commonTophubSpider(String htmlListId, String source) {
        String body = HttpRequest.get(ZHIHU_URL)
                .header(Header.USER_AGENT, USER_AGENT)   // 设置代理，防止请求不到
                .timeout(20000)
                .execute()
                .body();
        Document doc = Jsoup.parse(body);
        Elements listEle = doc.select(htmlListId);
        Elements rows = listEle.select(".cc-cd-cb-l>a");

        return parseTophubHtml(rows, source);
    }
}
