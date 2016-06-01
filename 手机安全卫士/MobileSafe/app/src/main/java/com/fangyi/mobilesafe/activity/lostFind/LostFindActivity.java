package com.fangyi.mobilesafe.activity.lostFind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fangyi.mobilesafe.R;

/**
 * Created by FANGYI on 2016/5/30.
 */
public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        //判断是否做过设置页面向导，如果没有做过就跳转到设置向导页面第一个页面，否者就加载手机防盗页面
        boolean configed = sp.getBoolean("configed", false);
        if (configed) {
            setContentView(R.layout.activity_lost_find);
        } else {
            enterSeeting();
        }

    }

    /**
     * 进入设置向导页面
     */
    private void enterSeeting() {
        //跳转到手机防盗设置向导第一个页面
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        //把当前页面-手机防盗页面关闭掉
        finish();
    }

    /**
     * 重新进入设置向导
     * @param v
     */
    public void reEnterSetting(View v) {
        enterSeeting();
    }
}
