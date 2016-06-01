package com.fangyi.mobilesafe.activity.lostFind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fangyi.mobilesafe.R;

/**
 * Created by FANGYI on 2016/5/30.
 */
public class Setup1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    /**
     * 下一步按钮的点击事件
     * @param v
     */
    public void next(View v) {
        Log.e("dsdd","！！！！！！！！！！！！！！！！！！！！！！！！");
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        //当前页面关闭
        finish();
        //Activity切换动画，在startActivity(intent);或finish();后面写
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }
}
