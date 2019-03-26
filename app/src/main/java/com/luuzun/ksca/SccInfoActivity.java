package com.luuzun.ksca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.luuzun.ksca.domain.SCC;

public class SccInfoActivity extends AppCompatActivity {

    private SCC mScc;
    private TextView mTfSccTitle;
    private TextView mTfCode;
    private TextView mTfDong;
    private TextView mTfName;
    private TextView mTfAddress;
    private TextView mTfRegDate;
    private TextView mTfSite;
    private TextView mTfBuilding;
    private TextView mTfMember;
    private TextView mTfMale;
    private TextView mTfFemale;
    private TextView mTfOwn;
    private TextView mTfTel;
    private TextView mTfPresident;
    private TextView mTfPhone;

    //mTfCode mTfDong mTfName mTfAddress mTfRegDate mTfSite mTfBuilding mTfMember mTfMale mTfFemale mTfOwn mTfTel mTfPresident mTfPhone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scc_info);

        Intent intent = getIntent();
        mScc = (SCC) intent.getSerializableExtra("scc");

        mTfSccTitle = findViewById(R.id.scc_info_title);
        mTfCode = findViewById(R.id.scc_info_code);
        mTfDong = findViewById(R.id.scc_info_dong);
        mTfName = findViewById(R.id.scc_info_name);
        mTfAddress = findViewById(R.id.scc_info_address);
        mTfRegDate = findViewById(R.id.scc_info_reg_date);
        mTfSite = findViewById(R.id.scc_info_site);
        mTfBuilding = findViewById(R.id.scc_info_building);
        mTfMember = findViewById(R.id.scc_info_member);
        mTfMale = findViewById(R.id.scc_info_male);
        mTfFemale = findViewById(R.id.scc_info_female);
        mTfOwn = findViewById(R.id.scc_info_own);
        mTfTel = findViewById(R.id.scc_info_tel);
        mTfPresident = findViewById(R.id.scc_info_president);
        mTfPhone = findViewById(R.id.scc_info_phone);

        mTfSccTitle.setText(mScc.getName());
        mTfCode.setText(mScc.getAreaCode() + "-" + mScc.getBranchCode() + "-" + mScc.getSccCode());
        mTfDong.setText(mScc.getDong());
        mTfName.setText(mScc.getName());
        mTfAddress.setText(mScc.getAddress());
        mTfRegDate.setText(mScc.getSimpleRegDate());
        mTfSite.setText(Float.toString(mScc.getSite()));
        mTfBuilding.setText(Float.toString(mScc.getBuilding()));
        mTfMember.setText(Integer.toString(mScc.getMember()));
        mTfMale.setText(Integer.toString(mScc.getMale()));
        mTfFemale.setText(Integer.toString(mScc.getFemale()));
        mTfOwn.setText(mScc.getOwn());
        mTfTel.setText(mScc.getTel());
        mTfPresident.setText(mScc.getPresident());
        mTfPhone.setText(mScc.getPhone());
    }

    public void onAddressClicked(View view) {
        Intent intent = new Intent(this, GoogleMapActivity.class);
        intent.putExtra("address", mScc.getAddress().split("\\(")[0]);
        intent.putExtra("name", mScc.getName());
        startActivity(intent);
    }

    public void onTelClicked(View view) {
        String srcTel = mScc.getTel();
        srcTel = srcTel.replaceAll("-", "");
        String tel="tel:"+srcTel;
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
    }

    public void onPhoneClicked(View view) {
        String srcTel = mScc.getPhone();
        srcTel = srcTel.replaceAll("-", "");
        String tel="tel:"+srcTel;
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
    }
}