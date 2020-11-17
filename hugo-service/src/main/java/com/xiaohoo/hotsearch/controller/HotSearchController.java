package com.xiaohoo.hotsearch.controller;

import com.xiaohoo.annotation.AnonymousAccess;
import com.xiaohoo.hotsearch.dao.HotSearchRepository;
import com.xiaohoo.hotsearch.entity.HotSearch;
import com.xiaohoo.hotsearch.service.HotSearchService;
import com.xiaohoo.utils.PageUtil;
import com.xiaohoo.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author ：yangxiaohu
 * @date ：2020/8/14
 * @email :1126457667@qq.com
 */
@RestController
@RequestMapping("/api/hot")
@Api(tags = "HUGO：热搜")
public class HotSearchController {

    private static final SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private HotSearchService hotSearchService;

    @Autowired
    private HotSearchRepository repository;

    @GetMapping("/findTodayHot")
    @AnonymousAccess
    public ResponseEntity findTodayHot(String date) {
        return ResponseEntity.ok(hotSearchService.findTodayHot(date));
    }

    @GetMapping("/findSource")
    @ApiOperation("查询分类")
    @AnonymousAccess
    public ResponseEntity findSource() {
        List<String> sources = repository.findAllSource();
        return ResponseEntity.ok(sources);
    }

    @ApiOperation("查询热搜数据")
    @GetMapping(value = "/findPage")
    public ResponseEntity<Object> queryTables(@RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "0")Integer page,
                                              @RequestParam(defaultValue = "10")Integer size){
        HotSearch ex = new HotSearch();
        if (StringUtils.isNotEmpty(name)) {
            ex.setSource(name);
        }

        Sort.Order or1 = new Sort.Order(Sort.Direction.DESC, "createTime");
        Sort.Order or2 = new Sort.Order(Sort.Direction.ASC, "source");
        Sort.Order or3 = new Sort.Order(Sort.Direction.ASC, "orders");

        Pageable pg = PageRequest.of(page, size, Sort.by(or1, or2, or3));
        Page<HotSearch> all = repository.findAll(Example.of(ex), pg);
        return new ResponseEntity<>(PageUtil.toPage(all), HttpStatus.OK);
    }
}
