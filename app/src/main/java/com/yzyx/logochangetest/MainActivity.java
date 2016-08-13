package com.yzyx.logochangetest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageLoader;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView logo;
    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = (ImageView) findViewById(R.id.imageView);
        logo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        ImageConfig imageConfig = new ImageConfig.Builder(new ImageLoader() {//ImageLoader定义imageseclet的每一项的缩略图的加载
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {//此处暂时若使用picasso会出现无缩略图的状况，没解决
                Glide.with(context)
                        .load(path)
                        .centerCrop()//优化性能作用，与resize方法联用
                        .into(imageView);

            }
        })
                .crop(1, 2, 800, 800)// (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
                .mutiSelect()//（默认为多选）
                .singleSelect()  // 开启单选   （默认为多选）
                .filePath("/ImageSelector/Pictures") // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .showCamera() // 开启拍照功能 （默认关闭）
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open(MainActivity.this, imageConfig);   // 开启图片选择器
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//得到选择的裁剪过的图片的路径
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);//此处暂时为有优化
            for (String path : pathList) {
                Glide.with(MainActivity.this).load(path).transform(new CircleTransform(MainActivity.this)).into(logo);
                System.out.println(path);
            }
        }

    }
}
