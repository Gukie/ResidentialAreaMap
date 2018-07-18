package lokia.com.utils;

import android.widget.Toast;

public class ToastUtils {
    public static void showText(String msg) {
        Toast.makeText(BaseApplication.mContext,msg,2);

    }
}
