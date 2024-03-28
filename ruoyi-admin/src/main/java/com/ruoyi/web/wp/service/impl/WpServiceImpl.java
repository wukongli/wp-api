package com.ruoyi.web.wp.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.web.wp.dto.ParseCopyLink;
import com.ruoyi.web.wp.dto.ParseLink;
import com.ruoyi.web.wp.service.WpService;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URLDecoder;
import java.net.URLEncoder;

@Service
public class WpServiceImpl implements WpService {
    @Override
    public Object parseList(ParseCopyLink parse) {
        String url = "https://pan.baidu.com/share/wxlist?channel=weixin&version=2.2.2&clienttype=25&web=1";
        HttpRequest request = HttpRequest.post(url)
                .header("User-Agent","netdisk")
                .header("Cookie","BDUSS=UdsN25HUzJHa0JwbFA5N3lUQmI1TndHMHdMQkJlSmh1M2xYSEpjUXRPaDhrQjltRUFBQUFBJCQAAAAAAAAAAAEAAAAUjy9EwO6087TzMDgyOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHwD-GV8A~hlV")
                .header("Referer","https://pan.baidu.com/disk/home");
        request.form("shorturl",parse.getShorturl()).form("dir",parse.getDir()).form("root",parse.getRoot()).form("pwd",parse.getPwd()).form("num","1000").form("order","time");
        request.contentType("application/x-www-form-urlencoded");
        Object parse1 = JSON.parse(request.execute().body());
        return parse1;
    }

    @Override
    public Object parseLink(ParseLink parse) {
        String cookie = "XFCS=72FB30DB92B610A60C570DBE64D718E765CC9F6FF22C3C42151A4F484119228F; XFT=axt40840u1DrL1AWIuJyzDA4Wpc770d6ryuXtRW4u6M=; XFI=12ac29c1-2572-9e6f-7336-007b09340c5d; ab_sr=1.0.1_ZGQ0NTZjYTY0NTJhOTYwMDY2YTczZWUzOTEzOGNmNDY3MzE2YWExZTk4M2YwNjc1NmYyODNjNjY4YzVmMzkyYzdjMGE2ZGNkYTU3MzhlODczYjAzM2Q1YzNiMTllNWQ0ZjkxNjJlZjQzNjFmYzBkZDYxMjZkMWU0N2JiMjFlZDhjMzc4ZDBhZDM0ODhkZWI1YjBjN2Q0MTNlNzU1NzllZWI0NWMwMDE4ZGM3MDViZjlkNWZjNGI5OGI3MTQxYzFj; ndut_fmt=7481935518680DEFF1EF5B55322F01E9DE6FCA766E0EB2CDA56AA00FEB23F4B9; PANPSC=14289207901933885514%3ACU2JWesajwBYvg9Ylv4ga0YemHfSgA%2FTY99dOkDKw9QM9Sh8JJFaWxnO789E14pGK9sKmG0jScxntU%2BhEo6jIXGdWXTOv55xRNjpS2SKb1qcCNpWJnWVTsp3zBZhJr1MxFuBIW3y156QvaoB4mrv5XUvHO53SMyxMRmYrlFHgmqF9Kes%2FZQ%2FFe4PuTzNr1rr; STOKEN=b2962b87f820667ce31903212b3777cec72c383bf3c9ae39f6b2580a386f04f3; BDUSS=UdsN25HUzJHa0JwbFA5N3lUQmI1TndHMHdMQkJlSmh1M2xYSEpjUXRPaDhrQjltRUFBQUFBJCQAAAAAAAAAAAEAAAAUjy9EwO6087TzMDgyOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHwD-GV8A~hlV; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BCLID=10139106855655402495; BCLID_BFESS=10139106855655402495; BDSFRCVID=RzPOJexroG3M8lnqUK6ub7Hp_9NbUdrTDYrEjGc3VtzSGYLVYE-bEG0Pts1-dEub6j30ogKKBeOTHn0F_2uxOjjg8UtVJeC6EG0Ptf8g0M5; BDSFRCVID_BFESS=RzPOJexroG3M8lnqUK6ub7Hp_9NbUdrTDYrEjGc3VtzSGYLVYE-bEG0Pts1-dEub6j30ogKKBeOTHn0F_2uxOjjg8UtVJeC6EG0Ptf8g0M5; H_BDCLCKID_SF=tbC8VCDKJKD3H48k-4QEbbQH-UnLqh5O0mOZ04n-ah058Dn1hRQlyTK0-GJ7bp48BDrP0Pom3UTKsq76Wh35K5tTQP6rLqc4HCr4KKJxbP8aKJbH5tK-M6JQhUJiBMj-Ban7WDJIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbC8lejuaj6bLep3K2D6aKC5bL6rJabC3V4cMXU6q2bDeQNbuqP7X-GTi_t5tJhn2_IT8-lofKl0vWq54afb2tacJXfOTylR4oqKz-xonDh83KNLLKUQtKJcBoKJO5hvvER5O3M7_DMKmDloOW-TB5bbPLUQF5l8-sq0x0bOte-bQXH_E5bj2qRAt_IK53f; H_BDCLCKID_SF_BFESS=tbC8VCDKJKD3H48k-4QEbbQH-UnLqh5O0mOZ04n-ah058Dn1hRQlyTK0-GJ7bp48BDrP0Pom3UTKsq76Wh35K5tTQP6rLqc4HCr4KKJxbP8aKJbH5tK-M6JQhUJiBMj-Ban7WDJIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbC8lejuaj6bLep3K2D6aKC5bL6rJabC3V4cMXU6q2bDeQNbuqP7X-GTi_t5tJhn2_IT8-lofKl0vWq54afb2tacJXfOTylR4oqKz-xonDh83KNLLKUQtKJcBoKJO5hvvER5O3M7_DMKmDloOW-TB5bbPLUQF5l8-sq0x0bOte-bQXH_E5bj2qRAt_IK53f; newlogin=1; csrfToken=UznJuTRV-4WlwJC_Q76JjuIW; H_PS_PSSID=40207_40215_40280_40080_40365_40352_40368_40376_40334_40416; PSINO=1; delPer=0; BA_HECTOR=a0a100a58ga58k8h2g0l8l00ikrjvt1ivdjg51t; Hm_lvt_fa0277816200010a74ab7d2895df481b=1709997532; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1709606971,1710307539; BDCLND=cRoA4OVYS%2Bupbvs87A2ZFa6GBla15SbtLemnZ4LjpH4%3D; PANWEB=1; ZFY=eJGg1E:BHvq:BpdEWDurireOOW3qlmawgw9rBwnMQipH0:C; BAIDUID=9C81E3FDCCDDA372E89F2A5BF9502189:FG=1; BIDUPSID=9C81E3FDCCDDA3722F487C08FFB37A68; PSTM=1695797530";
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
                .header("Cookie",cookie)
                .header("Referer","https://pan.baidu.com/disk/home");
        request.form("encrypt",0)
                .form("extra",extra)
                .form("fid_list","["+fsId+"]")
                .form("primaryid",shareid)
                .form("uk",uk)
                .form("product","share")
                .form("type","nolimit");
        request.contentType("application/x-www-form-urlencoded");
        Object parse1 = JSON.parse(request.execute().body());
        return parse1;
    }
}
