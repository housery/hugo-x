package com.xiaohoo.controller;

import io.swagger.annotations.Api;
import com.xiaohoo.annotation.AnonymousAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author ：yangxiaohu
 * @date ：2020/6/12
 * @email :1126457667@qq.com
 */
@RestController
@RequestMapping("/hugo")
@Api(tags = "测试：本地化测试")
public class HugoController {

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/hi")
    @AnonymousAccess
    public ResponseEntity test() {
        return ResponseEntity.ok("hello");
    }
}
