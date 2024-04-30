package com.ruoyi.web.wp.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.wp.dto.Code;
import com.ruoyi.web.wp.dto.Cookie;
import com.ruoyi.web.wp.dto.ParseCopyLink;
import com.ruoyi.web.wp.dto.ParseLink;
import com.ruoyi.web.wp.service.WpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("wp")
public class WpController extends BaseController {

    @Resource
    private WpService wpService;
    @Resource
    private RedisCache redisCache;
    @PostMapping("/parseCopyLink")
    public R parseCopyLink(@RequestBody ParseCopyLink parse) {
       Object result =  wpService.parseList(parse);
        return R.ok(result);
    }

    @PostMapping("/parseLink")
    public R parseLink(@RequestBody ParseLink parse) {
        Object o = wpService.parseLink(parse);
        return R.ok(o);
    }

    @PostMapping("/getSign")
    public R getSign(@RequestBody @Valid ParseCopyLink parse) {
        Object result =  wpService.getSign(parse);
        return R.ok(result);
    }

    @PostMapping("/setCode")
    public R setCode(@RequestBody Code code)  {
        String code1 = code.getCode();
        if(code1.contains("V")){
            redisCache.setCacheObject(code1,"10000");
        }else{
            redisCache.setCacheObject(code1,"5",1, TimeUnit.DAYS);
        }
        return R.ok("success");
    }

    @PostMapping("/setCookie")
    public R setCookie(@RequestBody Cookie cookie)  {
        String code = cookie.getCookie();
        redisCache.setCacheObject("cookie-1",code);
        return R.ok("success");
    }


    @PostMapping("/getCodeNum")
    public R getCodeNum(@RequestBody Code code)  {
        try {
            Object cacheObject = redisCache.getCacheObject(code.getCode());
            return R.ok(cacheObject.toString() );

        } catch (Exception e) {
            return R.ok("验证码不正确");
        }
    }



}
