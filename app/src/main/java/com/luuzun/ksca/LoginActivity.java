package com.luuzun.ksca;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.luuzun.ksca.domain.Manager;
import com.luuzun.ksca.util.RequestServerUtil;

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

            //excute의 인자는 DoInBackground의 parameter
            //get method로 DoInBackground의 return값을 가져옴
            if(result.containsKey("ERROR")) {
                String error = (String) result.get("ERROR");
                if (error.equals("FAIL")) {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (error.equals("NOT_APPROVE")){
                    Toast.makeText(getApplicationContext(), "승인 대기중입니다.", Toast.LENGTH_SHORT).show();
                } else if (error.equals("NOT_EXIST")){
                    Toast.makeText(getApplicationContext(), "이미 탈퇴한 회원입니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Manager manager = (Manager) result.get("manager");
                String areaName = (String) result.get("areaName");

                Log.i("ksca_log", manager.getId());
                Log.i("ksca_log", manager.permToString());
                Log.i("ksca_log", manager.getArea());
                Log.i("ksca_log", areaName);

                Intent intent = new Intent(this, MenuActivity.class);

                intent.putExtra("id", manager.getId());
                intent.putExtra("permission", manager.getPermission());
                intent.putExtra("areaCode", manager.getArea());
                intent.putExtra("areaName", areaName);

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
            Map<String,Object> result = new HashMap<>();
            String body = RequestServerUtil.getInstance().request("login", params);
            Log.i("ksca_log", "Body : " + body);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(body);

            if(body.contains("ERROR")) {
                String error = element.getAsJsonObject().get("ERROR").getAsString();
                result.put("ERROR",error);
                Log.i("ksca_log", "Login Error : " + error);
            } else {
                Manager manager = gson.fromJson(body, Manager.class);
                String areaName = element.getAsJsonObject().get("areaName").getAsString();
                result.put("areaName",areaName);
                result.put("manager",manager);
                Log.i("ksca_log", "Login Success");
            }
            return result;
        }

        /* doInBackground가 종료되면 동작. doInBackground가 리턴한 값을 받음. */
        @Override
        protected void onPostExecute(Map<String,Object> manager) {
        }
    }
}
