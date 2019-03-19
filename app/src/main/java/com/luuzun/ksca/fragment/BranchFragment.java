package com.luuzun.ksca.fragment;

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
import com.luuzun.ksca.domain.Branch;
import com.luuzun.ksca.util.RequestServerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BranchFragment extends Fragment {

    private RecyclerView mBranchRecyclerView;
    private BranchAdapter mBranchAdapter;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branch, container, false);

        mBranchRecyclerView = (RecyclerView) view.findViewById(R.id.branchFragment);
        mBranchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {   // updateUI : List를 생성하고 어뎁터 세팅
        NetworkTask networkTask2 = new NetworkTask();
        List<Branch> branchList = new ArrayList<>();
        String mAreaCode = getArguments().getString("areaCode");

        Map paramsMap = new HashMap();
        paramsMap.put("areaCode", mAreaCode);

        try {
            branchList = networkTask2.execute(paramsMap).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Branch branch: branchList) {
            Log.i("ksca_log",branch.toString());
        }

        mBranchAdapter= new BranchAdapter(branchList);
        mBranchRecyclerView.setAdapter(mBranchAdapter);
        Log.i("ksca_log","Set Adapter");
    }

    private class BranchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mBranchFullCodeTextView;
        private TextView mAreaNameTextView;
        private TextView mBranchCodeTextView;
        private TextView mBranchNameTextView;

        private Branch mBranch;

        public BranchHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mBranchFullCodeTextView = (TextView) itemView.findViewById(R.id.list_item_branch_full_code);
            mAreaNameTextView = (TextView) itemView.findViewById(R.id.list_item_area_name);
            mBranchCodeTextView = (TextView) itemView.findViewById(R.id.list_item_branch_code);
            mBranchNameTextView = (TextView) itemView.findViewById(R.id.list_item_branch_name);
        }

        public void bindBranch(Branch branch){
            String areaName = getArguments().getString("areaName");

            mBranch = branch;
            mBranchFullCodeTextView.setText(branch.getAreaCode());
            mAreaNameTextView.setText(areaName);
            mBranchCodeTextView.setText(branch.getBranchCode());
            mBranchNameTextView.setText(branch.getBranch());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(), mBranch.getBranch() + "선택됨", Toast.LENGTH_SHORT).show();
            //Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getmId());
            //position = getAdapterPosition();
            //startActivity(intent);
        }
    }

    private class BranchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Branch> mBranchList;
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;

        public BranchAdapter(List<Branch> mBranchList) {
            this.mBranchList = mBranchList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            View view;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == TYPE_HEADER) {
                view = layoutInflater.inflate(R.layout.list_item_branch_header, parent, false);
                holder = new HeaderViewHolder(view);
            } else {
                view = layoutInflater.inflate(R.layout.list_item_branch, parent, false);
                holder = new BranchHolder(view);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof HeaderViewHolder){
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            } else {
                BranchHolder branchHolder = (BranchHolder) holder;
                Branch branch = mBranchList.get(position-1);
                branchHolder.bindBranch(branch);
            }
        }

        @Override
        public int getItemCount() {
            return mBranchList.size()+1;
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

    public class NetworkTask extends AsyncTask<Map, Integer, List<Branch>> {
        /* doInBackground가 실행되기 이전에 동작 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Branch> doInBackground(Map... params) {
            String body = RequestServerUtil.getInstance().request("branch", params);
            Log.i("ksca_log", "Body : " + body);

            Gson gson = new Gson();

            List<Branch> branchList
                    = gson.fromJson(body, new TypeToken<List<Branch>>(){}.getType());

            return branchList;
        }

        /* doInBackground가 종료되면 동작. doInBackground가 리턴한 값을 받음. */
        @Override
        protected void onPostExecute(List<Branch> branchList) {
        }
    }
}
