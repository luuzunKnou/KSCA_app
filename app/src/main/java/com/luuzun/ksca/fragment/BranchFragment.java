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
import android.widget.Toast;

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
    private String mAreaCode;

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

        mAreaCode = getArguments().getString("areaCode");

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

        if (mBranchAdapter == null){
            mBranchAdapter= new BranchAdapter(branchList);
            mBranchRecyclerView.setAdapter(mBranchAdapter);
        } else {
            Log.d("ksca_log","@@Position: "+position);

            mBranchAdapter.notifyItemChanged(position);
            //mAdapter.notifyDataSetChanged();
        }
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
            mBranch = branch;
            mBranchFullCodeTextView.setText(branch.getAreaCode()+"-"+branch.getBranchCode());
            mAreaNameTextView.setText("");
            mBranchCodeTextView.setText(branch.getBranchCode());
            mBranchNameTextView.setText(branch.getBranch());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mBranch.getBranch() + "선택됨", Toast.LENGTH_SHORT).show();
            //Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getmId());
            //position = getAdapterPosition();
            //startActivity(intent);
        }
    }

    private class BranchAdapter extends RecyclerView.Adapter<BranchHolder>{
        private List<Branch> mBranchList;

        public BranchAdapter(List<Branch> mBranchList) {
            this.mBranchList = mBranchList;
        }

        @Override
        public BranchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_branch, parent, false);
            return new BranchHolder(view);
        }

        @Override
        public void onBindViewHolder(BranchHolder holder, int position) {
            Branch branch = mBranchList.get(position);
            holder.bindBranch(branch);
        }

        @Override
        public int getItemCount() {
            return mBranchList.size();
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
