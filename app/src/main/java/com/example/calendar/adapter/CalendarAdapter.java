package com.example.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.R;
import com.example.calendar.activity.ChangeEventActivity;
import com.example.calendar.activity.MainActivity;
import com.example.calendar.entity.MyDate;
import com.example.calendar.entity.Schedule;
import com.example.calendar.utils.CalendarUtils;

import org.litepal.LitePal;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> list;
    private final int HEADER_TYPE = 1;
    private final int DATE_TYPE = 2;

    private View last_choosed_view = null;//保存上次选中的日期
    boolean flag = false;
    public static String key = "";//持久化待办事项所用到的键

    public CalendarAdapter(Context mContext, List<Object> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("aaa","onCreateViewHolder————"+viewType);
        if (viewType == HEADER_TYPE){
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.header_layout,null,false));
        }else {
            return new DateHolder(LayoutInflater.from(mContext).inflate(R.layout.date_layout,null,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder){
            HeaderHolder headerHolder = (HeaderHolder) holder;
            final String year = (String) list.get(position);
            headerHolder.tv_header.setText(year);
            /*headerHolder.tv_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,year,Toast.LENGTH_SHORT).show();
                }
            });*/
        }else if (holder instanceof DateHolder){
            final DateHolder dateHolder = (DateHolder) holder;
            final MyDate myDate = (MyDate) list.get(position);
            dateHolder.day.setText(myDate.getDay());

            //获取数据库中的日程填充到TextView中
            final String yearAndMonth = getYear(position);
            final List<Schedule> scheduleList = LitePal.select("schedule").where("calendar=?", yearAndMonth+myDate.getDay()+"日").find(Schedule.class);
            if(scheduleList.size() > 0){
                dateHolder.todo.setText(scheduleList.get(0).getSchedule());
            }else {
                dateHolder.todo.setText("");
            }

            //Log.e("aaaYear",yearAndMonth + position);

            //设置当天的颜色
            if (yearAndMonth.equals(CalendarUtils.YEARANDMONTH) && myDate.getDay().equals(CalendarUtils.DAY)){
                dateHolder.day.setBackgroundResource(R.drawable.text_view_today);
            }else {
                dateHolder.day.setBackgroundResource(R.drawable.text_view_noback);
            }
            dateHolder.day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"".equals(myDate.getDay())){
                        key = yearAndMonth + myDate.getDay() + "日";
                        Toast.makeText(mContext, key,Toast.LENGTH_SHORT).show();
                        //改变选中日期的颜色
                        if (yearAndMonth.equals(CalendarUtils.YEARANDMONTH) && myDate.getDay().equals(CalendarUtils.DAY)){
                            if(last_choosed_view != null){
                                DateHolder last_holder = new DateHolder(last_choosed_view);
                                last_holder.day.setBackgroundResource(R.drawable.text_view_noback);
                            }
                        }else {
                            dateHolder.day.setBackgroundResource(R.drawable.text_view_choosed);
                            if (!flag){
                                //第一次点击
                                last_choosed_view = v;
                                flag = true;
                            }else {
                                if (last_choosed_view != v){
                                    //再次点击的不是同一个
                                    DateHolder last_holder = new DateHolder(last_choosed_view);
                                    last_holder.day.setBackgroundResource(R.drawable.text_view_noback);
                                    last_choosed_view = v;
                                }
                            }
                        }
                    }
                }
            });
            dateHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeEventActivity.class);
                    if(scheduleList.size() > 0){
                        intent.putExtra("key",yearAndMonth + myDate.getDay() + "日");
                        intent.putExtra("event",scheduleList.get(0).getSchedule());
                        intent.putExtra("id",String.valueOf(scheduleList.get(0).getId()));
                        ((MainActivity)v.getContext()).startActivityForResult(intent,1);
                    }
                }
            });
        }
    }

    private String getYear(int position){
        String year = null;
        boolean flag = true;
        while (flag){
            position--;
            if (list.get(position) instanceof String){
                year = (String) list.get(position);
                flag = false;
            }
        }
        return year;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof String){
            return HEADER_TYPE;
        }else if (list.get(position) instanceof MyDate){
            return DATE_TYPE;
        }
        return super.getItemViewType(position);
    }


    public static class HeaderHolder extends RecyclerView.ViewHolder{

        TextView tv_header;

        public HeaderHolder(View itemView) {
            super(itemView);
            tv_header = itemView.findViewById(R.id.tv_year);
        }
    }

    public static class DateHolder extends RecyclerView.ViewHolder{

        TextView day;
        TextView todo;

        public DateHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tv_day);
            todo = itemView.findViewById(R.id.tv_todo);
        }
    }
}
