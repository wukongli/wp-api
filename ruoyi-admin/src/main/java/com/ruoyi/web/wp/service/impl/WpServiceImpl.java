package com.ruoyi.web.wp.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.wp.dto.Link;
import com.ruoyi.web.wp.dto.ParseCopyLink;
import com.ruoyi.web.wp.dto.ParseLink;
import com.ruoyi.web.wp.dto.ParseResult;
import com.ruoyi.web.wp.entity.Vip;
import com.ruoyi.web.wp.service.VipService;
import com.ruoyi.web.wp.service.WpService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WpServiceImpl implements WpService {
    @Resource
    private VipService vipService;

    @Resource
    private RedisCache redisCache;

    @Override
    public Object parseList(ParseCopyLink parse) {
        Long userId = SecurityUtils.getUserId();
        Object downLoadNum = redisCache.getCacheObject(String.valueOf(userId));
        if(downLoadNum == null){
            redisCache.setCacheObject(String.valueOf(userId),0,1, TimeUnit.DAYS);
        }
        String code = parse.getCode();
        String cookie = getCommonCookie(code);
        String bduss = getBduss(cookie);
        //普通账号cookie
        String url = "https://pan.baidu.com/share/wxlist?channel=weixin&version=2.2.2&clienttype=25&web=1";
        HttpRequest request = HttpRequest.post(url)
                .header("User-Agent","netdisk")
                .header("Cookie",bduss)
                .header("Referer","https://pan.baidu.com/disk/home");
        request.form("shorturl",parse.getShorturl()).form("dir",parse.getDir()).form("root",parse.getRoot()).form("pwd",parse.getPwd()).form("num","1000").form("order","time");
        request.contentType("application/x-www-form-urlencoded");
        Object parse1 = JSON.parse(request.execute().body());
        return parse1;
    }

    @Override
    public ParseResult parseLink(ParseLink parse) {
        //大于设定次数直接返回
        String userCode =  parse.getCode();
        Long userid = SecurityUtils.getUserId();

        Object codeNum = redisCache.getCacheObject(userCode);
        Object downLoadNum = redisCache.getCacheObject(String.valueOf(userid));
        ParseResult parseResult = new ParseResult();
        parseResult.setErrno("0");
        parseResult.setCode(userCode);
        if("0".equals(codeNum.toString())){
            parseResult.setCodeUseNum("0");
            redisCache.deleteObject(userCode);
            return parseResult;
        }
        if(Integer.parseInt(downLoadNum.toString()) >= 40){
            parseResult.setCodeUseNum("40");
            return parseResult;
        }
        //使用普通cookie 获取dlink 地址。
        String cookie = getCommonCookie(userCode);
        String sign = parse.getSign();
//        String sign = "7b3848965e98b3ca0717e3a2744f95a151e6cca3";
        String timestamp = parse.getTimestamp();
//        String timestamp = "1714094010";
        String fsId = parse.getFs_id();
        String uk = parse.getUk();
        String randsk = parseSk(parse.getRandsk());
        String shareid = parse.getShareid();
        if(randsk.contains("%")){
            randsk = URLDecoder.decode(randsk);
        }
        String str = "{" +
                    "\"sekey\":\""+randsk+"\"" +
                "}";
//        String str = "{"+"sekey"+":"+randsk+"}";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.append("sekey",randsk);
        String extra = URLEncoder.encode(str);
        String url = "https://pan.baidu.com/api/sharedownload?app_id=250528&channel=chunlei&clienttype=12&sign="+sign+"&timestamp="+timestamp+"&web=1";
        HttpRequest request = HttpRequest.post(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.69")
                .header("Cookie", cookie)
                .header("Referer","https://pan.baidu.com/disk/home");
        String params  = "encrypt=0&extra="+extra+"&fid_list="+"["+fsId+"]"+"&primaryid="+shareid+"&uk="+uk+"&product=share&type=nolimit";
        request.body(params);
//        request.form("encrypt",0)
//                .form("extra",extra)
//                .form("fid_list","["+fsId+"]")
//                .form("primaryid",shareid)
//                .form("uk",uk)
//                .form("product","share")
//                .form("type","nolimit");
        JSONObject jsonObject1 = JSONUtil.parseObj(request.execute().body());

        ParseResult bean = jsonObject1.toBean(ParseResult.class);
        String errno = bean.getErrno();
        String realLink = null;
        if(Objects.equals(errno, "0")){
            //接口正确，获取真实链接
            ArrayList<Link> list = bean.getList();
            Link link = list.get(0);
            String dlink = link.getDlink();
            realLink = getRealLink(dlink);
        }else{
            //错误直接返回
            return bean;
        }
        //真实链接返回null 直接返回
        if(realLink == null){
            return bean;
        }
        //下载次数减1
        int currentNum = Integer.parseInt(codeNum.toString()) - 1;
        bean.setRealLink(realLink);
        bean.setCode(userCode);
        bean.setCodeUseNum(codeNum.toString());
        redisCache.setCacheObject(userCode,currentNum);
        return bean;
    }
    public String getBduss(String cookie){
        StringBuilder bduss = new StringBuilder();
        String[] split = cookie.split(";");
        Arrays.stream(split).filter(s->s.contains("BDUSS=")).forEach(s->{
            bduss.append(s.trim());
        });
        return bduss.toString();
    }
    public String getRealLink(String dlink){
        //获取vip账号
        String cookie = null;
        List<Vip> list = vipService.selectVipList();
        if(list.isEmpty()){
            return null;
        }else{
            Random random = new Random();
            int size = list.size();
            int randomIndex = random.nextInt(size);
            Vip vip = list.get(randomIndex);
            cookie = vip.getCookie();
        }
        String bduss = getBduss(cookie);
        String realLink = null;
        try {
            HttpRequest header = HttpRequest.head(dlink)
                    .header("User-Agent", "LogStatistic")
                    .header("Cookie", bduss);
            HttpResponse execute = header.execute();
            List<String> location = execute.headers().get("Location");
            realLink = location.get(0);
        } catch (Exception e) {
            return realLink;
        }
        if(!realLink.contains("tsl=0") || realLink.contains("qdall01")){
            QueryWrapper<Vip> vipQueryWrapper = new QueryWrapper<>();
            vipQueryWrapper.eq("cookie", cookie);
            Vip one = vipService.getOne(vipQueryWrapper);
            one.setStatus(1);
            boolean b = vipService.updateById(one);
            if(b){
                //限速后自动切换账号
                getRealLink(dlink);
            }
        }
        return realLink;
    }

    public String getCommonCookie(String code){
        char lastChar = code.charAt(code.length() - 1);
        int num = Integer.parseInt(String.valueOf(lastChar));
        String index = num >= 5 ? "1" : "2";
        String key = "cookie-" + index;
        Object cookie = redisCache.getCacheObject(key);
        return cookie.toString();
    }

    public String parseSk(String seckey){
        String s = seckey.replace("-", "+");
        String s1 = s.replace("~", "=");
        String s2 = s1.replace("_", "/");
        String encode = URLEncoder.encode(s2);
        return encode;
    }


    @Override
    public Object getSign(ParseCopyLink parse) {
        String shorturl = parse.getShorturl();
        String code = parse.getCode();
        String cookie = getCommonCookie(code);
        String url = "https://pan.baidu.com/share/tplconfig?&surl="+shorturl+"&fields=sign,timestamp&channel=chunlei&web=1&app_id=250528&clienttype=0";
//        String url = "https://pan.baidu.com/share/tplconfig?&surl="+shorturl+"&shareid=&uk=&fields=sign,timestamp&channel=chunlei&web=1&app_id=250528&clienttype=0";
        HttpRequest request = HttpRequest.get(url)
                .header("User-Agent","netdisk;pan.baidu.com")
                .header("Cookie",cookie);
       return JSON.parse(request.execute().body());
    }
}
