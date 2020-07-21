package com.example.ymhuanxin.utis;

public interface ResultCallBack {
    void onSuccess(String filePath, long duration);
    void onFail(String str);
}
