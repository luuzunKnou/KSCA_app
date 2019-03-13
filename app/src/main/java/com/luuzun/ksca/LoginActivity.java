package com.luuzun.ksca;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.luuzun.ksca.domain.Manager;
import com.luuzun.ksca.util.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private EditText mEtId;
    private EditText mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnLoginClicked(View view) {
        NetworkTask networkTask = new NetworkTask();

        mEtId = findViewById(R.id.et_id);
        mEtPassword = findViewById(R.id.et_password);

        Map paramsMap = new HashMap();
        paramsMap.put("id", mEtId.getText());
        paramsMap.put("password", mEtPassword.getText());

        try {
            Map<String,Object> result = networkTask.execute(paramsMap).get();
            Manager manager = (Manager) result.get("manager");
            String areaName = (String) result.get("areaName");

            //excute의 인자는 DoInBackground의 parameter
            //get method로 DoInBackground의 return값을 가져옴
            if(manager == null){
                Snackbar.make(view, "아이디 또는 비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
                Log.i("ksca_log", manager.getId());
                Log.i("ksca_log", manager.permToString());
                Log.i("ksca_log", manager.getArea());
                Log.i("ksca_log", areaName);

                Intent intent = new Intent(this, MenuActivity.class);

                intent.putExtra("id", manager.getId());
                intent.putExtra("permission", manager.getPermission());
                intent.putExtra("areaCode", manager.getArea());
                intent.putExtra("areaNode", areaName);

                startActivity(intent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public class NetworkTask extends AsyncTask<Map, Integer, Map<String,Object>> {
        /* doInBackground가 실행되기 이전에 동작 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected  Map<String,Object> doInBackground(Map... params) {
            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.2:8080/android/login");

            // 파라미터 전송
            http.addAllParameters(params[0]);

            // HTTP 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();
            Log.i("ksca_log", "Body : " + body);

            //객체로 받기
            Gson gson = new Gson();
            Manager manager = gson.fromJson(body, Manager.class);

            //문자열 받기
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(body);
            String areaName = element.getAsJsonObject().get("areaName").getAsString();

            Map<String,Object> result = new HashMap<>();
            result.put("manager",manager);
            result.put("areaName",areaName);

            return result;
        }

        /* doInBackground가 종료되면 동작. doInBackground가 리턴한 값을 받음. */
        @Override
        protected void onPostExecute(Map<String,Object> manager) {
        }
    }
}
