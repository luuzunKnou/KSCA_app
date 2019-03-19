package com.luuzun.ksca.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luuzun.ksca.R;
import com.luuzun.ksca.domain.Agency;
import com.luuzun.ksca.util.RequestServerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AgencyFragment extends Fragment {

    private RecyclerView mAgencyRecyclerView;
    private AgencyFragment.AgencyAdapter mAgencyAdapter;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agency, container, false);

        mAgencyRecyclerView = (RecyclerView) view.findViewById(R.id.agencyFragment);
        mAgencyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {   // updateUI : List를 생성하고 어뎁터 세팅
        AgencyFragment.NetworkTask networkTask2 = new AgencyFragment.NetworkTask();
        List<Agency> agencyList = new ArrayList<>();
        String mAreaCode = getArguments().getString("areaCode");

        Map paramsMap = new HashMap();
        paramsMap.put("areaCode", mAreaCode);

        try {
            agencyList = networkTask2.execute(paramsMap).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Agency agency: agencyList) {
            Log.i("ksca_log",agency.toString());
        }

        mAgencyAdapter= new AgencyFragment.AgencyAdapter(agencyList);
        mAgencyRecyclerView.setAdapter(mAgencyAdapter);
        Log.i("ksca_log","Set Adapter");
    }

    private class AgencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mAgencyNameTextView;
        private TextView mAgencyManagerTextView;
        private TextView mAgencyTelTextView;

        private Agency mAgency;

        public AgencyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAgencyNameTextView = (TextView) itemView.findViewById(R.id.list_item_agency_name);
            mAgencyManagerTextView = (TextView) itemView.findViewById(R.id.list_item_agency_manager);
            mAgencyTelTextView = (TextView) itemView.findViewById(R.id.list_item_agency_tel);
        }

        public void bindAgency(Agency agency){
            String areaName = getArguments().getString("areaName");

            mAgency = agency;
            mAgencyNameTextView.setText(agency.getName());
            mAgencyManagerTextView.setText(agency.getManager());
            mAgencyTelTextView.setText(agency.getTel());
        }

        @Override
        public void onClick(View v) {
            String srcTel = mAgency.getTel();
            srcTel = srcTel.replaceAll("-", "");
            String tel="tel:"+srcTel;
            startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
        }
    }

    private class AgencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Agency> mAgencyList;
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;

        public AgencyAdapter(List<Agency> mAgencyList) {
            this.mAgencyList = mAgencyList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            View view;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == TYPE_HEADER) {
                view = layoutInflater.inflate(R.layout.list_item_agency_header, parent, false);
                holder = new AgencyFragment.HeaderViewHolder(view);
            } else {
                view = layoutInflater.inflate(R.layout.list_item_agency, parent, false);
                holder = new AgencyFragment.AgencyHolder(view);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof AgencyFragment.HeaderViewHolder){
                AgencyFragment.HeaderViewHolder headerViewHolder = (AgencyFragment.HeaderViewHolder) holder;
            } else {
                AgencyFragment.AgencyHolder agencyHolder = (AgencyFragment.AgencyHolder) holder;
                Agency agency = mAgencyList.get(position-1);
                agencyHolder.bindAgency(agency);
            }
        }

        @Override
        public int getItemCount() {
            return mAgencyList.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_HEADER;
            else
                return TYPE_ITEM;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View headerView) {
            super(headerView);
        }
    }

    public class NetworkTask extends AsyncTask<Map, Integer, List<Agency>> {
        /* doInBackground가 실행되기 이전에 동작 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Agency> doInBackground(Map... params) {
            String body = RequestServerUtil.getInstance().request("agency", params);
            Log.i("ksca_log", "Body : " + body);

            Gson gson = new Gson();

            List<Agency> agencyList
                    = gson.fromJson(body, new TypeToken<List<Agency>>(){}.getType());

            return agencyList;
        }

        /* doInBackground가 종료되면 동작. doInBackground가 리턴한 값을 받음. */
        @Override
        protected void onPostExecute(List<Agency> agencyList) {
        }
    }
}
