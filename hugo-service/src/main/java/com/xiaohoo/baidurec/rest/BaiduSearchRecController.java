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
package com.xiaohoo.baidurec.rest;

import com.xiaohoo.annotation.Log;
import com.xiaohoo.baidurec.domain.BaiduSearchRec;
import com.xiaohoo.baidurec.service.BaiduSearchRecService;
import com.xiaohoo.baidurec.service.dto.BaiduSearchRecQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @author yangxiaohu
* @date 2020-11-17
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "BaiduSearchRec管理")
@RequestMapping("/api/BaiduSearchRec")
public class BaiduSearchRecController {

    private final BaiduSearchRecService BaiduSearchRecService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('BaiduSearchRec:list')")
    public void download(HttpServletResponse response, BaiduSearchRecQueryCriteria criteria) throws IOException {
        BaiduSearchRecService.download(BaiduSearchRecService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询BaiduSearchRec")
    @ApiOperation("查询BaiduSearchRec")
    @PreAuthorize("@el.check('BaiduSearchRec:list')")
    public ResponseEntity<Object> query(BaiduSearchRecQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(BaiduSearchRecService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增BaiduSearchRec")
    @ApiOperation("新增BaiduSearchRec")
    @PreAuthorize("@el.check('BaiduSearchRec:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody BaiduSearchRec resources){
        return new ResponseEntity<>(BaiduSearchRecService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改BaiduSearchRec")
    @ApiOperation("修改BaiduSearchRec")
    @PreAuthorize("@el.check('BaiduSearchRec:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody BaiduSearchRec resources){
        BaiduSearchRecService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除BaiduSearchRec")
    @ApiOperation("删除BaiduSearchRec")
    @PreAuthorize("@el.check('BaiduSearchRec:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        BaiduSearchRecService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
