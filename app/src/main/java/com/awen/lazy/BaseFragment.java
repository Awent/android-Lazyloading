package com.awen.lazy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awen.lazy.util.NetWorkUtil;
import com.awen.lazy.view.LoadPageHelper;
import com.awen.lazy.view.LoadState;

/**
 * 方法执行的顺序为：
 * <br>{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * <br>{@link #onCreateViewSuccess(LayoutInflater, ViewGroup, Bundle)}
 * <br>{@link #onActivityCreated(Bundle)}
 * <br>{@link #onFirstUserVisible()}
 * <br>{@link #onInit(Bundle)}
 * <br>{@link #onLoad(LoadState.OnLoadCallBack)}
 * <br>
 * 该类使用的是延迟加载，当前fragment可见的时候才会加载数据展示出来，可重写以下几个方法：
 *
 * @author Homk-M <Awentljs@gmail.com>
 * @see #onFirstUserVisible()
 * @see #onFirstUserInvisible()
 * @see #onUserVisible()
 * @see #onUserInvisible()
 */
public abstract class BaseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private boolean isPrepared;

    private LoadPageHelper loadPageHelper;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        loadPageHelper = new LoadPageHelper(getContext()) {
            @Override
            public View onCreateViewSuccess() {
                return BaseFragment.this.onCreateViewSuccess(inflater, container, savedInstanceState);
            }

            @Override
            public int onLoad(OnLoadCallBack onLoadCallBack) {
                if (!isFirstVisible) {
                    onInit(savedInstanceState);
                }
                return BaseFragment.this.onLoad(onLoadCallBack);
            }
        };
        return loadPageHelper;
    }

    /**
     * 加载成功后显示的布局，在此方法inflate布局,相当于{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Nullable
    public abstract View onCreateViewSuccess(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 当fragment第一次可见的时候会回调此方法,可进行findViewById或一些初始化操作
     */
    public abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * 可重写此方法<br>
     * 1、可在此方法加载网络或数据库等数据，如果是加载网络请求（在子线程操作的），这里的返回值意义不大,因为请求还没完已经返回了<br>
     * 2、当fragment第一次可见的时候会回调此方法，{@link #onInit(Bundle)}执行完就会执行此方法<br>
     * 3、如果是网络加载，这里有可能会回调多次,回调多次的原因是看你设置的{@link LoadState}
     *
     * @param onLoadCallBack 可通过此参数控制界面展示。比如进行网络请求成功、失败、数据空，都可通过此对象对界面的展示进行控制<br>
     *                       设置方法为：<br>
     *                       {@link LoadState.OnLoadCallBack#onEmpty()}<br>
     *                       {@link LoadState.OnLoadCallBack#onError()} <br>
     *                       {@link LoadState.OnLoadCallBack#onSuccess()}<br>
     *                       {@link LoadState.OnLoadCallBack#onLoading()}
     * @return {@link LoadState} <br>
     *     1、如果fragment可见的时候界面需要立马显示(比如个人信息界面)并跳过网络检查步骤的，重写此方法的返回值填这个：{@link LoadState#STATE_SUCCESS}<br>
     *     2、这里默认会检查网络是否有连接<br>
     * @see #setLoadState(int)
     */
    public int onLoad(LoadState.OnLoadCallBack onLoadCallBack) {
        if (!NetWorkUtil.isLinkedNetwork(getContext())) {
            return LoadState.STATE_ERROR;
        }
        return LoadState.STATE_SUCCESS;
    }

    /**
     * 此方法也是对界面展示进行控制的
     *
     * @param statue {@link LoadState}
     */
    public void setLoadState(int statue) {
        if (loadPageHelper != null) {
            loadPageHelper.setLoadState(statue);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
        Log.e(TAG, "----onFirstUserVisible----");
        if (loadPageHelper != null) {
            loadPageHelper.loadData();
        }
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
        Log.e(TAG, "----onUserVisible----");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
        Log.e(TAG, "----onFirstUserInvisible----");
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
        Log.e(TAG, "----onUserInvisible----");
    }

}
