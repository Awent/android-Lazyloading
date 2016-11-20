package com.awen.lazy;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.awen.lazy.adapter.ListFragmentPagerAdapter;
import com.awen.lazy.ui.FA;
import com.awen.lazy.ui.FB;
import com.awen.lazy.ui.FC;
import com.awen.lazy.ui.FD;
import com.awen.lazy.ui.FE;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FA());
        fragments.add(new FB());
        fragments.add(new FC());
        fragments.add(new FD());
        fragments.add(new FE());
        ListFragmentPagerAdapter adapter = new ListFragmentPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
}
