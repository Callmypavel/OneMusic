package cn.onepeacemaker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String convertForFileName(String text){
        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(text);

        text = matcher.replaceAll(" ");
        return text;
    }
}
