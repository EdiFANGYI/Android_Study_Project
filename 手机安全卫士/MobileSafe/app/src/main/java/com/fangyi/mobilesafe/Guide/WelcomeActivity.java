package com.fangyi.mobilesafe.Guide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.fangyi.mobilesafe.activity.MainActivity;
import com.fangyi.mobilesafe.R;
import com.fangyi.mobilesafe.utils.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by FANGYI on 2016/5/26.
 */
public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";

    private boolean isFirstIn = false;
    private static final int TIME = 2000;

    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final int SHOW_UPDATE_DIALOG = 1002;
    private static final int URL_ERROR = 1003;
    private static final int NETWORK_ERROR = 1004;
    private static final int JSON_ERROR = 1005;
    /**
     * 升级的描述信息
     */
    private String description;
    /**
     * 最新的APK升级地址
     */
    private String apkurl;


    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GO_HOME://进入主页面
                    goHome();
                    break;

                case GO_GUIDE://进入引导页面
                    goGuide();
                    break;

                case SHOW_UPDATE_DIALOG://弹出升级对话框
                    Log.e(TAG, "有新版本，弹出升级对话框");
                    break;

                case URL_ERROR://URL错位
                    goHome();
                    Toast.makeText(WelcomeActivity.this, "URL错位", Toast.LENGTH_SHORT).show();
                    break;

                case NETWORK_ERROR://网络异常
                    goHome();
                    Toast.makeText(WelcomeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;

                case JSON_ERROR://解析JSON异常
                    goHome();
                    Toast.makeText(WelcomeActivity.this, "解析JSON异常", Toast.LENGTH_SHORT).show();
                    break;
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        //引导页
        init();
    }

    private TextView tvWelcomeVersion;

    private void assignViews() {
        tvWelcomeVersion = (TextView) findViewById(R.id.tv_welcome_version);
        //设置版本号
        tvWelcomeVersion.setText("版本名：" + getVersionName());
        //检测是否有新版本
        checkVersion();
    }

    /**
     * 得到版本名称
     * @return
     */
    public String getVersionName() {
        //包管理器
        PackageManager pm = getPackageManager();
        //得到功能清单文件信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 软件的升级
     */
    private void checkVersion() {
        //升级的流程
        new Thread() {
            @Override
            public void run() {
                //请求网络，得到最新的版本信息
                Message msg = Message.obtain();
                try {
                    URL url = new URL(getString(R.string.serverurl));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    con.setRequestMethod("GET");
                    //设置超时的时间
                    con.setConnectTimeout(4000);
                    //相应码
                    if (con.getResponseCode() == 200) {
                        //请求成功
                        //把流转换成String类型
                        InputStream is = con.getInputStream();
                        String result = StreamTools.readFromStream(is);

                        Log.e(TAG, "result==" +result);
                        //解析json
                        JSONObject obj = new JSONObject(result);
                        //服务器最新的版本
                        String version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");

                        if (getVersionName().equals(version)) {
                            //没有新版本-进入主页面
                            msg.what = GO_HOME;
                        } else {
                            //弹出升级对话框
                            msg.what = SHOW_UPDATE_DIALOG;
                        }
                    }


                } catch (MalformedURLException e) {
                    //URL错位
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    //网络异常
                    e.printStackTrace();
                    msg.what = NETWORK_ERROR;
                } catch (JSONException e) {
                    //解析JSON异常
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }

    /**
     * 判断是否第一次启动
     * 进行选择
     */
    private void init(){
        SharedPreferences perPreferences = getSharedPreferences("jike", MODE_PRIVATE);
        isFirstIn = perPreferences.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
            assignViews();
        }else{
            handler.sendEmptyMessageDelayed(GO_GUIDE, 0);
            SharedPreferences.Editor editor = perPreferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.commit();
        }

    }

    private void goHome(){
        Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goGuide(){
        Intent i = new Intent(WelcomeActivity.this,Guide.class);
        startActivity(i);
        finish();
    }

}

