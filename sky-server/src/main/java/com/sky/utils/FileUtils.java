package com.sky.utils;/**
 * @description
 * @author starlord
 * @create 2025/5/29
 */

import cn.hutool.crypto.digest.DigestUtil;

import java.io.File;

/**
 * @author starlord
 * @description
 * @create 2025/5/29
 */
public class FileUtils {

    /**
     * 计算文件的 MD5 值（推荐使用 Hutool）
     *
     * @param file 文件对象
     * @return MD5 值（32 位十六进制字符串）
     */
    public static String calculateMD5(File file) {
        // 使用 Hutool 的 SecureUtil 直接计算文件 MD5
        return DigestUtil.md5Hex(file);
    }
}
