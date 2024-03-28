package com.ruoyi.web.wp.service;

import com.ruoyi.web.wp.dto.ParseCopyLink;
import com.ruoyi.web.wp.dto.ParseLink;

public interface WpService {
    Object parseList(ParseCopyLink parse);

    Object parseLink(ParseLink parse);
}
