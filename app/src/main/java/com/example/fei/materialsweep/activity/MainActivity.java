package com.example.fei.materialsweep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.fei.materialsweep.R;

/**
 * Created by fei on 2017/7/13.
 */

public class MainActivity extends BaseActivity {

    private RelativeLayout relativeLayoutOutbound;
    private RelativeLayout relativeLayoutSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transparent();
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        relativeLayoutOutbound = (RelativeLayout) findViewById(R.id.relativeLayout_outbound);
        relativeLayoutSearch = (RelativeLayout) findViewById(R.id.relativeLayout_search);
        setListener();
    }

    private void setListener() {
        relativeLayoutOutbound.setOnClickListener(onClickListener);
        relativeLayoutSearch.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.relativeLayout_outbound:
                    startActivity(new Intent(MainActivity.this, OutboundActivity.class));
                    break;
                case R.id.relativeLayout_search:
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    break;
                default:
                    break;
            }
        }
    };
}
