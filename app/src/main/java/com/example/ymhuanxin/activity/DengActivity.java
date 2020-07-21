package com.example.ymhuanxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ymhuanxin.MainActivity;
import com.example.ymhuanxin.R;
import com.example.ymhuanxin.utis.SpUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class DengActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mZhangEt;
    private EditText mMiEt;
    private Button mDengBu;
    private Button mZhuBu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean loggedInBefore = EMClient.getInstance().isLoggedInBefore();
        if (loggedInBefore){//为真表示登录过 直接跳转
            startActivity(new Intent(DengActivity.this,MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_deng);
        initView();
    }

    private void initView() {
        mZhangEt = (EditText) findViewById(R.id.et_zhang);
        mMiEt = (EditText) findViewById(R.id.et_mi);
        mDengBu = (Button) findViewById(R.id.bu_deng);
        mDengBu.setOnClickListener(this);
        mZhuBu = (Button) findViewById(R.id.bu_zhu);
        mZhuBu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bu_deng:
                // TODO 20/07/20

                login();
                break;
            case R.id.bu_zhu:
                // TODO 20/07/20
                Intent intent = new Intent(DengActivity.this, ZhuActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void login() {
        String name = mZhangEt.getText().toString().trim();
        String user = mMiEt.getText().toString().trim();
        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(user)){
            EMClient.getInstance().login(name, user, new EMCallBack() {
                @Override
                public void onSuccess() {//登陆成功
                    //以下两个方法是为了保证进入主页面后本地会话和群组都 load 完毕。
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    Log.d("main", "登录聊天服务器成功！");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DengActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            SpUtils.getParam(DengActivity.this,"name",name);
                            Intent intent1 = new Intent(DengActivity.this, MainActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {//登陆失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DengActivity.this, "登陆失败"+s, Toast.LENGTH_SHORT).show();
                            Log.e("login", "run: "+s );
                        }
                    });

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }
}
