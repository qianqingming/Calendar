package com.example.calendar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.R;
import com.example.calendar.entity.Schedule;

import org.litepal.LitePal;

public class ChangeEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_event);

        TextView titleText = findViewById(R.id.title_text);
        Button backButton = findViewById(R.id.back_button);
        Button rightButton = findViewById(R.id.right_button);
        titleText.setText("修改日程");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView tv_change = findViewById(R.id.change_event);
        TextView timeText = findViewById(R.id.text_view_time);

        Intent intent = getIntent();
        final String key = intent.getStringExtra("key");
        final String event = intent.getStringExtra("event");
        final String id = intent.getStringExtra("id");

        tv_change.setText(event);
        timeText.setText(key);


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_change = tv_change.getText().toString();
                if(!event_change.equals(event)){
                    if ("".equals(event_change)){
                        LitePal.delete(Schedule.class,Long.parseLong(id));
                    }else {
                        Schedule schedule = new Schedule();
                        schedule.setSchedule(event_change);
                        schedule.update(Long.parseLong(id));
                        //schedule.updateAll("calendar=?",key);
                    }
                    finish();
                }else{
                    Toast.makeText(ChangeEventActivity.this,"没有修改",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
