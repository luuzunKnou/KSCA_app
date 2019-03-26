package com.luuzun.ksca.fragment;

import android.content.Intent;
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
import com.luuzun.ksca.SccInfoActivity;
import com.luuzun.ksca.domain.SCC;
import com.luuzun.ksca.util.RequestServerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SccFragment extends Fragment {

    private RecyclerView mSccRecyclerView;
    private SccFragment.SccAdapter mSccAdapter;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scc, container, false);

        mSccRecyclerView = (RecyclerView) view.findViewById(R.id.sccFragment);
        mSccRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {   // updateUI : List를 생성하고 어뎁터 세팅
        SccFragment.NetworkTask networkTask = new SccFragment.NetworkTask();
        List<SCC> sccList = new ArrayList<>();
        String mAreaCode = getArguments().getString("areaCode");

        Map paramsMap = new HashMap();
        paramsMap.put("areaCode", mAreaCode);

        try {
            sccList = networkTask.execute(paramsMap).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (SCC scc: sccList) {
            Log.i("ksca_log",scc.toString());
        }

        mSccAdapter= new SccFragment.SccAdapter(sccList);
        mSccRecyclerView.setAdapter(mSccAdapter);
        Log.i("ksca_log","Set Adapter");
    }

    private class SccHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mSccCodeTextView;
        private TextView mSccDongTextView;
        private TextView mSccNameTextView;

        private SCC mScc;

        public SccHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mSccCodeTextView = (TextView) itemView.findViewById(R.id.list_item_scc_code);
            mSccDongTextView = (TextView) itemView.findViewById(R.id.list_item_scc_dong);
            mSccNameTextView = (TextView) itemView.findViewById(R.id.list_item_scc_name);
        }

        public void bindScc(SCC scc){
            mScc = scc;
            mSccCodeTextView.setText(scc.getAreaCode()+"-"+scc.getBranchCode()+"-"+scc.getSccCode());
            mSccDongTextView.setText(scc.getDong());
            mSccNameTextView.setText(scc.getName());
        }

        @Override
        public void onClick(View v) {
            position = getAdapterPosition();
            Intent intent = new Intent(getActivity(), SccInfoActivity.class);
            intent.putExtra("scc", mScc);

            startActivity(intent);
        }
    }

    private class SccAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<SCC> mSccList;
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;

        public SccAdapter(List<SCC> mSccList) {
            this.mSccList = mSccList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            View view;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == TYPE_HEADER) {
                view = layoutInflater.inflate(R.layout.list_item_scc_header, parent, false);
                holder = new SccFragment.HeaderViewHolder(view);
            } else {
                view = layoutInflater.inflate(R.layout.list_item_scc, parent, false);
                holder = new SccFragment.SccHolder(view);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof SccFragment.HeaderViewHolder){
                SccFragment.HeaderViewHolder headerViewHolder = (SccFragment.HeaderViewHolder) holder;
            } else {
                SccFragment.SccHolder sccHolder = (SccFragment.SccHolder) holder;
                SCC scc = mSccList.get(position-1);
                sccHolder.bindScc(scc);
            }
        }

        @Override
        public int getItemCount() {
            return mSccList.size()+1;
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

    public class NetworkTask extends AsyncTask<Map, Integer, List<SCC>> {
        /* doInBackground가 실행되기 이전에 동작 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<SCC> doInBackground(Map... params) {
            String body = RequestServerUtil.getInstance().request("scc", params);
            Log.i("ksca_log", "Body : " + body);

            Gson gson = new Gson();

            List<SCC> sccList
                    = gson.fromJson(body, new TypeToken<List<SCC>>(){}.getType());

            return sccList;
        }

        /* doInBackground가 종료되면 동작. doInBackground가 리턴한 값을 받음. */
        @Override
        protected void onPostExecute(List<SCC> SccList) {
        }
    }
}
