package com.example.chatbotapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageActivity extends AppCompatActivity {
    private Button startDayBtn;
    private Button startTimeBtn;
    private Button endDayBtn;
    private Button endTimeBtn;
    private Button addBtn;
    private Button dbRefreshBtn;

    private TextView startText;
    private TextView endText;
    private TextView infoText;

    private String startDay = "";
    private String startTime = "";
    private String endDay = "";
    private String endTime = "";
    private String start = "";
    private String end = "";
    private String info = "";

    final int DIALOG_DATE_START = 1;
    final int DIALOG_TIME_START = 2;
    final int DIALOG_DATE_END = 3;
    final int DIALOG_TIME_END = 4;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.manage_page);

        startDayBtn = (Button)findViewById(R.id.start_day);
        startTimeBtn = (Button)findViewById(R.id.start_time);
        endDayBtn = (Button)findViewById(R.id.end_day);
        endTimeBtn = (Button)findViewById(R.id.end_time);
        addBtn = (Button)findViewById(R.id.schedule_add);
        dbRefreshBtn = (Button)findViewById(R.id.db_refresh);

        startText = (TextView)findViewById(R.id.start);
        endText = (TextView)findViewById(R.id.end);
        infoText = (TextView)findViewById(R.id.schedule_info);

        startDayBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_START);
            }
        });

        startTimeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_START);
            }
        });

        endDayBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_END);
            }
        });

        endTimeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_END);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                info = infoText.getText().toString();
                Toast.makeText(ManageActivity.this, start + "\n" + end + "\n" + info, Toast.LENGTH_SHORT).show();
            }
        });

        dbRefreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 데이터베이스 파싱 초기화 버튼 처리

            }
        });
    } //end OnCreate

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch(id){
            case DIALOG_DATE_START :
                DatePickerDialog dpds = new DatePickerDialog
                        (ManageActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        startDay = year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일";
                                        start = startDay + " " + startTime;
                                        startText.setText(start);
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2020, 6, 2); // 기본값 연월일
                return dpds;

            case DIALOG_DATE_END:
                DatePickerDialog dpde = new DatePickerDialog
                        (ManageActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        endDay = year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일";
                                        end = endDay + " " + endTime;
                                        endText.setText(end);
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2020, 6, 2); // 기본값 연월일
                return dpde;
            case DIALOG_TIME_START :
                TimePickerDialog tpds =
                        new TimePickerDialog(ManageActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        startTime = hourOfDay +"시 " + minute+"분";
                                        start = startDay + " " + startTime;
                                        startText.setText(start);
                                    }
                                }, // 값설정시 호출될 리스너 등록
                                4,19, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return tpds;
            case DIALOG_TIME_END :
                TimePickerDialog tpde =
                        new TimePickerDialog(ManageActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        endTime = hourOfDay +"시 " + minute+"분";
                                        end = endDay + " " + endTime;
                                        endText.setText(end);
                                    }
                                }, // 값설정시 호출될 리스너 등록
                                4,19, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return tpde;
        }


        return super.onCreateDialog(id);
    }
}
