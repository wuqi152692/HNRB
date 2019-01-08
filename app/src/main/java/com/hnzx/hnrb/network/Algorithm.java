package com.hnzx.hnrb.network;

import android.util.Log;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Algorithm {

    /**
     * MD5加密
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String Md5Encrypt(String message, String encode) throws Exception {
        MessageDigest bmd5 = MessageDigest.getInstance("MD5");
        bmd5.update(message.getBytes());
        StringBuffer buf = new StringBuffer();
        String result = toHexString(bmd5.digest(buf.toString().getBytes(encode)));
        return result;
    }

    /**
     * 3DES加密
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String DesEncrypt(String message, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("GBK"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("GBK"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return toHexString(cipher.doFinal(message.getBytes("GBK")));
    }

    /**
     * Byte数组转字符串
     * @param
     * @return
     */
    private static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString().toUpperCase(Locale.ENGLISH);
    }

    /**
     * 3DES解密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String DesDecrypt(String message, String key)
            throws Exception {
        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("GBK"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("GBK"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte, "GBK");
    }

    /**
     * 字符串转Byte数组
     *
     * @param ss
     * @return
     */
    private static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }


    /**
     * MD5加密
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String Md5Encrypt16(String message) {
        String result;
        try {
            result = Md5Encrypt(message, "GBK").substring(8, 24).toLowerCase(
                    Locale.ENGLISH);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

}
