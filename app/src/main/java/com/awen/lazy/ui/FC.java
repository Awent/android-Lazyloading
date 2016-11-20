package com.awen.lazy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awen.lazy.BaseFragment;
import com.awen.lazy.R;
import com.awen.lazy.view.LoadState;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class FC extends BaseFragment {

    @Nullable
    @Override
    public View onCreateViewSuccess(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment,container,false);
    }

    @Override
    public void onInit(@Nullable Bundle savedInstanceState) {
        TextView content = (TextView) getView().findViewById(R.id.content);
        content.setText("C");
    }

    @Override
    public int onLoad(final LoadState.OnLoadCallBack onLoadCallBack) {
        //你可以在这里去加载网络数据或数据库数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //更新ui
                            onLoadCallBack.onError();
                            //这样设置也可以
                            //setLoadState(LoadState.STATE_ERROR);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return LoadState.STATE_LOADING;
    }
}
