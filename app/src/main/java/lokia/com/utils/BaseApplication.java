package lokia.com.utils;

import android.content.Context;

public class BaseApplication {
    public static Context mContext;

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        BaseApplication.mContext = mContext;
    }
}
