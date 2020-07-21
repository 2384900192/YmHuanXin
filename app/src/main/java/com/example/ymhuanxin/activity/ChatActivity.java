package com.example.ymhuanxin.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.example.ymhuanxin.R;
import com.example.ymhuanxin.adapter.MessageAdapter;
import com.example.ymhuanxin.utis.AudioUtil;
import com.example.ymhuanxin.utis.ResultCallBack;
import com.example.ymhuanxin.utis.SpUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTxtTitle;
    /**
     * 请输入聊天信息
     */
    private EditText mEtMessage;
    /**
     * 发送文本信息
     */
    private Button mBtnSendTxt;
    private RecyclerView mRlvMessage;
    private String mToName;
    private String mMyName;
    private EMMessageListener mMsgListener;
    private ArrayList<EMMessage> mMessages;
    private MessageAdapter mAdapter;
    /**
     * 开始录音
     */
    private Button mBtnLuying;
    /**
     * 发送录音
     */
    private Button mBtnSendAudio;
    private MediaPlayer mMp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
        initRlv();
        getOldMsg();//获得历史聊天信息
        initGetMessage();
    }

    private void getOldMsg() {
        //获得我和这个好友的聊天回话
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mToName);
        if (conversation == null)//如果没有历史聊天信息，则不用处理
            return;
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        mMessages.addAll(messages);
        mAdapter.notifyDataSetChanged();
    }

    private void initRlv() {
        mRlvMessage.setLayoutManager(new LinearLayoutManager(this));
        mMessages = new ArrayList<>();
        mAdapter = new MessageAdapter(this, mMessages, mMyName);
        mRlvMessage.setAdapter(mAdapter);
        mAdapter.setOnItemClickLis(new MessageAdapter.OnItemClickLis() {
            @Override
            public void onItemClick(int position) {
                EMMessage emMessage = mMessages.get(position);
                String body = emMessage.getBody().toString();
                if(body.startsWith("voice")){//如果消息以 vocie开头  播放声音
                    String[] strs = body.split(",");//安 逗号 分割，得到每一段信息
                    for (String str:strs) {//循环遍历得到每一段信息
                        if (str.startsWith("localurl")) {//此段信息为  录音的保存路径信息
                            String videoPath = str.split(":")[1];//以 ： 分割的，通过 下标1得到具体的存放路径
                            File file = new File(videoPath);
                            if(file.exists()){//语音文件存在则播放
                                try {
                                    if (mMp != null) {
                                        mMp.stop();
                                        mMp.release();
                                    }
                                    mMp = new MediaPlayer();//创建新的 Mediaplayer对象，播放语音
                                    mMp.setDataSource(videoPath);//加载资源
                                    mMp.prepare();//准备资源
                                    mMp.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(ChatActivity.this, "录音丢失", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });
    }

    private void startMp(String filePath) throws IOException {
        if(mMp != null){
            mMp.stop();
            mMp.release();
        }
        mMp = new MediaPlayer();
        mMp.setDataSource(filePath);
        mMp.prepare();
        mMp.start();
    }

    private void initGetMessage() {
        mMsgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                for (int i = 0; i < messages.size(); i++) {
                    EMMessage emMessage = messages.get(i);//得到当前的一条消息
                    String from = emMessage.getFrom();//得到消息的来源，谁发的
                    if (from.equals(mToName)) {//如果消息是当前聊天对象发送过来的，则显示，否则不显示
                        mMessages.add(emMessage);
                    }
                }
                //切主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);//添加接收消息的监听

    }

    private void initData() {
        mToName = getIntent().getStringExtra("toName");//聊天对象
        mMyName = (String) SpUtils.getParam(this, "name", "未登录");
        mTxtTitle.setText(mMyName + "和" + mToName + "在尬聊：");


    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mBtnSendTxt = (Button) findViewById(R.id.btn_send_txt);
        mBtnSendTxt.setOnClickListener(this);
        mRlvMessage = (RecyclerView) findViewById(R.id.rlv_message);
        mBtnLuying = (Button) findViewById(R.id.btn_luying);
        mBtnLuying.setOnClickListener(this);
        mBtnSendAudio = (Button) findViewById(R.id.btn_send_audio);
        mBtnSendAudio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_send_txt:
                sendText();//发送消息
                break;
            case R.id.btn_luying:
                if(AudioUtil.isRecording){//正在录音，停止停止
                    AudioUtil.stopRecord();
                    mBtnLuying.setText("开始录音");
                }else {//没有录音
                    startRecord();//开始录音
                    mBtnLuying.setText("停止录音");
                }
                break;
            case R.id.btn_send_audio:
                sendAudio();
                break;
        }
    }

    private void sendAudio() {
        if(AudioUtil.isRecording || mFileFath == null){//正在录音或者录音文件为空，不能发送
            return;//返回  下面的鄂代码不再执行
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                //发送语音信息
                EMMessage message = EMMessage.createVoiceSendMessage(mFileFath, (int) mDuration, mToName);
                EMClient.getInstance().chatManager().sendMessage(message);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMessages.add(message);//把自己发送的消息添加到列表中
                        mAdapter.notifyDataSetChanged();
                    }
                });
                mFileFath = null;
                mDuration = 0;

            }
        }.start();
    }

    private String mFileFath;
    private long mDuration;
    private void startRecord() {
        AudioUtil.startRecord(new ResultCallBack() {
            @Override
            public void onSuccess(String filePath, long duration) {
                //得到录音的路径和大小
                mFileFath = filePath;
                mDuration = duration;
            }

            @Override
            public void onFail(String str) {

            }
        });
    }

    //发送消息
    private void sendText() {
        String msg = mEtMessage.getText().toString().trim();//得到发送的文本信息
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(msg, mToName);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        mMessages.add(message);//把自己发送的消息添加到列表中
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        记得在不需要的时候移除listener，如在activity的onDestroy()时
//        EMClient.getInstance().chatManager().removeMessageListener(mMsgListener);
    }
}
