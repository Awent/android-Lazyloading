package com.awen.lazy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Awen <Awentljs@gmail.com>.
 */
public class ListFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> titles;

    public ListFragmentPagerAdapter(List<Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.mFragmentList = fragments;
    }

    public ListFragmentPagerAdapter(List<Fragment> fragments, FragmentManager fm, List<String> titles) {
        super(fm);
        this.mFragmentList = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList == null ? null : mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    //Remove a page for the given position. The adapter is responsible for removing the view from its container
    //防止重新销毁视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
