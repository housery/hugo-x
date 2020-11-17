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
package com.xiaohoo.baidurec.service;

import com.xiaohoo.baidurec.domain.BaiduSearchRec;
import com.xiaohoo.baidurec.service.dto.BaiduSearchRecDto;
import com.xiaohoo.baidurec.service.dto.BaiduSearchRecQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description 服务接口
* @author yangxiaohu
* @date 2020-11-17
**/
public interface BaiduSearchRecService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BaiduSearchRecQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BaiduSearchRecDto>
    */
    List<BaiduSearchRecDto> queryAll(BaiduSearchRecQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return BaiduSearchRecDto
     */
    BaiduSearchRecDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return BaiduSearchRecDto
    */
    BaiduSearchRecDto create(BaiduSearchRec resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(BaiduSearchRec resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<BaiduSearchRecDto> all, HttpServletResponse response) throws IOException;

    /**
     * 同步搜索信息
     * @param keyword 城市名称
     * @return 搜索推荐结果
     */
    void syncSearchRec(String keyword, List<BaiduSearchRec> res, int level);
}
