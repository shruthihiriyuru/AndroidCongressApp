package com.firstApp.congressAPI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by shruthihiriyuru on 11/25/16.
 */

public class AboutMeActivity extends AppCompatActivity {

    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

        Toolbar tb = (Toolbar) findViewById(R.id.aboutmeToolbar);
        tb.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
