package com.shineskoo.toolkit.sha256;

/*
 *   @Author: Cosmos
 *   @Date: 2021/1/13 11:55 上午
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * SHA256加密算法
 */
public class SHA {
    public static final String KEY_SHA = "SHA-256";

    /**
     * 获得加密后的字符串
     * @param toEncrypt 需要加密的内容
     * @return 
     */
    public static String encrypt(String toEncrypt) {
        byte[] data = toEncrypt.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(data);
            byte[] digest = messageDigest.digest();

            StringBuilder sb = new StringBuilder();
            int temp;
            for (int i = 0; i < digest.length; i++) {
                temp = digest[i];
                if (temp < 0) {
                    temp += 256;
                }
                if (temp < 16) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(temp));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(encrypt("4e28d7008a26b8350c3a7d53ef0cfdcc3fb7c90da1bd752e03c14427ec9ae4d7dd01ed58780f5587"));
    }
}

