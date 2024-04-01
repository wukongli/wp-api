package com.ruoyi.web.wp.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.redis.RedisCache;
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

@Service
public class WpServiceImpl implements WpService {
    @Resource
    private VipService vipService;

    @Resource
    private RedisCache redisCache;

    @Override
    public Object parseList(ParseCopyLink parse) {
        Object cookie = redisCache.getCacheObject("cookie");
        String bduss = getBduss(cookie.toString());
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
        Object codeNum = redisCache.getCacheObject(userCode);
        if("0".equals(codeNum.toString())){
            ParseResult parseResult = new ParseResult();
            parseResult.setErrno("0");
            parseResult.setCode(userCode);
            parseResult.setCodeUseNum("0");
            redisCache.deleteObject(userCode);
            return parseResult;
        }

        //使用普通cookie 获取dlink 地址。
        Object cookie = redisCache.getCacheObject("cookie");
        String sign = parse.getSign();
        String timestamp = parse.getTimestamp();
        String fsId = parse.getFs_id();
        String uk = parse.getUk();
        String randsk = parse.getRandsk();
        if(randsk.contains("%")){
            randsk = URLDecoder.decode(randsk);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("sekey",randsk);
        String extra = URLEncoder.encode(jsonObject.toString());
        String shareid = parse.getShareid();
        String url = "https://pan.baidu.com/api/sharedownload?app_id=250528&channel=chunlei&clienttype=12&sign="+sign+"&timestamp="+timestamp+"&web=1";
        HttpRequest request = HttpRequest.post(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.69")
                .header("Cookie",cookie.toString())
                .header("Referer","https://pan.baidu.com/disk/home");
        request.form("encrypt",0)
                .form("extra",extra)
                .form("fid_list","["+fsId+"]")
                .form("primaryid",shareid)
                .form("uk",uk)
                .form("product","share")
                .form("type","nolimit");
        request.contentType("application/x-www-form-urlencoded");
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
        //真是链接返回null 直接返回
        if(realLink == null){
            return bean;
        }
        //下载次数加1
        int currentNum = Integer.parseInt(codeNum.toString()) - 1;
        bean.setRealLink(realLink);
        bean.setCode(userCode);
        bean.setCodeUseNum(currentNum+"");
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


    @Override
    public Object getSign(ParseCopyLink parse) {
        String shorturl = parse.getShorturl();
        Object cookie = redisCache.getCacheObject("cookie");
        String url = "https://pan.baidu.com/share/tplconfig?&surl="+shorturl+"&shareid=&uk=&fields=sign,timestamp&channel=chunlei&web=1&app_id=250528&clienttype=0";
        HttpRequest request = HttpRequest.get(url)
                .header("User-Agent","netdisk;pan.baidu.com")
                .header("Cookie",cookie.toString());
       return JSON.parse(request.execute().body());
    }
}
