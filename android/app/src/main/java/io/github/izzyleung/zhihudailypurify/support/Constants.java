package io.github.izzyleung.zhihudailypurify.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Constants {
    private Constants() {

    }

    public static final class Dates {
        public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        @SuppressWarnings("deprecation")
        public static final Date birthday = new java.util.Date(113, 4, 19); // May 19th, 2013
    }

    public static final class Strings {
        public static final String SHARE_FROM_ZHIHU = " 分享自知乎网";
        public static final String MULTIPLE_DISCUSSION = "这里包含多个知乎讨论，请点击后选择";
    }

    public static final class Information {
        public static final String ZHIHU_PACKAGE_ID = "com.zhihu.android";
    }

    public static final class SharedPreferencesKeys {
        public static final String KEY_SHOULD_ENABLE_ACCELERATE_SERVER = "enable_accelerate_server?";
        public static final String KEY_SHOULD_USE_CLIENT = "using_client?";
        public static final String KEY_SHOULD_AUTO_REFRESH = "auto_refresh?";
        public static final String KEY_SHOULD_USE_ACCELERATE_SERVER = "using_accelerate_server?";
    }

    public static final class BundleKeys {
        public static final String DATE = "date";
        public static final String IS_SINGLE = "single?";
        public static final String IS_FIRST_PAGE = "first_page?";
    }
}
