package com.ruoyi.web.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
@Data
public class Common {


    public static void main(String[] args) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sekey","DX55zRCJfPgANS8UciLAhJfhkt4pe9BcqQgKARJ5b1Y=");
        String temp = "DX55zRCJfPgANS8UciLAhJfhkt4pe9BcqQgKARJ5b1Y=";
        String str = "{\n" +
                "    \"sekey\": \""+temp+"\"\n" +
                "}";
//        String string = '{"sekey": '+temp+'}';
        System.out.println(str);

    }
}
