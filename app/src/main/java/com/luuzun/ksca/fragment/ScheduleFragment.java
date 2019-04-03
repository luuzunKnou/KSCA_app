package com.luuzun.ksca.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luuzun.ksca.R;
import com.luuzun.ksca.domain.ScheduleJoinforList;
import com.luuzun.ksca.util.RequestServerUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ScheduleFragment extends Fragment {

    private RecyclerView mScheduleRecyclerView;
    private LinearLayout mCalendarCell;
    private ScheduleFragment.ScheduleAdapter mScheduleAdapter;

    private TextView mMonthTextView;
    private TextView mYearTextView;
    private Button mBtnLeft;
    private Button mBtnRight;

    private List<ScheduleJoinforList> scheduleList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mScheduleRecyclerView = view.findViewById(R.id.calendar);
        mScheduleRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5));
        mYearTextView = view.findViewById(R.id.schdule_year);
        mMonthTextView = view.findViewById(R.id.schdule_month);
        mBtnLeft=view.findViewById(R.id.schedule_before);
        mBtnRight=view.findViewById(R.id.schedule_after);

        mBtnLeft.setOnClickListener(new Button.OnClickListener(){ //왼쪽 버튼
            @Override
            public void onClick(View view) {
                int month = Integer.parseInt(((String)mMonthTextView.getText()).substring(1));
                int year = Integer.parseInt(mYearTextView.getText().toString());

                if(month<=1){
                    mYearTextView.setText(String.valueOf(year-1));
                    mMonthTextView.setText(".12");
                    Log.i("ksca_log",String.valueOf(year-1)+"-"+"12");
                } else {
                    mMonthTextView.setText("."+String.valueOf(month-1));
                    Log.i("ksca_log",year+"-"+String.valueOf(month-1));
                }

                updateUI();
            }
        });

        mBtnRight.setOnClickListener(new Button.OnClickListener(){ //오른쪽 버튼
            @Override
            public void onClick(View view) {
                int month = Integer.parseInt(((String)mMonthTextView.getText()).substring(1));
                int year = Integer.parseInt(mYearTextView.getText().toString());

                if(month>=12){
                    mYearTextView.setText(String.valueOf(year+1));
                    mMonthTextView.setText(".1");
                    Log.i("ksca_log",String.valueOf(year-1)+"-"+"12");
                } else {
                    mMonthTextView.setText("."+String.valueOf(month+1));
                    Log.i("ksca_log",year+"-"+String.valueOf(month-1));
                }

                updateUI();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        Calendar cal = Calendar.getInstance();

        mYearTextView.setText(String.valueOf(cal.get(Calendar.YEAR)));
        mMonthTextView.setText("."+(cal.get(Calendar.MONTH)+1));
        updateUI();

        super.onResume();
    }

    private void updateUI() {   // updateUI : List를 생성하고 어뎁터 세팅
        ScheduleFragment.NetworkTask networkTask = new ScheduleFragment.NetworkTask();
        String mAreaCode = getArguments().getString("areaCode");

        /* Get schedule list*/
        Map paramsMap = new HashMap();
        paramsMap.put("areaCode", mAreaCode);
        paramsMap.put("year", mYearTextView.getText());
        paramsMap.put("month", ((String)mMonthTextView.getText()).substring(1));

        try {
            scheduleList = networkTask.execute(paramsMap).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(ScheduleJoinforList schdule : scheduleList){
            Log.i("ksca_log", schdule.toString());
        }

        /* Get dateList */
        List<Integer> dateList = createCal();
        for (int date: dateList) {
            Log.i("ksca_log", String.valueOf(date));
        }

        mScheduleAdapter= new ScheduleFragment.ScheduleAdapter(dateList);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);
        Log.i("ksca_log","Set Adapter");
    }

    private List<Integer> createCal(){
        String year = (String) mYearTextView.getText();
        String month = ((String) mMonthTextView.getText()).substring(1);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.set(Calendar.DATE, 1);

        int lastDate = cal.getActualMaximum(Calendar.DATE); //마지막 날짜
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); //시작 요일 // 1:일요일 2:월요일

        List<Integer> dateList = new ArrayList<>();

        for (int i=1; i<=lastDate; i++){
            if(i==1) { //1일 이전의 공백
                for(int j=1; j<dayOfWeek; j++) {
                    dateList.add(0);
                }
            }

            if(dayOfWeek%7!=1 && dayOfWeek%7!=0) { //토,일요일에 해당하는 날짜는 표시하지 않음
                dateList.add(i);
            }

            dayOfWeek++;
        }

        if(cal.get(Calendar.DAY_OF_WEEK)==1){ //일요일 시작 : 정상

        } else if(cal.get(Calendar.DAY_OF_WEEK)==7) { //토요일 시작 : 5칸 밀림
            dateList.remove(4);
            dateList.remove(3);
            dateList.remove(2);
            dateList.remove(1);
            dateList.remove(0);
        } else { //월~금요일 시작 : 한칸 밀림
            dateList.remove(0);
        }

        return dateList;
    }

    private class ScheduleHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mDateTextView;

        public ScheduleHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCalendarCell = itemView.findViewById(R.id.calendar_cell);
            mDateTextView = itemView.findViewById(R.id.calendar_date);
        }

        public void bindSchedule(int date){
            if(date==0){
                mDateTextView.setText("");
            } else {
                mDateTextView.setText(String.valueOf(date));

                int srcDate;
                for(ScheduleJoinforList schedule : scheduleList){
                    srcDate = Integer.parseInt(schedule.getSchedule().getSimpleDate().split("-")[2]);
                    if(srcDate==date){
                        //TextView 생성
                        TextView textView = new TextView(getContext());
                        textView.setText(schedule.getScc().getName());
                        textView.setTextSize(10);
                        textView.setPadding(5,0,0,5);
                        textView.setTextColor(Color.parseColor("#"+schedule.getOfferProgram().getColor()));

                        //layout_width, layout_height 설정
                        LinearLayout.LayoutParams layoutParams
                                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textView.setLayoutParams(layoutParams);

                        //부모 뷰에 추가
                        mCalendarCell.addView(textView);
                    }
                }
            }
        }

        @Override
        public void onClick(View v) {
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder>{
        private List<Integer> mDateList;

        public ScheduleAdapter(List<Integer> mDateList) {
            this.mDateList = mDateList;
        }

        @Override
        public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_schedule, parent, false);
            return new ScheduleHolder(view);
        }

        @Override
        public void onBindViewHolder(ScheduleHolder holder, int position) {
            int date = mDateList.get(position);
            holder.bindSchedule(date);
        }

        @Override
        public int getItemCount() {
            return mDateList.size();
        }
    }

    public class NetworkTask extends AsyncTask<Map, Integer, List<ScheduleJoinforList>> {
        ProgressDialog asyncDialog;

        @Override
        protected void onPreExecute() {
            Log.i("ksca_log", "Loading..");
            super.onPreExecute();

            asyncDialog = new ProgressDialog(getActivity());
            asyncDialog.setTitle("Loading");
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Loading...");
            asyncDialog.show();
        }

        @Override
        protected List<ScheduleJoinforList> doInBackground(Map... params) {
            String body = RequestServerUtil.getInstance().request("schedule", params);
            Log.i("ksca_log", "Body : " + body);

            Gson gson = new Gson();

            List<ScheduleJoinforList> scheduleList
                    = gson.fromJson(body, new TypeToken<List<ScheduleJoinforList>>(){}.getType());

            return scheduleList;
        }

        @Override
        protected void onPostExecute(List<ScheduleJoinforList> scheduleList) {
            Log.i("ksca_log", "Complete..");
            super.onPostExecute(scheduleList);
            asyncDialog.dismiss();
        }
    }
}
