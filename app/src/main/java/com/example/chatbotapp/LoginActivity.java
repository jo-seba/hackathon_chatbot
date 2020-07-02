package com.example.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private String id;
    private String pw;
    private TextView manage_id;
    private TextView manage_pw;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.manage_login);

        // 텍스트 뷰 초기화
        manage_id = (TextView)findViewById(R.id.manage_id);
        manage_pw = (TextView)findViewById(R.id.manage_pw);

        Button manageLogin = (Button)findViewById(R.id.manage_login);
        manageLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id = manage_id.getText().toString();
                pw = manage_pw.getText().toString();

                Toast.makeText(LoginActivity.this, id + "\n" + pw, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
                startActivity(intent);
            }
        });

    }

}
