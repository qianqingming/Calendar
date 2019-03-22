package com.example.calendar.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calendar.R;
import com.example.calendar.adapter.CalendarAdapter;
import com.example.calendar.adapter.StickyItemDecoration;
import com.example.calendar.entity.MyDate;
import com.example.calendar.entity.Schedule;
import com.example.calendar.utils.CalendarUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Object> list = new ArrayList<>();

    private CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleText = findViewById(R.id.title_text);
        Button backButton = findViewById(R.id.back_button);
        Button rightButton = findViewById(R.id.right_button);
        titleText.setText("日历");
        backButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);

        //初始化日历
        final Calendar cal = CalendarUtils.CALENDAR;
        final Calendar calTop = Calendar.getInstance();
        initCalendar(cal);
        cal.add(Calendar.MONTH,1);
        initCalendar(cal);

        //给RecyclerView添加布局管理
        RecyclerView calendarRecyclerView = findViewById(R.id.recycler_view_calendar);
        GridLayoutManager layoutManager = new GridLayoutManager(this,7);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (list.get(i) instanceof String){
                    return 7;
                }else {
                    return 1;
                }
            }
        });
        calendarRecyclerView.setLayoutManager(layoutManager);
        //给RecyclerView添加适配器
        adapter = new CalendarAdapter(this,list);
        //添加吸顶悬浮
        calendarRecyclerView.addItemDecoration(new StickyItemDecoration(this, new StickyItemDecoration.DecorationCallback() {
            @Override
            public String getYearAndMonth(int position) {
                if (list.get(position) instanceof String){
                    return (String)list.get(position);
                }
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
        }));
        calendarRecyclerView.setAdapter(adapter);

        //给RecyclerView添加滚动监听
        calendarRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            //newState 参数有：
            // SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
            // SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
            // SCROLL_STATE_IDLE(0) 停止滚动
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
                //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                if (newState==1 && !recyclerView.canScrollVertically(1)){
                    cal.add(Calendar.MONTH,1);
                    initCalendar(cal);
                    adapter.notifyDataSetChanged();
                }else if (newState==2 && !recyclerView.canScrollVertically(-1)){
                    calTop.add(Calendar.MONTH,-1);//设置为上一个月
                    initCalendar2(calTop);
                    adapter.notifyDataSetChanged();
                }
            }



            //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次
            // @Override
            //public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //    super.onScrolled(recyclerView, dx, dy);

                /*int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //Log.e("Main","lastVisibleItemPosition："+lastVisibleItemPosition);
                //Log.e("Main","ItemCount："+adapter.getItemCount());
                if (lastVisibleItemPosition + 5 > adapter.getItemCount()){
                    cal.add(Calendar.MONTH,1);//设置为下一个月
                    initCalendar(cal);

                    //adapter.notifySetListDataChanged(mCalList);
                    adapter.notifyDataSetChanged();
                }*/

            //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
            //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                /*boolean isBottom = recyclerView.canScrollVertically(1);
                if (!isBottom){
                    //如果滚动到了底部
                    cal.add(Calendar.MONTH,1);//设置为下一个月
                    initCalendar(cal);

                    //adapter.notifySetListDataChanged(mCalList);
                    adapter.notifyDataSetChanged();
                }*/

                /*boolean isTop = recyclerView.canScrollVertically(-1);
                if (!isTop){
                    //如果已经到达顶部
                    calTop.add(Calendar.MONTH,-1);//设置为上一个月
                    initCalendar2(calTop);
                    //adapter.notifySetListDataChanged(mCalList);
                    adapter.notifyDataSetChanged();
                }*/

                /*if(dy > 0){
                    //上滑监听
                    *//*cal.add(Calendar.MONTH,-1);//设置为上一个月
                    initCalendar(cal);
                    adapter.notifyDataSetChanged();*//*
                }else{
                    cal.add(Calendar.MONTH,1);//设置为下一个月
                    initCalendar(cal);

                    adapter.notifySetListDataChanged(mCalList);
                    adapter.notifyDataSetChanged();
                }*/
            // }
        });


        //给FloatingActionButton添加点击事件
        FloatingActionButton fab = findViewById(R.id.float_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                //判断当天是否有日程
                String key = CalendarAdapter.key;
                if(key.equals("")){
                    //如果key为空,使用当天的日期作为key
                    key = CalendarUtils.YEARANDMONTH + CalendarUtils.DAY + "日";
                }
                List<Schedule> schedules = LitePal.where("calendar=?", key).find(Schedule.class);
                if (schedules.size() > 0){
                    //如果有日程
                    String event = schedules.get(0).getSchedule();
                    intent = new Intent(MainActivity.this,ChangeEventActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("event",event);
                    intent.putExtra("id",String.valueOf(schedules.get(0).getId()));
                    startActivityForResult(intent,1);
                }else{
                    intent = new Intent(MainActivity.this,AddEventActivity.class);
                    startActivityForResult(intent,2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //刷新布局
        adapter.notifyDataSetChanged();
    }


    private void initCalendar2(Calendar cal){
        int year = cal.get(Calendar.YEAR);//年
        int month = cal.get(Calendar.MONTH) + 1;//月
        int days = CalendarUtils.daysOfMonth(year, month);//当月的天数
        int week = CalendarUtils.weekOfTheFirstDay(cal);//当月第一天是周几

        int count = 1;

        list.add(0,year+"年"+month+"月");

        MyDate date;
        if(week > 1){
            for(int i=0;i<week-1;i++){
                date = new MyDate("","");
                list.add(count,date);
                count++;
            }
        }
        for(int i=1;i<=days;i++){
            date = new MyDate(String.valueOf(i),"");
            list.add(count,date);
            count++;
        }
    }

    private void initCalendar(Calendar cal){
        int year = cal.get(Calendar.YEAR);//年
        int month = cal.get(Calendar.MONTH) + 1;//月
        int days = CalendarUtils.daysOfMonth(year, month);//当月的天数
        int week = CalendarUtils.weekOfTheFirstDay(cal);//当月第一天是周几
        list.add(year+"年"+month+"月");

        MyDate date;
        if(week > 1){
            for(int i=0;i<week-1;i++){
                date = new MyDate("","");
                list.add(date);
            }
        }
        for(int i=1;i<=days;i++){
            date = new MyDate(String.valueOf(i),"");
            list.add(date);
        }
    }
}
