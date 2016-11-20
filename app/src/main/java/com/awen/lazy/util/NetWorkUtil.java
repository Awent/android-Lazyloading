package com.awen.lazy.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class NetWorkUtil {

    /**
     * 判断当前网络是否连同
     *
     * @return
     */
    public static boolean isLinkedNetwork(Context context) {
        ConnectivityManager connecte = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connecte.getActiveNetworkInfo() != null && connecte.getActiveNetworkInfo().isConnected();
    }
}
