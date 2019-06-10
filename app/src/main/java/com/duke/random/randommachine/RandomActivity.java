package com.duke.random.randommachine;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.duke.random.randommachine.wheelview.RotateListener;
import com.duke.random.randommachine.wheelview.WheelSurfView;

import java.util.ArrayList;
import java.util.Random;

public class RandomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView result = findViewById(R.id.result);

        final ArrayList<String> data = getIntent().getStringArrayListExtra(Constants.KEY_DATA_ARRAY);
        if (data == null) {
            return;
        }

        String[] des = new String[data.size()];
        Integer[] colors = new Integer[data.size()];

        if (data.size() % 2 != 0) {
            for (int i = 0; i < data.size(); i++) {
                des[i] = data.get(i);
                if (i == 0) {
                    colors[0] = Color.parseColor("#fef9f7");
                } else if (i % 2 == 0) {
                    colors[i] = Color.parseColor("#ffdecc");
                } else {
                    colors[i] = Color.parseColor("#fbc6a9");
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                des[i] = data.get(i);
                if (i % 2 == 0) {
                    colors[i] = Color.parseColor("#ffdecc");
                } else {
                    colors[i] = Color.parseColor("#fbc6a9");
                }
            }
        }

        final WheelSurfView wheelSurfView = findViewById(R.id.wheel_surfView);
        WheelSurfView.Builder build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmDeses(des)
                .setmType(1)
                .setmTypeNum(data.size())
                .build();
        wheelSurfView.setConfig(build);

        //添加滚动监听
        wheelSurfView.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                result.setText("随机结果：" + des);
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {

            }

            @Override
            public void rotateBefore(ImageView goImg) {
                //模拟位置
                int position = new Random().nextInt(data.size()) + 1;
                wheelSurfView.startRotate(position);
            }
        });
    }
}
