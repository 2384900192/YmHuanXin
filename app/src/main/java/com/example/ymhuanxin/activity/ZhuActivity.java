package com.example.ymhuanxin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ymhuanxin.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class ZhuActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mZhang1Et;
    private EditText mMi1Et;
    private Button mDeng1Bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu);
        initView();
    }

    private void initView() {
        mZhang1Et = (EditText) findViewById(R.id.et_zhang1);
        mMi1Et = (EditText) findViewById(R.id.et_mi1);
        mDeng1Bu = (Button) findViewById(R.id.bu_deng1);
        mDeng1Bu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bu_deng1:
                // TODO 20/07/20
                regisn();
                break;
            default:
                break;
        }
    }

    private void regisn() {
        String name = mZhang1Et.getText().toString();
        String user = mMi1Et.getText().toString();
        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(user)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(name, user);//同步方法
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ZhuActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    ZhuActivity.this.finish();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ZhuActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
        }else {
            Toast.makeText(this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
