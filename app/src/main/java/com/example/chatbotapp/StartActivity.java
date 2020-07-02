package com.example.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends AppCompatActivity {
    private Spinner college_spinner;
    private Spinner department_spinner;
    private List<String> college_items = new ArrayList<String>();
    private List<String> department_items = new ArrayList<>();
    private TextView studentIDText;
    private TextView studentNameText;
    private String college;
    private String department;
    private String studentID;
    private String studentName;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.start_page);

        Button summitBtn = findViewById(R.id.summit);
        summitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                studentID = studentIDText.getText().toString();
                studentName = studentNameText.getText().toString();
                Toast.makeText(StartActivity.this, college + "\n" + department + "\n" + studentID + "\n" + studentName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", studentName);
                startActivity(intent);
            }
        });

        Button manageBtn = (Button)findViewById(R.id.manageBtn);
        manageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 텍스트뷰
        studentIDText = (TextView)findViewById(R.id.student_id_text);
        studentNameText = (TextView)findViewById(R.id.student_name_text);

        // 스피너 동적 추가 해보자
        college_spinner = (Spinner)findViewById(R.id.college_spinner);
        department_spinner = (Spinner)findViewById(R.id.department_spinner);

        college_items.add("인문과학대학");
        college_items.add("사회과학대학");
        college_items.add("경영경제대학");
        college_items.add("호텔관광대학");
        college_items.add("자연과학대학");
        college_items.add("생명과학대학");
        college_items.add("전자정보공학대학");
        college_items.add("소프트웨어융합대학");
        college_items.add("공과대학");
        college_items.add("예체능대학");
        college_items.add("대양휴머니티칼리지");
        college_items.add("법학부");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,
                college_items
        );
        college_spinner.setAdapter(adapter);

        college_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                college = (String) college_spinner.getItemAtPosition(position);
                department_items.clear();
                switch (position){
                    case 0: // 인문과학대학
                        department_items.add("국어국문학과");
                        department_items.add("국제학부");
                        department_items.add("역사학과");
                        department_items.add("교육학과");
                        break;
                    case 1: // 사회과학대학
                        department_items.add("행정학과");
                        department_items.add("미디어커뮤니케이션학과");
                        break;
                    case 2: // 경영경제대학
                        department_items.add("경제학과");
                        department_items.add("경영학부");
                        break;
                    case 3: // 호텔관광대학
                        department_items.add("호텔관광외식경영학부");
                        department_items.add("호텔외식관광프랜차이즈경영학과");
                        department_items.add("호텔외식비즈니스학과");
                        department_items.add("글로벌조리학과");
                        break;
                    case 4: // 자연과학대학
                        department_items.add("수학통계학부");
                        department_items.add("물리천문학과");
                        department_items.add("화학과");
                        break;
                    case 5: // 생명과학대학
                        department_items.add("생명시스템학부");
                        department_items.add("스마트생명산업융합학과");
                        break;
                    case 6: // 전자정보공학대학
                        department_items.add("전자정보통신공학과");
                        break;
                    case 7: // 소프트웨어융합대학
                        department_items.add("컴퓨터공학과");
                        department_items.add("소프트웨어학과");
                        department_items.add("정보보호학과");
                        department_items.add("데이터사이언스학과");
                        department_items.add("지능기전공학부");
                        department_items.add("창의소프트학부");
                        break;
                    case 8: // 공과대학
                        department_items.add("건축공학부");
                        department_items.add("건설환경공학과");
                        department_items.add("환경에너지공간융합학과");
                        department_items.add("지구자원시스템공학과");
                        department_items.add("기계항공우주공학부");
                        department_items.add("나노신소재공학과");
                        department_items.add("양자원자력공학과");
                        department_items.add("국방시스템공학과");
                        department_items.add("항공시스템공학과");
                        break;
                    case 9: // 예체능대학
                        department_items.add("회화과");
                        department_items.add("산업디자인학과");
                        department_items.add("패션디자인학과");
                        department_items.add("음악과");
                        department_items.add("체육학과");
                        department_items.add("무용과");
                        department_items.add("영화예술학과");
                        break;
                    case 10: // 대양휴머니티칼리지
                        department_items.add("대양휴머니티칼리지");
                        break;
                    case 11: // 법학부
                        department_items.add("법학부");
                        break;
                    default:
                        department_items.add("단과대학을 선택해주세요");
                        break;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        StartActivity.this, android.R.layout.simple_list_item_1,
                        department_items
                );
                department_spinner.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = (String) department_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
