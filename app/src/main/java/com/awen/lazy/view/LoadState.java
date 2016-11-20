package com.awen.lazy.view;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public interface LoadState {
    int STATE_UNKNOWN = -1; //未知
    int STATE_NORMAL = 0;   //未加载
    int STATE_SUCCESS = 1;  //加载成功
    int STATE_EMPTY = 2;    //数据为空
    int STATE_LOADING = 3;  //正在加载
    int STATE_ERROR = 4;    //加载失败

    void setLoadState(int statue);

    interface OnLoadCallBack {
        /**
         * 当加载数据成功后可调用此方法，对应的状态码：{@link #STATE_SUCCESS}
         */
        void onSuccess();

        /**
         * 数据为空的时候，对应的状态码：{@link #STATE_EMPTY}
         */
        void onEmpty();

        /**
         * 获取数据出错的时候，比如网络无连接，服务器无响应等错误，对应的状态码：{@link #STATE_ERROR}
         */
        void onError();

        /**
         * 正在加载，对应的状态码：{@link #STATE_LOADING}
         */
        void onLoading();
    }
}
