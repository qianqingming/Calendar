package com.example.calendar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.R;
import com.example.calendar.adapter.CalendarAdapter;
import com.example.calendar.entity.Schedule;
import com.example.calendar.utils.CalendarUtils;

public class AddEventActivity extends AppCompatActivity {

    private String key = CalendarAdapter.key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        TextView titleText = findViewById(R.id.title_text);
        Button backButton = findViewById(R.id.back_button);
        Button rightButton = findViewById(R.id.right_button);
        titleText.setText("新建日程");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText editEvent = findViewById(R.id.edit_event);
        TextView timeText = findViewById(R.id.time);

        if(key.equals("")){
            //如果key为空,使用当天的日期作为key
            key = CalendarUtils.YEARANDMONTH + CalendarUtils.DAY + "日";
        }
        timeText.setText(key);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = editEvent.getText().toString();
                if(!event.equals("")){
                    //使用数据库存储日程信息
                    Schedule schedule = new Schedule();
                    schedule.setCalendar(key);
                    schedule.setSchedule(event);
                    schedule.save();
                    Toast.makeText(AddEventActivity.this,"成功添加日程",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(AddEventActivity.this,"没有添加日程",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
