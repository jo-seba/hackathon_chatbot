package com.example.chatbotapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;

public class ChatRequest extends AsyncTask<String, String, String> {
    private String question;
    private String department; // 학과명
    private String college; // 단과대학
    private int year; // 입학년도

    ChatRequest(int year, String college, String department, String question) {
        this.year = year;
        this.college = college;
        this.department = department;
        this.question = question;
    }

    @Override
    protected String doInBackground(String... urls) {
        JSONObject jsonObj = new JSONObject();
        System.out.println("Client Request: -1");
        try {
            jsonObj.accumulate("year", year);
            jsonObj.accumulate("college", college);
            jsonObj.accumulate("department", department);
            jsonObj.accumulate("question", question);
            System.out.println("Client Request: 0");
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                System.out.println("Client Request: 1");
                //연결을 함
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();
                System.out.println("Client Request: 2");
                //서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObj.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌
                System.out.println("Client Request: 3");
                //서버로 부터 데이터를 받음
                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String result = buffer.toString();//서버로 부터 받은 값
                System.out.println("Client Request: 4");
                return result;

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("Client Request: 5");
        System.out.println("recevie: " + result);

    }
}
