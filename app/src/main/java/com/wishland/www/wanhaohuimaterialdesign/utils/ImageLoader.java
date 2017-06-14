package com.wishland.www.wanhaohuimaterialdesign.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wishland.www.wanhaohuimaterialdesign.R;

/**
 * Created by gerry
 */
public class ImageLoader {

    public static void load(Context context, String url, ImageView iv) {
        try {
            Glide.with(context).load(url).placeholder(R.drawable.loading_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load(Activity activity, String url, ImageView iv) {
        try {
            Glide.with(activity).load(url).placeholder(R.drawable.loading_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load(Fragment fragment, String url, ImageView iv) {
        try {
            Glide.with(fragment).load(url).placeholder(R.drawable.loading_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
