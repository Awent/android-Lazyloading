package com.awen.lazy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.awen.lazy.R;

/**
 * Created by Awen <Awentljs@gmail.com>.
 */
public abstract class LoadPageHelper extends FrameLayout implements View.OnClickListener, LoadState {
    private int currentState = STATE_NORMAL; //当前状态
    private View loadingPage;
    private View errorPage;
    private View emptyPage;
    private View successPage;
    private View viewStub;
    private OnLoadCallBack onLoadCallBack;

    public LoadPageHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public LoadPageHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadPageHelper(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        viewStub = LayoutInflater.from(getContext()).inflate(R.layout.load_page_layout, null);
        addView(viewStub);
        showPage();
        onLoadCallBack = new OnLoadCallBack() {
            @Override
            public void onSuccess() {
                setLoadState(STATE_SUCCESS);
            }

            @Override
            public void onEmpty() {
                setLoadState(STATE_EMPTY);
            }

            @Override
            public void onError() {
                setLoadState(STATE_ERROR);
            }

            @Override
            public void onLoading() {
                setLoadState(STATE_LOADING);
            }
        };
    }

    @Override
    public void onClick(View v) {
        loadData();  //重新调用加载数据方法
    }

    /**
     * 根据当前状态决定显示哪个布局
     */
    private void showPage() {
        if (successPage == null) {
            successPage = onCreateViewSuccess();//这里开始加载fragment的布局
            if(successPage != null){
                addView(successPage);
            }
        }
        if(currentState == STATE_UNKNOWN){
            return;
        }
        switch (currentState) {
            case STATE_SUCCESS:
                if (successPage != null) {
                    successPage.setVisibility(VISIBLE);
                    viewStub.setVisibility(GONE);
                }
                break;
            case STATE_EMPTY:
                if (emptyPage == null) {
                    ViewStub stub = (ViewStub) viewStub.findViewById(R.id.empty_viewstub);
                    emptyPage = stub.inflate();
                }
                emptyPage.setVisibility(VISIBLE);
                //点击重试事件
                emptyPage.setOnClickListener(this);
                if (errorPage != null) {
                    errorPage.setVisibility(GONE);
                }
                if (loadingPage != null) {
                    loadingPage.setVisibility(GONE);
                }
                if (successPage != null) {
                    successPage.setVisibility(GONE);
                    viewStub.setVisibility(VISIBLE);
                }
                break;
            case STATE_ERROR:
                if (errorPage == null) {
                    ViewStub stub = (ViewStub) viewStub.findViewById(R.id.error_viewstub);
                    errorPage = stub.inflate();
                }
                errorPage.setVisibility(VISIBLE);
                //点击重试事件
                errorPage.setOnClickListener(this);
                if (loadingPage != null) {
                    loadingPage.setVisibility(GONE);
                }
                if (emptyPage != null) {
                    emptyPage.setVisibility(GONE);
                }
                if (successPage != null) {
                    successPage.setVisibility(GONE);
                    viewStub.setVisibility(VISIBLE);
                }
                break;
            case STATE_NORMAL:
            case STATE_LOADING:
                if (loadingPage == null) {
                    ViewStub stub = (ViewStub) viewStub.findViewById(R.id.loading_viewstub);
                    loadingPage = stub.inflate();
                }
                loadingPage.setVisibility(VISIBLE);
                if (errorPage != null) {
                    errorPage.setVisibility(GONE);
                }
                if (emptyPage != null) {
                    emptyPage.setVisibility(GONE);
                }
                if (successPage != null) {
                    successPage.setVisibility(GONE);
                    viewStub.setVisibility(VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开始加载数据,此方法必须运行在主线程<br>
     * this method must run in mainThread
     */
    public void loadData() {
        if (currentState != STATE_LOADING) {
            currentState = STATE_LOADING;
            int resultState = onLoad(onLoadCallBack);
            if (resultState >= STATE_NORMAL) {
                currentState = resultState;//网络加载结束后，更新网络状态
            }
        }
        //根据最新状态来刷新页面
        showPage();
    }

    @Override
    public void setLoadState(int statue) {
        currentState = statue;
        showPage();
    }

    /**
     * 加载成功后显示的布局
     * @return fragment inflate的layout
     */
    public abstract View onCreateViewSuccess();

    /**
     * 加载数据
     * @param onLoadCallBack OnLoadCallBack
     * @return {@link LoadState}
     */
    public abstract int onLoad(OnLoadCallBack onLoadCallBack);

}
