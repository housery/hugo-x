/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.xiaohoo.baidurec.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.xiaohoo.baidurec.domain.BaiduSearchRec;
import com.xiaohoo.util.HugoConstant;
import com.xiaohoo.utils.FileUtil;
import com.xiaohoo.utils.PageUtil;
import com.xiaohoo.utils.QueryHelp;
import com.xiaohoo.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import com.xiaohoo.baidurec.repository.BaiduSearchRecRepository;
import com.xiaohoo.baidurec.service.BaiduSearchRecService;
import com.xiaohoo.baidurec.service.dto.BaiduSearchRecDto;
import com.xiaohoo.baidurec.service.dto.BaiduSearchRecQueryCriteria;
import com.xiaohoo.baidurec.service.mapstruct.BaiduSearchRecMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://docs.auauz.net
* @description 服务实现
* @author yangxiaohu
* @date 2020-11-17
**/
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BaiduSearchRecServiceImpl implements BaiduSearchRecService {

    private static final String BAIDU_URL = "https://www.baidu.com/s?wd=";

    private final BaiduSearchRecRepository BaiduSearchRecRepository;
    private final BaiduSearchRecMapper BaiduSearchRecMapper;

    @Override
    public Map<String,Object> queryAll(BaiduSearchRecQueryCriteria criteria, Pageable pageable){
        Page<BaiduSearchRec> page = BaiduSearchRecRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(BaiduSearchRecMapper::toDto));
    }

    @Override
    public List<BaiduSearchRecDto> queryAll(BaiduSearchRecQueryCriteria criteria){
        return BaiduSearchRecMapper.toDto(BaiduSearchRecRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public BaiduSearchRecDto findById(Long id) {
        BaiduSearchRec BaiduSearchRec = BaiduSearchRecRepository.findById(id).orElseGet(BaiduSearchRec::new);
        ValidationUtil.isNull(BaiduSearchRec.getId(),"BaiduSearchRec","id",id);
        return BaiduSearchRecMapper.toDto(BaiduSearchRec);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaiduSearchRecDto create(BaiduSearchRec resources) {
        return BaiduSearchRecMapper.toDto(BaiduSearchRecRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BaiduSearchRec resources) {
        BaiduSearchRec BaiduSearchRec = BaiduSearchRecRepository.findById(resources.getId()).orElseGet(BaiduSearchRec::new);
        ValidationUtil.isNull( BaiduSearchRec.getId(),"BaiduSearchRec","id",resources.getId());
        BaiduSearchRec.copy(resources);
        BaiduSearchRecRepository.save(BaiduSearchRec);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            BaiduSearchRecRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<BaiduSearchRecDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BaiduSearchRecDto BaiduSearchRec : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("搜索关键词", BaiduSearchRec.getKeyword());
            map.put(" createBy",  BaiduSearchRec.getCreateBy());
            map.put(" createTime",  BaiduSearchRec.getCreateTime());
            map.put(" updateTime",  BaiduSearchRec.getUpdateTime());
            map.put(" updateBy",  BaiduSearchRec.getUpdateBy());
            map.put(" pid",  BaiduSearchRec.getPid());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void syncSearchRec(String keyword, List<BaiduSearchRec> res, int level) {
        if (level > 5) {
            // 递归深度最大四层
            return;
        }

        String body = HttpRequest.get(BAIDU_URL + keyword)
                .header(Header.USER_AGENT, HugoConstant.USER_AGENT)   // 设置代理
                .timeout(20000)
                .execute()
                .body();
        Document doc = Jsoup.parse(body);
        // 获取推荐列表
        Elements recommendList = doc.select(".list_1V4Yg a");

        List<BaiduSearchRec> childList = new ArrayList<>();
        recommendList.forEach(ele -> {
            BaiduSearchRec childSearchRec = new BaiduSearchRec();
            childSearchRec.setKeyword(ele.text());
            childList.add(childSearchRec);
        });

        BaiduSearchRec searchRec = new BaiduSearchRec();
        searchRec.setKeyword(keyword);
        searchRec.setChildren(childList);
        BaiduSearchRec saveRes = BaiduSearchRecRepository.save(searchRec);
        res.add(searchRec);

        saveRes.getChildren().forEach(child -> {
            syncSearchRec(child.getKeyword(), res, level+1);
        });
    }
}
