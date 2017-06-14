package com.wishland.www.wanhaohuimaterialdesign.base;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.wishland.www.wanhaohuimaterialdesign.R;

import butterknife.BindView;

/**
 * Created by gerry on 2017/4/29.
 */

public abstract class BaseStyleActivity extends BaseActivtiy {
    @BindView(R.id.content)
    ProgressFrameLayout content;
    @BindView(R.id.root)
    LinearLayout root;
    private TextView tvTitle;
    private ImageButton btnBack;



    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.base_content_layout);
        root = (LinearLayout) findViewById(R.id.root);
        content = (ProgressFrameLayout) findViewById(R.id.content);
        View inflate = getLayoutInflater().inflate(R.layout.base_toolbar, root, false);
        tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        btnBack = (ImageButton) inflate.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        root.addView(inflate, 0);
        content.addView(getLayoutInflater().inflate(layoutResID, content, false));
        content.addView(getLayoutInflater().inflate(layoutResID, content, false));

    }

    public void setContentView(@LayoutRes int layoutResID, @LayoutRes int toolbarResID) {
        super.setContentView(R.layout.base_content_layout);
        root = (LinearLayout) findViewById(R.id.root);
        content = (ProgressFrameLayout) findViewById(R.id.content);
        View inflate = getLayoutInflater().inflate(toolbarResID, root, false);
        tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        btnBack = (ImageButton) inflate.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        root.addView(inflate, 0);
        content.addView(getLayoutInflater().inflate(layoutResID, content, false));
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (tvTitle != null)
            tvTitle.setText(title);
    }


    protected ProgressFrameLayout getContentView() {
        return content;
    }

}
