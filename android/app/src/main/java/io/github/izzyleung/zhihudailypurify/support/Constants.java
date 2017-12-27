package io.github.izzyleung.zhihudailypurify.support;

public final class Constants {
    private Constants() {

    }

    public static final class PackageID {
        public static final String ZHIHU = "com.zhihu.android";
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
