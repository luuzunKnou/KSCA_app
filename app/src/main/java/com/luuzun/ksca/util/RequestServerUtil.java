package com.luuzun.ksca.util;

import android.util.Log;

import java.util.Map;

public class RequestServerUtil {
    public static final RequestServerUtil instance = new RequestServerUtil();
    private RequestServerUtil() {}
    public static RequestServerUtil getInstance(){
        return instance;
    }

    private final String SERVER = "http://15.164.25.224/android/";

    public String request(String url, Map... params){
        HttpClient.Builder http = new HttpClient.Builder("POST", SERVER+url); //HTTP 요청 준비
        Log.i("ksca_log", "Call : "+SERVER+url);

        http.addAllParameters(params[0]); //파라미터 전송

        HttpClient post = http.create();
        post.request(); //HTTP 요청 전송

        int statusCode = post.getHttpStatusCode(); //응답 상태코드 가져오기
        Log.i("ksca_log", "Server Status Code : "+statusCode); //응답 본문 가져오기

        return post.getBody();
    }
}


