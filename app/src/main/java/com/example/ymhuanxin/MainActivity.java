package com.example.ymhuanxin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ymhuanxin.activity.ChatActivity;
import com.example.ymhuanxin.activity.DengActivity;
import com.example.ymhuanxin.adapter.MyAdapter;
import com.example.ymhuanxin.utis.SpUtils;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private MyAdapter myAdapter;
    private List<String> list;
    private String mName;
    private boolean isPermissionRequested;
    private TextView mTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    private void initView() {
        mTitleTxt = (TextView) findViewById(R.id.txt_title);
        //得到登录的账号名
        mName = (String) SpUtils.getParam(this, "name", "未登录");
        mTitleTxt.setText("当前登录人是：" + mName);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        myAdapter = new MyAdapter(this, list);
        mRecycler.setAdapter(myAdapter);
        myAdapter.setModeerssd(new MyAdapter.Modeerssd() {
            @Override
            public void post(int i) {
                String toName = list.get(i);
                if (mName.equals(toName)) {
                    Toast.makeText(MainActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                } else {
                    Intent it = new Intent(MainActivity.this, ChatActivity.class);
                    it.putExtra("toName", toName);
                    startActivity(it);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "退出登录");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            EMClient.getInstance().logout(true);
//            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DengActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissionsList = new ArrayList<>();

            String[] permissions = {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
            };
            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (permissionsList.isEmpty()) {
                return;
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 0);
            }
        }
    }
}
