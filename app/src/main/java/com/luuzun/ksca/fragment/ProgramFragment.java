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
import com.luuzun.ksca.domain.ProgramJoinForList;
import com.luuzun.ksca.util.RequestServerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProgramFragment extends Fragment {

    private RecyclerView mProgramRecyclerView;
    private ProgramFragment.ProgramAdapter mProgramAdapter;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        mProgramRecyclerView = (RecyclerView) view.findViewById(R.id.programFragment);
        mProgramRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {   // updateUI : List를 생성하고 어뎁터 세팅
        ProgramFragment.NetworkTask networkTask = new ProgramFragment.NetworkTask();
        List<ProgramJoinForList> programList = new ArrayList<>();
        String mAreaCode = getArguments().getString("areaCode");

        Map paramsMap = new HashMap();
        paramsMap.put("areaCode", mAreaCode);

        try {
            programList = networkTask.execute(paramsMap).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (ProgramJoinForList program: programList) {
            Log.i("ksca_log",program.toString());
        }

        mProgramAdapter= new ProgramFragment.ProgramAdapter(programList);
        mProgramRecyclerView.setAdapter(mProgramAdapter);
        Log.i("ksca_log","Set Adapter");
    }

    private class ProgramHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mProgramNameTextView;
        private TextView mProgramAgencyTextView;
        private TextView mProgramManagerTextView;
        private TextView mProgramTelTextView;

        private ProgramJoinForList mProgram;

        public ProgramHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mProgramNameTextView = (TextView) itemView.findViewById(R.id.list_item_program_name);
            mProgramAgencyTextView = (TextView) itemView.findViewById(R.id.list_item_program_agency);
            mProgramManagerTextView = (TextView) itemView.findViewById(R.id.list_item_program_manager);
            mProgramTelTextView = (TextView) itemView.findViewById(R.id.list_item_program_tel);
        }

        public void bindProgram(ProgramJoinForList program){
            mProgram = program;
            mProgramNameTextView.setText(program.getProgram().getName());
            mProgramAgencyTextView.setText(program.getAgency().getName());
            mProgramManagerTextView.setText(program.getAgency().getManager());
            mProgramTelTextView.setText(program.getAgency().getTel());
        }

        @Override
        public void onClick(View v) {
            String srcTel = mProgram.getAgency().getTel();
            srcTel = srcTel.replaceAll("-", "");
            String tel="tel:"+srcTel;
            startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
        }
    }

    private class ProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<ProgramJoinForList> mProgramList;
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;

        public ProgramAdapter(List<ProgramJoinForList> mProgramList) {
            this.mProgramList = mProgramList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            View view;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == TYPE_HEADER) {
                view = layoutInflater.inflate(R.layout.list_item_program_header, parent, false);
                holder = new ProgramFragment.HeaderViewHolder(view);
            } else {
                view = layoutInflater.inflate(R.layout.list_item_program, parent, false);
                holder = new ProgramFragment.ProgramHolder(view);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof ProgramFragment.HeaderViewHolder){
                ProgramFragment.HeaderViewHolder headerViewHolder = (ProgramFragment.HeaderViewHolder) holder;
            } else {
                ProgramFragment.ProgramHolder programHolder = (ProgramFragment.ProgramHolder) holder;
                ProgramJoinForList program = mProgramList.get(position-1);
                programHolder.bindProgram(program);
            }
        }

        @Override
        public int getItemCount() {
            return mProgramList.size()+1;
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

    public class NetworkTask extends AsyncTask<Map, Integer, List<ProgramJoinForList>> {
        /* doInBackground가 실행되기 이전에 동작 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ProgramJoinForList> doInBackground(Map... params) {
            String body = RequestServerUtil.getInstance().request("program", params);
            Log.i("ksca_log", "Body : " + body);

            Gson gson = new Gson();

            List<ProgramJoinForList> programList
                    = gson.fromJson(body, new TypeToken<List<ProgramJoinForList>>(){}.getType());

            return programList;
        }

        /* doInBackground가 종료되면 동작. doInBackground가 리턴한 값을 받음. */
        @Override
        protected void onPostExecute(List<ProgramJoinForList> programList) {
        }
    }
}
