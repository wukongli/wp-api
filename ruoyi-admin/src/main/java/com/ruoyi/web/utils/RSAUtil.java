package com.ruoyi.web.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
    //public static String PRIVATE_KEY="MIIEoQIBAAMMMMMMMMMMMMMMMMFbuiAmYG/VOym6ZDftCy2R4oJvkP7MgS1nsxBRdnb0eAU/abVy1Wk2y1d6ZsiF35Shpy235WfkIVc9cRX71LpNxetfMiFAJSqSZ2XbNJTBWZEah/r0rBgRSAkC6NbFsgdWi6VbPGDVM+Y2pM8wQjrUi3ZCTE9SSchv/grbl/OJm/T5uaYP0VqoOV0Zzi89bqjby9XexInyCj+1hxy7+9NP1brx5qHRAuiidbtqHzVOyKpsKGPh2fJugbVhzUoqM6ebL/j1y8w92obMj3snsgykqj5T2kfSiiMxAgMBAAECggEALujqfMixJEyl+IP6TV3kG61S63EFdHrmFv+GW3b8TdUWA3VABEF9p1nHH29SirBwG1Kqd5A9Wxg2v37hVSz2Mx2gaFv4HsFna4eZfY6ZoJmfr7ag7gw1+aqit9opmmNOcVBNUoLjiZLkQiihZpATvofJZEY/epnmKudABg+9ZN86vnbA2DFnYmVLbZvWwhGyr7AnvDO+678PtpIaWL4yxRA+p+6CxQd5ZkQf8E6VGeU4qSmbksjN+fav1SwDm4hGSBvHP6r2dWZ7MITg4DomavOwCJseh9AVX9LUtygumjY/NdYoUXkyoSeniQwL/uMd1ewB9Qi8jrJj9NAxUbsXDQKBgQCe2zRMi+R5u35FL2jWs/gAgT7KQ8d0vlv/eAOD2filiEGRoTgdzAXuGY0vytICf11HZ/y0qhPuiDuNIOnvIa9kj2zHawVBbm6Fl6J6GP874V894Bh3J3oWn9ilMIfEjvM4Jlg6aKPiht1EiykLgvIim4qaFj8UTBMMkaqUZJjw6wKBgQCWBurdYj59dZ6XOlYZTDNjJriTm4MdOZrvxAOnYIdvtDcVhqWNcocR0OXYilwehLxnsbExbE3En4OmUbZZe2pOvQ96OojUQHP4vwFUD3dm6gS87yT9nwDkMFgNETVbxVUI2SYsmtPnI8NgkkSiJfbj3Jlfe4ZCu4gwWJfD2WJVUwKBgDZW4XL1v3fsrOrZptEaytDRZTsnKElE/I9M7Fbj7uwgyd0Y+usmaTjjQKqChIgaLTxJ0+Ww8BR+QXCrO5pzOV36uckVULzrG59TGV8OwfAnzb6Ks5O+y0KF90O6cBbKl2QzqaaBON46J+JUf/vSFSXFcwn2Zil5RGLeSS1to+t1AoGAcq6NAHfmDt9HQJC+Wg4uxd9GfscEneV380oiBOM8BH59EP5hNNPUPiEyYam6KzYKEzX4njAM7n2FFDEoNBSsOc45s5RlHeK7A+12JXzBKDFBN++SljDibIwr8qkx4Znhm6bqkHQ2AV0ML5j/z/c3WA08TInPItnZjCNct8BHc7MCgYAsV4y3/CtQEmxk3wQr8ng/4LZtHuXdojh8ZQ74b3gQqO/iml6XdzZJRN/fmO+ikDAY6jPQAW4RsqZ1l8W1pKbzCvuTW6jGoUyti+Lrt3HDF9SlZMHk4gc/tq1/ad4lIgHbCxBJv7UvP6YDQX+7itJmpmYmhTuYuJ11ZnAZg9qCQg==";
    //public static String PRIVATE_KEY="MIIEuwNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNgEAAoIBAF0Yu4XRw/WYTxy4rbianuqLmxKY1VIHSp0mKLDqPORT6IfX5tUmYVu6ICZgb9U7KbpkN+0LLZHigm+Q/syBLWezEFF2dvR4BT9ptXLVaTbLV3pmyIXflKGnLbflZ+QhVz1xFfvUuk3F618yIUAlKpJnZds0lMFZkRqH+vSsGBFICQLo1sWyB1aLpVs8YNUz5jakzzBCOtSLdkJMT1JJyG/+CtuX84mb9Pm5pg/RWqg5XRnOLz1uqNvL1d7EifIKP7WHHLv700/VuvHmodEC6KJ1u2ofNU7IqmwoY+HZ8m6BtWHNSiozp5sv+PXLzD3ahsyPeyeyDKSqPlPaR9KKIzECAwEAAQKCAQAu6Op8yLEkTKX4g/pNXeQbrVLrcQV0euYW/4ZbdvxN1RYDdUAEQX2nWccfb1KKsHAbUqp3kD1bGDa/fuFVLPYzHaBoW/gewWdrh5l9jpmgmZ+vtqDuDDX5qqK32imaY05xUE1SguOJkuRCKKFmkBO+h8lkRj96meYq50AGD71k3zq+dsDYMWdiZUttm9bCEbKvsCe8M77rvw+2khpYvjLFED6n7oLFB3lmRB/wTpUZ5TipKZuSyM359q/VLAObiEZIG8c/qvZ1ZnswhODgOiZq87AImx6H0BVf0tS3KC6aNj811ihReTKhJ6eJDAv+4x3V7AH1CLyOsmP00DFRuxcNAoGBAJ7bNEyL5Hm7fkUvaNaz+ACBPspDx3S+W/94A4PZ+KWIQZGhOB3MBe4ZjS/K0gJ/XUdn/LSqE+6IO40g6e8hr2SPbMdrBUFuboWXonoY/zvhXz3gGHcnehaf2KUwh8SO8zgmWDpoo+KG3USLKQuC8iKbipoWPxRMEwyRqpRkmPDrAoGBAJYG6t1iPn11npc6VhlMM2MmuJObgx05mu/EA6dgh2+0NxWGpY1yhxHQ5diKXB6EvGexsTFsTcSfg6ZRtll7ak69D3o6iNRAc/i/AVQPd2bqBLzvJP2fAOQwWA0RNVvFVQjZJiya0+cjw2CSRKIl9uPcmV97hkK7iDBYl8PZYlVTAoGANlbhcvW/d+ys6tmm0RrK0NFlOycoSUT8j0zsVuPu7CDJ3Rj66yZpOONAqoKEiBotPEnT5bDwFH5BcKs7mnM5Xfq5yRVQvOsbn1MZXw7B8CfNvoqzk77LQoX3Q7pwFsqXZDOppoE43jon4lR/+9IVJcVzCfZmKXlEYt5JLW2j63UCgYByro0Ad+YO30dAkL5aDi7F30Z+xwSd5XfzSiIE4zwEfn0Q/mE009Q+ITJhqborNgoTNfieMAzufYUUMSg0FKw5zjmzlGUd4rsD7XYlfMEoMUE375KWMOJsjCvyqTHhmeGbpuqQdDYBXQwvmP/P9zdYDTxMic8i2dmMI1y3wEdzswKBgCxXjLf8K1ASbGTfBCvyeD/gtm0e5d2iOHxlDvhveBCo7+KaXpd3NklE39+Y76KQMBjqM9ABbhGypnWXxbWkpvMK+5NbqMahTK2L4uu3ccMX1KVkweTiBz+2rX9p3iUiAdsLEEm/tS8/pgNBf7uK0mamZiaFO5i4nXVmcBmD2oJC";
    //public static String PUBLIC_KEY="MIIBITANBgkqhkiKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKi5sSmNVSB0qdJiiw6jzkU+iH1+bVJmFbuiAmYG/VOym6ZDftCy2R4oJvkP7MgS1nsxBRdnb0eAU/abVy1Wk2y1d6ZsiF35Shpy235WfkIVc9cRX71LpNxetfMiFAJSqSZ2XbNJTBWZEah/r0rBgRSAkC6NbFsgdWi6VbPGDVM+Y2pM8wQjrUi3ZCTE9SSchv/grbl/OJm/T5uaYP0VqoOV0Zzi89bqjby9XexInyCj+1hxy7+9NP1brx5qHRAuiidbtqHzVOyKpsKGPh2fJugbVhzUoqM6ebL/j1y8w92obMj3snsgykqj5T2kfSiiMxAgMBAAE=";
    public static String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALBG1hY69Hu9s1pnDOBr+6ASbfaUPxV2PX6Xgnd/4Juud+f90qOQ4/ywBqJKYcZSaLx+3woVY75ynFFv1sfAzo8CAwEAAQ==";
    public static String privateKay ="MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAsEbWFjr0e72zWmcM\n" +
            "4Gv7oBJt9pQ/FXY9fpeCd3/gm6535/3So5Dj/LAGokphxlJovH7fChVjvnKcUW/W\n" +
            "x8DOjwIDAQABAkBX+yMDeW1pDWelKWXt5Tdzz37+4UMTLS7ILkq55iNuz7Nfh+7D\n" +
            "7EPREPIPgWUTuBDUZLXTWh3Jbg7oAvpOQAkhAiEA3YM42G1JbivBREPKp7jJvvw9\n" +
            "x5bJhNQ/fYXbE2vzrEkCIQDLuKhgqP1gxLu0+4f4n6iXHuTlPGsezA3FMtfJF/G0\n" +
            "FwIgVECX+4G930CXNv7N8vNPEOxiFyscJQCR0Y17ISz7NrkCIQCoNO/R37ZWEBps\n" +
            "dMLwJeOt43RbUmegJhu4lyJUh9CqQQIgcjxOCXVj7EkTw7RmjKuyDZrYP2f307Fk\n" +
            "Cb3NkEwaljk=";
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String message = "password";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message,keyMap.get(0));
        System.out.println("明文："+message);
        System.out.println("加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
}
