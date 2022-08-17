package com.jornah.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * AES加解密工具类，使用base64编码
 *
 * @author licong
 * @date 2022/8/16 11:10
 */
public class EncryptUtil {

    /**
     * 生成密钥对象
     */


    /**
     * 数据加密: 明文 -> 密文
     */
    public static String encrypt(String plainText, String key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key.getBytes());

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, secKey);

        // 加密数据, 返回密文
        return Base64Utils.encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    /**
     * 数据解密: 密文 -> 明文
     */
    public static String decrypt(String encryptText, String key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key.getBytes());
        byte[] cipherBytes = Base64Utils.decodeFromString(encryptText);

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secKey);

        // 解密数据, 返回明文
        return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);
    }

    private static SecretKey generateKey(byte[] key) throws Exception {
        // 根据指定的 RNG 算法, 创建安全随机数生成器
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // 设置 密钥key的字节数组 作为安全随机数生成器的种子
        random.setSeed(key);

        // 创建 AES算法生成器
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        // 初始化算法生成器
        gen.init(128, random);

        // 生成 AES密钥对象, 也可以直接创建密钥对象: return new SecretKeySpec(key, ALGORITHM);
        return gen.generateKey();
    }

}
