package com.ruoyi.web.wp.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.wp.dto.ParseCopyLink;
import com.ruoyi.web.wp.dto.ParseLink;
import com.ruoyi.web.wp.service.WpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("wp")
public class WpController extends BaseController {

    @Resource
    private WpService wpService;
    @PostMapping("/parseCopyLink")
    public R parseCopyLink(@RequestBody ParseCopyLink parse) {
       Object result =  wpService.parseList(parse);
        return R.ok(result);
    }

    @PostMapping("/parseLink")
    public R parseLink(@RequestBody ParseLink parse) {
        Object result =  wpService.parseLink(parse);
        return R.ok(result);
    }


}
