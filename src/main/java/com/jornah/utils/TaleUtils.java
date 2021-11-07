package com.jornah.utils;

import com.jornah.constant.WebConst;
import com.jornah.controller.AttachController;
import com.jornah.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaleUtils {

    /**
     * 匹配邮箱正则
     */
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern UN_FORMAT_HEAD = Pattern.compile("^#+(?=(?!.*#| ))", Pattern.MULTILINE);
    private static final Pattern HEAD_LINE = Pattern.compile("^#+.+", Pattern.MULTILINE);

    /**
     * 获取session中的用户
     *
     * @param request
     * @return
     */
    public static User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null == session) {
            return null;
        }
        return (User) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
    }


    /**
     * 获取保存文件的位置，jar所在的目录的路径
     *
     * @return
     */
    public static String getUploadFilePath() {
        String path = TaleUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1, path.length());
        try {
            java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/") + 1;
        path = path.substring(0, lastIndex);
        File file = new File("");
        return file.getAbsolutePath() + "/";
    }

    public static String getFileKey(String name) {
        String prefix = "upload/" + DateKit.dateFormat(new Date(), "yyyy/MM/dd");
        if (!new File(AttachController.CLASSPATH + prefix).exists()) {
            new File(AttachController.CLASSPATH + prefix).mkdirs();
        }
        name = StringUtils.trimToNull(name);
        if (name == null) {
            return prefix + "/" + UUID.UU32() + "." + null;
        } else {
            name = name.replace('\\', '/');
            name = name.substring(name.lastIndexOf("/") + 1);
            int index = name.lastIndexOf(".");
            String ext = null;
            if (index > 0) {
                ext = StringUtils.trimToNull(name.substring(index + 1));
            }
            return prefix + "/" + UUID.UU32() + "." + (ext == null ? null : (ext));
        }
    }

    /**
     * 判断文件是否是图片类型
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(InputStream imageFile) {
        try {
            Image img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;

        }
    }

    public static LinkedHashMap<String,Integer> getHeadLineFrom(String md) {
        LinkedHashMap<String,Integer> result = new LinkedHashMap<>();
        if (StringUtils.isBlank(md)) {
            return result;
        }
        Matcher matcher = HEAD_LINE.matcher(md);
        while (matcher.find()) {
            String group = Optional.of(matcher.group()).orElse("").trim();
            int level = StringUtils.countMatches(group, '#');
            String headHref=group.replaceAll("#+","").trim();
            result.put(headHref.replaceAll(" ","-"),level);
        }

        return result;
    }

    @Test
    public void test() {
        String str = "# 1\n" +
                "\n" +
                "## 11\n" +
                "\n" +
                "### 111\n" +
                "\n" +
                "# 2\n" +
                "\n" +
                "## 22\n" +
                "\n" +
                "# 3\n" +
                "\n" +
                "## 33\n" +
                "\n" +
                "# 4\n" +
                "\n" +
                "## 44\n" +
                "\n" +
                "目录\n" +
                "\n" +
                "[TOC]";
        getHeadLineFrom(str).entrySet().stream().forEach(System.out::println);


    }

    /**
     * 判断是否是邮箱
     *
     * @param emailStr
     * @return
     */
    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 替换HTML脚本
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }
}
