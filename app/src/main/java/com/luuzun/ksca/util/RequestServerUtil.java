package com.luuzun.ksca.util;

import android.util.Log;

import java.util.Map;

public class RequestServerUtil {
    public static final RequestServerUtil instance = new RequestServerUtil();
    private RequestServerUtil() {}
    public static RequestServerUtil getInstance(){
        return instance;
    }

    private final String SERVER = "http://192.168.0.2:8080/android/";

    public String request(String url, Map... params){
        // HTTP 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder("POST", SERVER+url);

        Log.i("ksca_log", "Call : "+SERVER+url);

        // 파라미터 전송
        http.addAllParameters(params[0]);

        // HTTP 요청 전송
        HttpClient post = http.create();
        post.request();

        // 응답 상태코드 가져오기
        int statusCode = post.getHttpStatusCode();
        Log.i("ksca_log", "Server Status Code : "+statusCode);

        // 응답 본문 가져오기

        return post.getBody();
    }
}
