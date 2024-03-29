package com.doctorwork.sword.gateway.discovery.common.util;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:08 2019/5/31
 * @Modified By:
 */
public class StringUtils {

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
