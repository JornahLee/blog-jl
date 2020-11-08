package com.wip.utils;

import org.apache.commons.lang3.StringUtils;

public class MyStringUtil {
    public static String LineNoFormat = "::L-%d::";
    public static String LineNoRegex = "::L-\\d{1,5}::";

    public static void main(String[] args) {
        String str = "woshiadjasljdas\r\n sfsdf\n奥术大师\n sdfsdf\n\n\n\n啊撒大声地\n啊撒大声地\n";
        String s = generateLineNumberForText(str, LineNoFormat, false);
        System.out.println(s);
        String rmStr = rmLineNumberForText(s, LineNoRegex);
        System.out.println(rmStr);
    }

    public static String generateLineNumberForText(String input, String lineNoFormat, boolean atHead) {
        String macStyle = StringUtils.replace(input, "\r\n", "\n");
        // 统一一下，换行符，如果是windows \r\n，存入的时候，自动替换为 \n
        String[] source = macStyle.split("\n");
        String[] target = new String[source.length];
        if (atHead) {
            for (int index = 0; index < source.length; index++) {
                target[index] = String.format(lineNoFormat, index + 1) + source[index] + "\n";
            }
        } else {
            for (int index = 0; index < source.length; index++) {
                target[index] = source[index] + String.format(lineNoFormat, index + 1) + "\n";
            }
        }
        return String.join("", target);
    }

    public static String rmLineNumberForText(String input, String regex) {
        return input.replaceAll(regex, "");
    }
}
