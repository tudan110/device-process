package indi.tudan.deviceprocess.utils;

/**
 * 字符串处理工具类
 *
 * @author wangtan
 * @date 2019-10-27 13:43:20
 * @since 1.0
 */
public class StringUtils {

    /**
     * Don't let anyone else instantiate this class
     */
    private StringUtils() {
    }

    /**
     * 获取字符串
     *
     * @param object 待处理数据
     * @return 处理后的字符串
     * @date 2019-10-26 17:31:49
     */
    public static String getStr(Object object) {
        if (isNotBlank(object)) {
            if (object instanceof Integer) {
                return String.valueOf(object);
            } else {
                return (String) object;
            }
        } else {
            return "";
        }
    }

    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     * @date 2019-10-26 17:31:14
     */
    public static boolean isBlank(Object str) {
        return str == null || "".equals(str);
    }

    /**
     * 字符串是否不为空
     *
     * @param str 字符串
     * @return boolean
     * @date 2019-10-26 17:31:07
     */
    public static boolean isNotBlank(Object str) {
        return !isBlank(str);
    }

}
